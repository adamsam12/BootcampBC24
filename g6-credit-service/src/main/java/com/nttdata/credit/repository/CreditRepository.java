package com.nttdata.credit.repository;


import com.nttdata.credit.entity.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CreditRepository extends ReactiveMongoRepository <Credit, String>{
    Flux<Credit> findByClientId(String clientId);
}
