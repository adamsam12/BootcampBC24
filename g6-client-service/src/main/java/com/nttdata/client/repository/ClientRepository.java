package com.nttdata.client.repository;


import com.nttdata.client.entity.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveMongoRepository<Client, String>{

	Mono<Client> findByDni(String dni);
	Flux<Client> findByClientType(Integer clientType);
	
}
