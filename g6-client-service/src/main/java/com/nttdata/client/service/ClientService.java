package com.nttdata.client.service;

import com.nttdata.client.entity.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {

	Mono<Client> findByDni(String dni);
	Flux<Client> findByClientType(Integer clientType);
	Flux<Client> findAll();
	
	Mono<Client> createClient(Client client);
    Mono<Client> updateClient(Client client);
    Mono<Client> deleteClient(String id);
    Mono<Client> getClient(String id);
    
}
