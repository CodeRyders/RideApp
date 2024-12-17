package de.coderyders.rideapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@CrossOrigin(origins = "http://localhost:4200")
public class RideAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideAppApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
