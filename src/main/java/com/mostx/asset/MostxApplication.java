package com.mostx.asset;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "https://japi.mostx.co.kr")})
@EnableScheduling
@SpringBootApplication
public class MostxApplication {

	public static void main(String[] args) {
		SpringApplication.run(MostxApplication.class, args);
	}

}
