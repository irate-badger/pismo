package uk.snowhunter.pismo.respository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import uk.snowhunter.pismo.entities.Account;

public interface AccountsRepository extends CrudRepository<Account, Long> {

    List<Account> findByDocumentNumber(String documentNumber);

    Account findByAccountNumber(long accountNumber);
}