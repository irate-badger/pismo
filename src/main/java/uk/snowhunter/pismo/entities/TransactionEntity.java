package uk.snowhunter.pismo.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACTIONS")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long transactionId;

    Long accountId;

    Integer operationTypeId;

    BigDecimal amount;

    LocalDateTime eventDate;

    public Long getTransactionId() {
        return transactionId;
    }

    public TransactionEntity setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public Long getAccountId() {
        return accountId;
    }

    public TransactionEntity setAcountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public Integer getOperationTypeId() {
        return operationTypeId;
    }

    public TransactionEntity setOperationTypeId(Integer operationTypeId) {
        this.operationTypeId = operationTypeId;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionEntity setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public TransactionEntity setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
        return this;
    }
}