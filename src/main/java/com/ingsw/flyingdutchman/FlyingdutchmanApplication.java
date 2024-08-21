package com.ingsw.flyingdutchman;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class FlyingdutchmanApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(FlyingdutchmanApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(@NotNull SpringApplicationBuilder application) {
		return application.sources(FlyingdutchmanApplication.class);
	}

}
