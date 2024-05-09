package com.ftn.sbnz.service.core.user.visitor.service.impl;

import com.ftn.sbnz.service.core.user.visitor.db.VisitorEntity;
import com.ftn.sbnz.service.core.user.visitor.db.VisitorRepository;
import com.ftn.sbnz.service.core.user.visitor.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DefaultVisitorService implements VisitorService {

    private final VisitorRepository visitorRepository;

    @Override
    public VisitorEntity save(VisitorEntity visitorEntity) {
        return visitorRepository.save(visitorEntity);
    }

    @Override
    public Optional<VisitorEntity> findByEmail(String email) {
        return visitorRepository.findByEmail(email);
    }
}
