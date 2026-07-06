package com.alertabarrio.ingsoft;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class IngsoftApplication {

	@PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Bogota"));
    }

	public static void main(String[] args) {
		SpringApplication.run(IngsoftApplication.class, args);
	}

}
