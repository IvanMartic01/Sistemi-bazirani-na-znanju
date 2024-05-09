package com.ftn.sbnz.service.core.user.organizer.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizerRepository extends JpaRepository<OrganizerEntity, UUID> {

    Optional<OrganizerEntity> findByEmail(String email);
}
