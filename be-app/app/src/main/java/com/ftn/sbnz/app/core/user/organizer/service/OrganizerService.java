package com.ftn.sbnz.app.core.user.organizer.service;

import com.ftn.sbnz.app.core.user.organizer.db.OrganizerEntity;

import java.util.Optional;

public interface OrganizerService {

    OrganizerEntity save(OrganizerEntity organizerEntity);
    Optional<OrganizerEntity> findByEmail(String email);
}
