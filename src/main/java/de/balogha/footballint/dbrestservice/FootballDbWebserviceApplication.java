package de.balogha.footballint.dbrestservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FootballDbWebserviceApplication {

	private static final Logger log = LoggerFactory.getLogger(FootballDbWebserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FootballDbWebserviceApplication.class, args);
	}
}
