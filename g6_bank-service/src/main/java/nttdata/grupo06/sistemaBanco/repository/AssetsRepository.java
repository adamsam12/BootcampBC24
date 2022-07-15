package nttdata.grupo06.sistemaBanco.repository;

import nttdata.grupo06.sistemaBanco.entity.BankAccount;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AssetsRepository extends ReactiveCrudRepository<BankAccount, String> {
}
