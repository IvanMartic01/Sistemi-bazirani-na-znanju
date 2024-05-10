package com.ftn.sbnz.app.core.user.admin.service;

import com.ftn.sbnz.app.core.user.admin.db.AdminEntity;

import java.util.Optional;

public interface AdminService {

    AdminEntity save(AdminEntity adminEntity);
    Optional<AdminEntity> findByEmail(String email);
}
