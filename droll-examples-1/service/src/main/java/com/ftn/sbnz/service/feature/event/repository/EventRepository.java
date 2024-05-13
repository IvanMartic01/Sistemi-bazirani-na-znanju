package com.ftn.sbnz.service.feature.event.repository;


import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {

    Collection<EventEntity> findAllByVisitorsEmail(String email);
    Collection<EventEntity> findAllByOrganizerEmail(String email);
}
