package com.ftn.sbnz.app.feature.event.service;

import com.ftn.sbnz.model.event.EventAlterationLogEntity;

public interface EventAlterationLogService {

    EventAlterationLogEntity saveAndGetEntity(EventAlterationLogEntity entity);

}
