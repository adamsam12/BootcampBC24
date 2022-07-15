package nttdata.grupo06.sistemaBanco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SistemaBancoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaBancoApplication.class, args);
	}

}
