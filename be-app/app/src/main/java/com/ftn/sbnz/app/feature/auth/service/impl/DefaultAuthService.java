package com.ftn.sbnz.app.feature.auth.service.impl;

import com.ftn.sbnz.app.core.country.service.CountryService;
import com.ftn.sbnz.app.core.user.abstract_user.exception.UserAlreadyExistException;
import com.ftn.sbnz.app.core.user.abstract_user.exception.UserNotFoundException;
import com.ftn.sbnz.app.core.user.abstract_user.service.UserService;
import com.ftn.sbnz.app.core.user.organizer.OrganizerResponseDto;
import com.ftn.sbnz.app.core.user.organizer.mapper.OrganizerMapper;
import com.ftn.sbnz.app.core.user.organizer.service.OrganizerService;
import com.ftn.sbnz.app.core.user.visitor.VisitorMapper;
import com.ftn.sbnz.app.core.user.visitor.VisitorResponseDto;
import com.ftn.sbnz.app.core.user.visitor.service.VisitorService;
import com.ftn.sbnz.app.feature.auth.dto.login.TokenResponseDto;
import com.ftn.sbnz.app.feature.auth.dto.login.UserCredentialsDto;
import com.ftn.sbnz.app.feature.auth.dto.user.request.CreateOrganizerDto;
import com.ftn.sbnz.app.feature.auth.dto.user.request.CreateVisitorDto;
import com.ftn.sbnz.app.feature.auth.exception.InvalidCredentialsException;
import com.ftn.sbnz.app.feature.auth.service.AuthService;
import com.ftn.sbnz.app.web_security.jwt.JwtService;
import com.ftn.sbnz.app.web_security.user_details.UserDetailsImpl;
import com.ftn.sbnz.model.core.CountryEntity;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.core.UserEntity;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DefaultAuthService  implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final UserService userService;
    private final VisitorService visitorService;
    private final OrganizerService organizerService;
    private final CountryService countryService;

    private final VisitorMapper visitorMapper;
    private final OrganizerMapper organizerMapper;

    @Override
    public TokenResponseDto generateToken(UserCredentialsDto loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException(e.getMessage());
        }

        UserEntity user = userService.findByEmail(loginRequest.getEmail()).orElseThrow();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", user.getId());
        extraClaims.put("roles", user.getRole());
        String jwt = jwtService.generateToken(extraClaims, new UserDetailsImpl(user));

        return  TokenResponseDto.builder()
                .token(jwt)
                .build();
    }

    @Transactional
    @Override
    public VisitorResponseDto registerVisitor(CreateVisitorDto requestDto) {
        visitorService.findByEmail(requestDto.getEmail()).ifPresent(user -> {
            throw new UserAlreadyExistException();
        });

        VisitorEntity newVisitor = visitorMapper.toEntity(requestDto, passwordEncoder);
        VisitorEntity newActivatedVisitor = newVisitor.toBuilder().enabled(true).build();
        CountryEntity country = countryService.getEntityById(UUID.fromString(requestDto.getCountryId()));
        newActivatedVisitor.setCountry(country);
        VisitorEntity savedVisitor = visitorService.save(newActivatedVisitor);
        return visitorMapper.toDto(savedVisitor);
    }

    @Transactional
    @Override
    public OrganizerResponseDto registerOrganizer(CreateOrganizerDto requestDto) {
        organizerService.findByEmail(requestDto.getEmail()).ifPresent(user -> {
            throw new UserAlreadyExistException();
        });

        OrganizerEntity newOrganizer = organizerMapper.toEntity(requestDto, passwordEncoder);
        OrganizerEntity newActivatedOrganizer = newOrganizer.toBuilder().enabled(true).build();
        OrganizerEntity savedOrganizer = organizerService.save(newActivatedOrganizer);
        return organizerMapper.toDto(savedOrganizer);
    }

    @Override
    public UserEntity getUserForCurrentSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userService.findByEmail(currentPrincipalName).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public VisitorEntity getVisitorForCurrentSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return visitorService.findByEmail(currentPrincipalName).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public OrganizerEntity getOrganizerForCurrentSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return organizerService.findByEmail(currentPrincipalName).orElseThrow(UserNotFoundException::new);
    }

}
