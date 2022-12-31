package uk.snowhunter.pismo.respository;

import org.springframework.data.repository.CrudRepository;
import uk.snowhunter.pismo.entities.OperationEntity;

public interface OperationRepository extends CrudRepository<OperationEntity, Integer> {

}