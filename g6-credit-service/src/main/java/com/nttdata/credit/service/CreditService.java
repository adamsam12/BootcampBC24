package com.nttdata.credit.service;


import com.nttdata.credit.entity.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {

    Mono<Credit> getCreditByCreditId(String creditId);
    Mono<Credit> createCredit(Credit credit);
    Mono<Credit> updateCredit(Credit credit);
    Mono<Credit> deleteCredit(String creditId);

    Flux<Credit> findAll();
    Flux<Credit> findCreditByClientId(String clientId);

    Mono<Credit> makeDeposit(String creditId, Float deposit);
}
