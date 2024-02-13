package com.mostx.asset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MostxApplication {

	public static void main(String[] args) {
		SpringApplication.run(MostxApplication.class, args);
	}

}
