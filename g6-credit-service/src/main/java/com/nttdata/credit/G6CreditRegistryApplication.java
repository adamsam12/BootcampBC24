package com.nttdata.credit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class G6CreditRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(G6CreditRegistryApplication.class, args);
	}

}
