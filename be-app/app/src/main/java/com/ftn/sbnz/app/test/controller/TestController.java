package com.ftn.sbnz.app.test.controller;

import com.ftn.sbnz.app.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TestController {

    private final TestService testService;

    @GetMapping("/test")
    public void test() {
        testService.fireAllRules();
    }
}
