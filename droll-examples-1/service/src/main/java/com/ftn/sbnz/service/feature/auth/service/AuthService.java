package com.ftn.sbnz.service.feature.auth.service;


import com.ftn.sbnz.app.core.user.organizer.OrganizerResponseDto;
import com.ftn.sbnz.app.core.user.visitor.VisitorResponseDto;
import com.ftn.sbnz.app.feature.auth.dto.login.TokenResponseDto;
import com.ftn.sbnz.app.feature.auth.dto.login.UserCredentialsDto;
import com.ftn.sbnz.app.feature.auth.dto.user.request.CreateOrganizerDto;
import com.ftn.sbnz.app.feature.auth.dto.user.request.CreateVisitorDto;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.core.UserEntity;
import com.ftn.sbnz.model.core.VisitorEntity;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {
    TokenResponseDto generateToken(UserCredentialsDto loginRequest);

    @Transactional
    VisitorResponseDto registerVisitor(CreateVisitorDto requestDto);

    @Transactional
    OrganizerResponseDto registerOrganizer(CreateOrganizerDto requestDto);

    UserEntity getUserForCurrentSession();

    VisitorEntity getVisitorForCurrentSession();

    OrganizerEntity getOrganizerForCurrentSession();
}
