package com.ftn.sbnz.service.core.user.admin.service.impl;

import com.ftn.sbnz.app.core.user.admin.db.AdminRepository;
import com.ftn.sbnz.app.core.user.admin.service.AdminService;
import com.ftn.sbnz.model.core.AdminEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DefaultAdminService implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public AdminEntity save(AdminEntity adminEntity) {
        return adminRepository.save(adminEntity);
    }

    @Override
    public Optional<AdminEntity> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
