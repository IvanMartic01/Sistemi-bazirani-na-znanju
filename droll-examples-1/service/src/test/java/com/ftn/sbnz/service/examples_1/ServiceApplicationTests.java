package com.ftn.sbnz.service.examples_1;

import com.ftn.sbnz.service.examples_1.Examples_1;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceApplicationTests {

	@Test
	void helloWorldTest() throws InstantiationException, IllegalAccessException {
		Examples_1.main();
	}

}
