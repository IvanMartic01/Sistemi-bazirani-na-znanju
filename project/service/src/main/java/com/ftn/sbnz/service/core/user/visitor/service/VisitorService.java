package com.ftn.sbnz.service.core.user.visitor.service;

import com.ftn.sbnz.service.core.user.visitor.db.VisitorEntity;

import java.util.Optional;

public interface VisitorService {

    VisitorEntity save(VisitorEntity visitorEntity);
    Optional<VisitorEntity> findByEmail(String email);
}
