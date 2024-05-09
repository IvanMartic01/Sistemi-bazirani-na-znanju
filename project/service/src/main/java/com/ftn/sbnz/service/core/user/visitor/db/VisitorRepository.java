package com.ftn.sbnz.service.core.user.visitor.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitorRepository extends JpaRepository<VisitorEntity, UUID> {

    Optional<VisitorEntity> findByEmail(String email);

}
