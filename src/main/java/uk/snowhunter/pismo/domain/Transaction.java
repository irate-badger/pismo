package uk.snowhunter.pismo.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class Transaction {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "transaction_id")
    Long transactionId;

    @Schema(name = "account_id", defaultValue = "1")
    Long accountId;

    @Schema(name = "operation_type_id", allowableValues = {"1", "2", "3", "4"}, defaultValue = "4")
    Integer operationTypeId;

    @Schema(name = "amount", defaultValue = "100")
    BigDecimal amount;

    public Long getTransactionId() {
        return transactionId;
    }

    public Transaction setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Transaction setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public Integer getOperationTypeId() {
        return operationTypeId;
    }

    public Transaction setOperationTypeId(Integer operationTypeId) {
        this.operationTypeId = operationTypeId;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Transaction setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }
}