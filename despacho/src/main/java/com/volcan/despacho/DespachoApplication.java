package com.volcan.despacho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DespachoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DespachoApplication.class, args);
	}

	// Bean necesario para comunicarse con otros microservicios (como pedidos-service)
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}