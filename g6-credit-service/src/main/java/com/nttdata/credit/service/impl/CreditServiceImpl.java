package com.nttdata.credit.service.impl;


import com.nttdata.credit.entity.Credit;
import com.nttdata.credit.repository.CreditRepository;
import com.nttdata.credit.service.ApiCredit;
import com.nttdata.credit.service.CreditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private ApiCredit apiCredit;

    @Override
    public Mono<Credit> getCreditByCreditId(String creditId) {

        return creditRepository.findById(creditId);
    }

    @Override
    public Mono<Credit> createCredit(Credit credit) {
        return apiCredit.getClientByIdMicroservice(credit.getClientId())
                .flatMap(client -> {
                    if (client.getClientType() != -1 && credit.getCreditType().equals("T"))
                        return creditRepository.save(credit);

                    log.info("El cliente es de tipo: " + client.getClientType());
                    log.info("El tipo de credito solicitado es: " + credit.getCreditType());
                    if (client.getClientType() == 0 && credit.getCreditType().equals("P")) {
                        System.out.println("Entre");
                        Flux<Credit> credits = findCreditByClientId(client.getId());
                        return credits.filter(c -> c.getStatus().equals("ACTIVE"))
                                .count().flatMap(
                                        numberActiveCredits -> {
                                            log.info("El numero de creditos activos del cliente personal es: " + numberActiveCredits);
                                            if (numberActiveCredits == 0L) return creditRepository.save(credit);
                                            else return Mono.empty();
                                        });
                    } else if (client.getClientType() == 1 && credit.getCreditType().equals("E")) {
                        return creditRepository.save(credit);
                    } else {
                        return Mono.empty();
                    }
                });
    }

    @Override
    public Mono<Credit> updateCredit(Credit credit) {
        return getCreditByCreditId(credit.getId())
                .flatMap(existingCredit -> {
                    existingCredit.setCreditType(credit.getCreditType());
                    existingCredit.setStatus(credit.getStatus());
                    existingCredit.setInitialAmount(credit.getInitialAmount());
                    existingCredit.setCurrentAmount(credit.getCurrentAmount());
                    existingCredit.setInterest(credit.getInterest());
                    existingCredit.setPaymentDay(credit.getPaymentDay());
                    return creditRepository.save(existingCredit);
                });
    }

    @Override
    public Mono<Credit> deleteCredit(String id) {

        return getCreditByCreditId(id)
                .flatMap(c -> creditRepository.deleteById(c.getId()).thenReturn(c));
    }

    @Override
    public Flux<Credit> findAll() {

        return creditRepository.findAll();
    }

    @Override
    public Flux<Credit> findCreditByClientId(String clientId) {
        return creditRepository.findByClientId(clientId);
    }

    @Override
    public Mono<Credit> makeDeposit(String creditId, Float deposit) {
        return getCreditByCreditId(creditId).flatMap(cCredit -> {
            if (cCredit.getCurrentAmount() > 0) {
                if (cCredit.getCurrentAmount() - deposit < 0) {
                    cCredit.setCurrentAmount(0.0F);
                    cCredit.setStatus("CANCELLED");
                } else {
                    cCredit.setCurrentAmount(cCredit.getCurrentAmount() - deposit);
                }
                return creditRepository.save(cCredit);
            } else return Mono.empty();
        });
    }
}
