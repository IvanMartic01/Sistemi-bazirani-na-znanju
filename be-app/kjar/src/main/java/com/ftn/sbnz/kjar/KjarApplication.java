package com.ftn.sbnz.kjar;


import com.ftn.sbnz.model.test.TestModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ftn.sbnz"})
public class KjarApplication {

    public static void main(String[] args) {
        SpringApplication.run(KjarApplication.class, args);
    }

}

