package uk.snowhunter.pismo.respository;

import org.springframework.data.repository.CrudRepository;
import uk.snowhunter.pismo.entities.AccountEntity;

public interface AccountRepository extends CrudRepository<AccountEntity, Long> {

    AccountEntity findByAccountId(long accountNumber);
}