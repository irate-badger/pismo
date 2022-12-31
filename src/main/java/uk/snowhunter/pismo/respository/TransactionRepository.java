package uk.snowhunter.pismo.respository;

import org.springframework.data.repository.CrudRepository;
import uk.snowhunter.pismo.entities.TransactionEntity;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {

}