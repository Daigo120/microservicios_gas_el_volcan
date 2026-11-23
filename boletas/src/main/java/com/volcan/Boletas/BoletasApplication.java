package com.volcan.Boletas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BoletasApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoletasApplication.class, args);
	}

	// Bean necesario para comunicarse con otros microservicios (como pedidos-service)
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
