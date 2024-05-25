package com.ftn.sbnz.app.core.user.visitor.service;


import com.ftn.sbnz.model.core.visitor.VisitorEntity;

import java.util.Collection;
import java.util.Optional;

public interface VisitorService {

    VisitorEntity save(VisitorEntity visitorEntity);
    Optional<VisitorEntity> findByEmail(String email);
    Collection<VisitorEntity> getAll();
}
