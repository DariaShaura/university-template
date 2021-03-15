package com.epam.rd.izh;

import com.epam.rd.izh.controller.AuthenticationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ComponentScan(basePackages = "com.epam.rd.izh")
public class RdTestWebApplicationTests {

	@Autowired
	AuthenticationController authenticationController;

	@Test
	public void contextLoads() {
		assertThat(authenticationController).isNotNull();
	}

}
