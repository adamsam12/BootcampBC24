package com.nttdata.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class G6ClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(G6ClientServiceApplication.class, args);
	}

}
