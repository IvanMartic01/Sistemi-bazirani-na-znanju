package com.ftn.sbnz.app;

import com.ftn.sbnz.app.data_loader.event.EventDataLoader;
import com.ftn.sbnz.app.data_loader.user.UserDataLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class AppApplication implements CommandLineRunner {

	private final UserDataLoader userDataLoader;
	private final EventDataLoader eventDataLoader;

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Override
	public void run(String... args)  {
//		userDataLoader.load();
//		eventDataLoader.load();
	}
}
