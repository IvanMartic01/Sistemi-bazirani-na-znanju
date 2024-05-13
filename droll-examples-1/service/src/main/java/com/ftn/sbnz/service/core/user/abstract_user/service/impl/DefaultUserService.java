package com.ftn.sbnz.service.core.user.abstract_user.service.impl;

import com.ftn.sbnz.app.core.user.abstract_user.db.UserRepository;
import com.ftn.sbnz.app.core.user.abstract_user.service.UserService;
import com.ftn.sbnz.model.core.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
