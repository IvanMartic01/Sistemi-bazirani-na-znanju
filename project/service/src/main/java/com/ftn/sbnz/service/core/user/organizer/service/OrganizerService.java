package com.ftn.sbnz.service.core.user.organizer.service;

import com.ftn.sbnz.service.core.user.organizer.db.OrganizerEntity;

import java.util.Optional;

public interface OrganizerService {

    OrganizerEntity save(OrganizerEntity organizerEntity);
    Optional<OrganizerEntity> findByEmail(String email);
}
