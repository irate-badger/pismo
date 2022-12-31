package uk.snowhunter.pismo.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.snowhunter.pismo.domain.Transaction;
import uk.snowhunter.pismo.entities.AccountEntity;
import uk.snowhunter.pismo.respository.AccountRepository;
import uk.snowhunter.pismo.respository.TransactionRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsControllerIT {
    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void testTransactionsController_emptyBodyReturns400() {
        Transaction transaction = new Transaction();
        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity("http://localhost:" + port + "/transactions", transaction, JsonNode.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JsonNode responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Account id, operation type id and amount are mandatory", responseBody.get("message").textValue());
    }

    @Test
    void testTransactionController_createTransactionSuccessful() {
        AccountEntity account = accountRepository.save(new AccountEntity().setDocumentNumber("12345"));

        Transaction transaction = new Transaction().setAccountId(account.getAccountId())
                                                    .setOperationTypeId(4)
                                                    .setAmount(BigDecimal.valueOf(100.01));

        ResponseEntity<Transaction> response = this.restTemplate.postForEntity("http://localhost:" + port + "/transactions", transaction, Transaction.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Transaction responseBody = response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.getTransactionId());

        transactionRepository.deleteById(responseBody.getTransactionId());
        accountRepository.deleteById(account.getAccountId());
    }

    @Test
    void testTransactionController_invalidAccountIdReturns400() {
        Transaction transaction = new Transaction().setAccountId(-1L)
                                                    .setOperationTypeId(99)
                                                    .setAmount(BigDecimal.valueOf(100.01));

        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity("http://localhost:" + port + "/transactions", transaction, JsonNode.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonNode responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Account number is invalid", responseBody.get("message").textValue());
    }

    @Test
    void testTransactionController_invalidOperationTypeReturns400() {
        AccountEntity account = accountRepository.save(new AccountEntity().setDocumentNumber("12345"));

        Transaction transaction = new Transaction().setAccountId(account.getAccountId())
                                                    .setOperationTypeId(99)
                                                    .setAmount(BigDecimal.valueOf(100.01));

        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity("http://localhost:" + port + "/transactions", transaction, JsonNode.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonNode responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Operation type id is invalid", responseBody.get("message").textValue());

        accountRepository.deleteById(account.getAccountId());
    }

    @Test
    void testTransactionController_invalidOperationReturns400() {
        AccountEntity account = accountRepository.save(new AccountEntity().setDocumentNumber("12345"));

        Transaction transaction = new Transaction().setAccountId(account.getAccountId())
                                                    .setOperationTypeId(1)
                                                    .setAmount(BigDecimal.valueOf(100.01));

        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity("http://localhost:" + port + "/transactions", transaction, JsonNode.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        JsonNode responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Operation type does not match value", responseBody.get("message").textValue());

        accountRepository.deleteById(account.getAccountId());
    }

}