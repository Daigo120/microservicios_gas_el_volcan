package com.volcan.pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PedidosApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedidosApplication.class, args);
	}

	// Bean necesario para comunicarse con el microservicio de productos
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
