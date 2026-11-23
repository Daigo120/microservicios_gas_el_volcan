package com.volcan.pagos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PagosApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagosApplication.class, args);
	}

	// Bean necesario para comunicarse con el microservicio de pedidos
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}