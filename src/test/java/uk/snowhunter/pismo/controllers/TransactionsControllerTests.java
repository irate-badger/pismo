package uk.snowhunter.pismo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import uk.snowhunter.pismo.domain.Transaction;
import uk.snowhunter.pismo.entities.AccountEntity;
import uk.snowhunter.pismo.entities.OperationEntity;
import uk.snowhunter.pismo.entities.TransactionEntity;
import uk.snowhunter.pismo.exceptions.InvalidRequestException;
import uk.snowhunter.pismo.respository.AccountRepository;
import uk.snowhunter.pismo.respository.OperationRepository;
import uk.snowhunter.pismo.respository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TransactionsControllerTests {

    @Mock TransactionRepository transactionRepository;
    @Mock AccountRepository accountRepository;
    @Mock OperationRepository operationRepository;

    @InjectMocks
    TransactionsController transactionsController;

    @Test
    void testTransactionController_success() {
        when(accountRepository.findById(any())).thenReturn(Optional.of(new AccountEntity()));
        when(operationRepository.findById(any())).thenReturn(Optional.of(new OperationEntity()
                                                                                .setOperationTypeId(1)
                                                                                .setDescription("PURCHASE")));

        when(transactionRepository.save(any())).thenReturn(new TransactionEntity());

        Transaction transaction = new Transaction()
                                            .setAccountId(1L)
                                            .setOperationTypeId(1)
                                            .setAmount(BigDecimal.valueOf(-10.10));
        Transaction response = transactionsController.createTransaction(transaction);
        assertNotNull(response);
    }

    @Test
    void testTransactionController_unpopulatedAccountNumberThrowsException() {
        Transaction transaction = new Transaction()
                                            .setOperationTypeId(1)
                                            .setAmount(BigDecimal.valueOf(10.10));
        assertThrows(InvalidRequestException.class,
                     () -> transactionsController.createTransaction(transaction),
                     "Expected controller to throw Invalid Request Exception, but it didn't");
    }

    @Test
    void testTransactionController_unpopulatedOperationIdThrowsException() {
        Transaction transaction = new Transaction()
                                            .setAccountId(1L)
                                            .setAmount(BigDecimal.valueOf(10.10));

        assertThrows(InvalidRequestException.class,
                     () -> transactionsController.createTransaction(transaction),
                     "Expected controller to throw Invalid Request Exception, but it didn't");
    }

    @Test
    void testTransactionController_unpopulatedAmountThrowsException() {
        Transaction transaction = new Transaction()
                                            .setAccountId(1L)
                                            .setOperationTypeId(1);

        assertThrows(InvalidRequestException.class,
                     () -> transactionsController.createTransaction(transaction),
                     "Expected controller to throw Invalid Request Exception, but it didn't");

    }

    @Test
    void testTransactionController_invalidAccountNumberThrowsException() {
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        Transaction transaction = new Transaction()
                                            .setAccountId(1L)
                                            .setOperationTypeId(1)
                                            .setAmount(BigDecimal.valueOf(10.10));
        assertThrows(InvalidRequestException.class,
                     () -> transactionsController.createTransaction(transaction),
                     "Expected controller to throw Invalid Request Exception, but it didn't");
    }

    @Test
    void testTransactionController_invalidOperationIdThrowsException() {
        when(accountRepository.findById(any())).thenReturn(Optional.of(new AccountEntity()));
        when(operationRepository.findById(any())).thenReturn(Optional.empty());

        Transaction transaction = new Transaction()
                                            .setAccountId(1L)
                                            .setOperationTypeId(1)
                                            .setAmount(BigDecimal.valueOf(10.10));
        assertThrows(InvalidRequestException.class,
                     () -> transactionsController.createTransaction(transaction),
                     "Expected controller to throw Invalid Request Exception, but it didn't");

    }

    @Test
    void testTransactionController_creditOperationForNegativeAmountThrowsException() {
        when(accountRepository.findById(any())).thenReturn(Optional.of(new AccountEntity()));
        when(operationRepository.findById(any())).thenReturn(Optional.of(new OperationEntity()
                                                                                .setOperationTypeId(4)
                                                                                .setDescription("PAYMENT")));

        Transaction transaction = new Transaction()
                                            .setAccountId(1L)
                                            .setOperationTypeId(1)
                                            .setAmount(BigDecimal.valueOf(-10.10));
        assertThrows(InvalidRequestException.class,
                     () -> transactionsController.createTransaction(transaction),
                     "Expected controller to throw Invalid Request Exception, but it didn't");
    }

    @Test
    void testTransactionController_debitOperationForPositiveAmountThrowsException() {
        when(accountRepository.findById(any())).thenReturn(Optional.of(new AccountEntity()));
        when(operationRepository.findById(any())).thenReturn(Optional.of(new OperationEntity()
                                                                                .setOperationTypeId(1)
                                                                                .setDescription("PURCHASE")));

        Transaction transaction = new Transaction()
                                            .setAccountId(1L)
                                            .setOperationTypeId(1)
                                            .setAmount(BigDecimal.valueOf(10.10));
        assertThrows(InvalidRequestException.class,
                     () -> transactionsController.createTransaction(transaction),
                     "Expected controller to throw Invalid Request Exception, but it didn't");
    }
}