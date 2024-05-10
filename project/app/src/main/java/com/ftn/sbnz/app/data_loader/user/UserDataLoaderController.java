package com.ftn.sbnz.app.data_loader.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/load-user-data")
public class UserDataLoaderController {

    private final UserDataLoader userDataLoader;

    @PostMapping
    public void loadData() {
        userDataLoader.load();
    }
}
