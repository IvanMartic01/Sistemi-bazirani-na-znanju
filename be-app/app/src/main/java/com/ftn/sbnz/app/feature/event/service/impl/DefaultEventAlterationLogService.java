package com.ftn.sbnz.app.feature.event.service.impl;

import com.ftn.sbnz.app.feature.event.repository.EventAlterationLogRepository;
import com.ftn.sbnz.app.feature.event.service.EventAlterationLogService;
import com.ftn.sbnz.model.event.EventAlterationLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultEventAlterationLogService implements EventAlterationLogService {

    private final EventAlterationLogRepository eventAlterationLogRepository;

    @Override
    public EventAlterationLogEntity saveAndGetEntity(EventAlterationLogEntity entity) {
        return eventAlterationLogRepository.save(entity);
    }

}
