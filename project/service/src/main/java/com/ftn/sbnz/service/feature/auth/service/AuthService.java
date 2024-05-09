package com.ftn.sbnz.service.feature.auth.service;


import com.ftn.sbnz.service.core.user.abstract_user.db.model.UserEntity;
import com.ftn.sbnz.service.core.user.organizer.OrganizerResponseDto;
import com.ftn.sbnz.service.core.user.organizer.db.OrganizerEntity;
import com.ftn.sbnz.service.core.user.visitor.VisitorResponseDto;
import com.ftn.sbnz.service.core.user.visitor.db.VisitorEntity;
import com.ftn.sbnz.service.feature.auth.dto.login.TokenResponseDto;
import com.ftn.sbnz.service.feature.auth.dto.login.UserCredentialsDto;
import com.ftn.sbnz.service.feature.auth.dto.user.request.CreateOrganizerDto;
import com.ftn.sbnz.service.feature.auth.dto.user.request.CreateVisitorDto;
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
