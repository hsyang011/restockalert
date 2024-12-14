package me.yangsongi.restockalert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RestockalertApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestockalertApplication.class, args);
	}

}
