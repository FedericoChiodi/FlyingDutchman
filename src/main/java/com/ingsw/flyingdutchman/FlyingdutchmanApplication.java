package com.ingsw.flyingdutchman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ingsw.flyingdutchman")
@EnableJpaRepositories(basePackages = "com.ingsw.flyingdutchman.repository")
public class FlyingdutchmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlyingdutchmanApplication.class, args);
	}

}
