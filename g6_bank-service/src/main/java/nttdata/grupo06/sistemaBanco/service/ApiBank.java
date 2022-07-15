package nttdata.grupo06.sistemaBanco.service;

import nttdata.grupo06.sistemaBanco.model.Clients;
import nttdata.grupo06.sistemaBanco.model.Credit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ApiBank {

    private static final String URL_CLIENT = "http://customer-service:8080/";
    private static final String URL_CREDIT = "http://credit-service:8082/";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;


    public Mono<Clients> getClientByIdMicroservice(String clientId){
        CircuitBreaker cb = circuitBreakerFactory.create("clientServiceBreaker");
        return Mono.just(cb.run( () ->
                        restTemplate.getForObject(URL_CLIENT + "client/" + clientId, Clients.class)
                , throwable -> fallbackGetClientById()
        ));
    }
    public Clients fallbackGetClientById( ){
        Clients c = new Clients();
        c.setClientType(-1);
        return c;
    }

    public Flux<Credit> getCreditByClientIdMicroservice(String clientId){
        CircuitBreaker cb = circuitBreakerFactory.create("clientServiceBreaker");
        return Flux.fromArray(cb.run( () ->
                        restTemplate.getForEntity(URL_CREDIT + "/credit/client/" + clientId, Credit[].class).getBody()
                , throwable -> fallbackGetCredit()));
    }
    public Credit[] fallbackGetCredit(){
        Credit[] t = new Credit[1];
        t[0] = new Credit();
        t[0].setId("-1");
        return t;
    }
}
