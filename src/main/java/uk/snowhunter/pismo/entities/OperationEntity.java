package uk.snowhunter.pismo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "OPERATIONS")
public class OperationEntity {

    @Id
    private Integer operationTypeId;

    private String description;

    public OperationEntity setOperationTypeId(Integer operationTypeId) {
        this.operationTypeId = operationTypeId;
        return this;
    }

    public int getOperationTypeId() {
        return operationTypeId;
    }

    public OperationEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }
}