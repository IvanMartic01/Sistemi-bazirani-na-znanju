package com.ftn.sbnz.app.data_loader.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/event/load-data")
public class EventDataLoaderController {

    private final EventDataLoader eventDataLoader;

    @PostMapping
    public void loadData() {
        eventDataLoader.load();
    }
}
