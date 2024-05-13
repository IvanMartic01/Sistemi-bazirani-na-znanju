package com.ftn.sbnz.service.core.user.admin.service;


import com.ftn.sbnz.model.core.AdminEntity;

import java.util.Optional;

public interface AdminService {

    AdminEntity save(AdminEntity adminEntity);
    Optional<AdminEntity> findByEmail(String email);
}
