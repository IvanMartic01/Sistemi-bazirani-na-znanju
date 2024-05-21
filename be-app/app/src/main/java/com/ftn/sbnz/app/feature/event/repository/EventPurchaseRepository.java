package com.ftn.sbnz.app.feature.event.repository;

import com.ftn.sbnz.model.event.EventPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventPurchaseRepository extends JpaRepository<EventPurchaseEntity, UUID> {

    Optional<EventPurchaseEntity> findByEventIdAndVisitorId(UUID eventId, UUID visitorId);

}
