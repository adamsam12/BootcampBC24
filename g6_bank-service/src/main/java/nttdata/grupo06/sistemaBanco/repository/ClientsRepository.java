package nttdata.grupo06.sistemaBanco.repository;

import nttdata.grupo06.sistemaBanco.entity.BankAccount;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ClientsRepository extends ReactiveCrudRepository<BankAccount, String> {
}
