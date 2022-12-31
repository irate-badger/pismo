package uk.snowhunter.pismo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.snowhunter.pismo.domain.Transaction;
import uk.snowhunter.pismo.entities.OperationEntity;
import uk.snowhunter.pismo.entities.TransactionEntity;
import uk.snowhunter.pismo.exceptions.InvalidRequestException;
import uk.snowhunter.pismo.respository.AccountRepository;
import uk.snowhunter.pismo.respository.OperationRepository;
import uk.snowhunter.pismo.respository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Transactions", description = "Methods for interacting with transactions")
public class TransactionsController {

    final List<String> NEGATIVE_TRANSACTIONS = List.of("PURCHASE", "INSTALLMENT PURCHASE", "WITHDRAWAL");
    final List<String> POSITIVE_TRANSACTIONS = List.of("PAYMENT");
    final TransactionRepository transactionRepository;
    final AccountRepository accountRepository;
    final OperationRepository operationRepository;

    public TransactionsController(TransactionRepository transactionRepository, AccountRepository accountRepository, OperationRepository operationRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @Operation(summary = "Create a new transaction for the given account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the transaction",
        content = { @Content(mediaType = "application/json",
          schema = @Schema(implementation = Transaction.class)) }),
        @ApiResponse(responseCode = "400", description = "Insufficent or incorrect details provided",
        content = @Content) })
    @PostMapping("/transactions")
    public Transaction createTransaction(@RequestBody Transaction incomingEntity) throws InvalidRequestException {
        validateRequest(incomingEntity);
        Long accountId = incomingEntity.getAccountId();
        Integer operationTypeId = incomingEntity.getOperationTypeId();
        BigDecimal amount = incomingEntity.getAmount();

        final TransactionEntity transaction = new TransactionEntity()
                                            .setAcountId(accountId)
                                            .setOperationTypeId(operationTypeId)
                                            .setAmount(amount)
                                            .setEventDate(LocalDateTime.now());
        TransactionEntity savedTransaction = transactionRepository.save(transaction);

        return new Transaction().setTransactionId(savedTransaction.getTransactionId())
                                    .setAccountId(savedTransaction.getAccountId())
                                    .setAmount(savedTransaction.getAmount())
                                    .setOperationTypeId(savedTransaction.getOperationTypeId());
    }

    private void validateRequest(Transaction transaction) throws InvalidRequestException {
        mandatoryValuesArePopulated(transaction);
        validateAccountId(transaction);
        validateOperationType(transaction);
    }

    private void mandatoryValuesArePopulated(Transaction transaction) throws InvalidRequestException {
        if (transaction.getAccountId() == null || transaction.getOperationTypeId() == null || transaction.getAmount() == null) {
            throw new InvalidRequestException("Account id, operation type id and amount are mandatory");
        }
    }

    private void validateAccountId(Transaction transaction) throws InvalidRequestException {
        if (accountRepository.findById(transaction.getAccountId()).isEmpty()) {
            throw new InvalidRequestException("Account number is invalid");
        }
    }

    private void validateOperationType(Transaction transaction) throws InvalidRequestException {
        Optional<OperationEntity> operationEntity = operationRepository.findById(transaction.getOperationTypeId());
        if (operationEntity.isEmpty()) {
            throw new InvalidRequestException("Operation type id is invalid");
        }
        String description = operationEntity.get().getDescription();

        // Skipping check if amount is zero, as this is undefined
        boolean isPositiveTransaction = transaction.getAmount().compareTo(BigDecimal.ZERO) > 0;
        boolean isNegativeTransaction = transaction.getAmount().compareTo(BigDecimal.ZERO) < 0;

        if ((isNegativeTransaction &&
             POSITIVE_TRANSACTIONS.stream().anyMatch(description::contains)) ||
            (isPositiveTransaction &&
             NEGATIVE_TRANSACTIONS.stream().anyMatch(description::contains))
        ) {
            throw new InvalidRequestException("Operation type does not match value");
        }
    }

}