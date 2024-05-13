package com.ftn.sbnz.app;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = {"com.ftn.sbnz"})
@EnableJpaRepositories(basePackages = {"com.ftn.sbnz"})
@EntityScan(basePackages = {"com.ftn.sbnz"})
public class AppApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Override
	public void run(String... args)  {
//		userDataLoader.load();
//		eventDataLoader.load();
	}
}
