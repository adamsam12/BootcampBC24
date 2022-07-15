package nttdata.grupo06.sistemaBanco.service.Impl;

import lombok.extern.slf4j.Slf4j;
import nttdata.grupo06.sistemaBanco.entity.BankAccount;
import nttdata.grupo06.sistemaBanco.entity.Signer;
import nttdata.grupo06.sistemaBanco.model.Credit;
import nttdata.grupo06.sistemaBanco.repository.BankRepository;
import nttdata.grupo06.sistemaBanco.service.ApiBank;
import nttdata.grupo06.sistemaBanco.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BankServiceImpl implements BankService {

    @Autowired
    BankRepository bankRepository;

    @Autowired
    ApiBank apiBank;

    @Override
    public Flux<BankAccount> findByClientId(String clientId) {
        return bankRepository.findByClientId(clientId);
    }

    @Override
    public Mono<BankAccount> createBankAccount(BankAccount bankAccount) {
        log.info("Start of operation to create a credit");
        return apiBank.getClientByIdMicroservice(bankAccount.getClientId())
                .flatMap(client -> {

                    if (client.getClientType() == 0) {

                        Flux<BankAccount> accounts = findByClientId(client.getClientId());
                        return accounts
                                .filter(a -> a.getAccountType().equals(bankAccount.getAccountType())).count().flatMap(
                                        numberAccounts -> {
                                            log.info("El numero de cuentas de tipo '" + bankAccount.getAccountType() + "' es: " + numberAccounts);
                                            if (numberAccounts == 0L) {
                                                if (client.getClientSubType().equals("V")) {
                                                    Flux<Credit> credits = apiBank.getCreditByClientIdMicroservice(client.getClientId());
                                                    return saveIfHasACreditCard(credits, bankAccount);
                                                } else return bankRepository.save(bankAccount);
                                            } else return Mono.empty();
                                        });

                    } else if (client.getClientType() == 1) {
                        if (bankAccount.getAccountType().equals("A") || bankAccount.getAccountType().equals("P"))
                            return Mono.empty();
                        else {
                            if (client.getClientSubType().equals("P") && bankAccount.getCommission() == 0F) {
                                Flux<Credit> credits = apiBank.getCreditByClientIdMicroservice(client.getClientId());
                                return saveIfHasACreditCard(credits, bankAccount);
                            } else return bankRepository.save(bankAccount);
                        }
                    } else return Mono.empty();

                });
    }

    @Override
    public Mono<BankAccount> deleteBankAccount(String id) {
        return getBankAccount(id).flatMap(b -> bankRepository.deleteById(b.getId()).thenReturn(b));
    }

    @Override
    public Mono<BankAccount> getBankAccount(String id) {
        return bankRepository.findById(id).flatMap(eBankAccount -> {
            if (eBankAccount.getAccountType().equals("C") && eBankAccount.getSigners() != null) {
                Flux<Signer> signerFlux = eBankAccount.getSigners().filter(eSigner -> !eSigner.getStatus().equals("RETIRED"));
                eBankAccount.setSigners(signerFlux);
                return Mono.just(eBankAccount);
            } else
                return Mono.just(eBankAccount);
        });
    }

    @Override
    public Mono<BankAccount> updateBankAccount(BankAccount bankAccount) {
        return getBankAccount(bankAccount.getId())
                .flatMap(existingBankAccount -> {
                    existingBankAccount.setBalance(bankAccount.getBalance());
                    return bankRepository.save(existingBankAccount);
                });
    }

    @Override
    public Mono<BankAccount> addSigner(String id, Signer signer) {
        return getBankAccount(id).flatMap(eBankAccount -> {
            if (eBankAccount.getAccountType().equals("C")) {
                if (eBankAccount.getSigners() != null) {
                    return eBankAccount.getSigners().filter(eSigner -> eSigner.getDni().equals(signer.getDni()))
                            .count().flatMap(
                                    numberOfCoincidence -> {
                                        if (numberOfCoincidence == 0L) {
                                            Flux<Signer> lts = eBankAccount.getSigners().concatWith(Flux.just(signer));
                                            eBankAccount.setSigners(lts);
                                            return bankRepository.save(eBankAccount);
                                        } else
                                            return Mono.empty();
                                    }
                            );
                } else {
                    eBankAccount.setSigners(Flux.just(signer));
                    return bankRepository.save(eBankAccount);
                }
            } else
                return Mono.empty();
        });
    }

    @Override
    public Mono<BankAccount> updateSigner(String id, Signer signer) {
        return getBankAccount(id).flatMap(eBankAccount -> {
            if (eBankAccount.getAccountType().equals("C") && eBankAccount.getSigners() != null) {
                return eBankAccount.getSigners().filter(eSigner -> eSigner.getDni().equals(signer.getDni()))
                        .elementAt(0).flatMap(esigner -> {
                            esigner.setSignerType(signer.getSignerType());
                            esigner.setName(signer.getName());
                            esigner.setFirstName(signer.getFirstName());
                            esigner.setLastName(signer.getLastName());
                            return bankRepository.save(eBankAccount);
                        });
            } else {
                return Mono.empty();
            }
        });
    }

    @Override
    public Mono<BankAccount> deleteSigner(String id, String dni) {
        return getBankAccount(id).flatMap(eBankAccount -> {
            if (eBankAccount.getAccountType().equals("C") && eBankAccount.getSigners() != null) {
                return eBankAccount.getSigners().filter(eSigner -> eSigner.getDni().equals(dni))
                        .elementAt(0).flatMap(es -> {
                            es.setStatus("RETIRED");
                            return bankRepository.save(eBankAccount);
                        });
            } else
                return Mono.empty();
        });
    }

    private Mono<BankAccount> saveIfHasACreditCard(Flux<Credit> credits, BankAccount bankAccount) {
        return credits.filter(c -> c.getId().equals("-1")).count().flatMap(
                invalidCredit -> {
                    if (invalidCredit > 0) return Mono.empty();
                    else
                        return credits.filter(c -> c.getCreditType().equals("T")).count().flatMap(
                                numberOfCreditCards -> {
                                    if (numberOfCreditCards > 0) return bankRepository.save(bankAccount);
                                    else return Mono.empty();
                                }
                        );
                }
        );
    }
}
