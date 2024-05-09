package com.ftn.sbnz.service.core.user.abstract_user.service;


import com.ftn.sbnz.service.core.user.abstract_user.db.model.UserEntity;

import java.util.Optional;

public interface UserService {

    UserEntity save(UserEntity user);
    Optional<UserEntity> findByEmail(String email);
}
