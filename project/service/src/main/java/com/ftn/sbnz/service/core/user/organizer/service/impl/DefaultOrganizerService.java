package com.ftn.sbnz.service.core.user.organizer.service.impl;

import com.ftn.sbnz.service.core.user.organizer.db.OrganizerEntity;
import com.ftn.sbnz.service.core.user.organizer.db.OrganizerRepository;
import com.ftn.sbnz.service.core.user.organizer.service.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DefaultOrganizerService implements OrganizerService {

    private final OrganizerRepository organizerRepository;

    @Override
    public OrganizerEntity save(OrganizerEntity organizerEntity) {
        return organizerRepository.save(organizerEntity);
    }

    @Override
    public Optional<OrganizerEntity> findByEmail(String email) {
        return organizerRepository.findByEmail(email);
    }
}
