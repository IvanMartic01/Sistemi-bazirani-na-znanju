package com.ftn.sbnz.service.feature.auth.controller;

import com.ftn.sbnz.service.core.user.visitor.VisitorResponseDto;
import com.ftn.sbnz.service.feature.auth.dto.login.TokenResponseDto;
import com.ftn.sbnz.service.feature.auth.dto.login.UserCredentialsDto;
import com.ftn.sbnz.service.feature.auth.dto.user.request.CreateOrganizerDto;
import com.ftn.sbnz.service.feature.auth.dto.user.request.CreateVisitorDto;
import com.ftn.sbnz.service.feature.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/generate-token")
    public TokenResponseDto login(@Valid @RequestBody UserCredentialsDto userCredentialsDto) {
        return authService.generateToken(userCredentialsDto);
    }

    @PostMapping("/register-visitor")
    public VisitorResponseDto registerVisitor(@Valid @RequestBody CreateVisitorDto visitorDto) {
        return authService.registerVisitor(visitorDto);
    }

    @PostMapping("/register-organizer")
    public void registerOrganizer(@Valid @RequestBody CreateOrganizerDto dto) {
        authService.registerOrganizer(dto);
    }
}
