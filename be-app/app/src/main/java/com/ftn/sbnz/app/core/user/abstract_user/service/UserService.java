package com.ftn.sbnz.app.core.user.abstract_user.service;



import com.ftn.sbnz.model.core.UserEntity;

import java.util.Optional;

public interface UserService {

    UserEntity save(UserEntity user);
    Optional<UserEntity> findByEmail(String email);
}
