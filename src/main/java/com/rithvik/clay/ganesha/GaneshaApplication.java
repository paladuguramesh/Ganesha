package com.rithvik.clay.ganesha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class GaneshaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaneshaApplication.class, args);
	}

}
