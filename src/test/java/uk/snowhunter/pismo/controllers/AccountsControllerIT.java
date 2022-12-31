package uk.snowhunter.pismo.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.snowhunter.pismo.domain.Account;
import uk.snowhunter.pismo.respository.AccountRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsControllerIT {

    @Value(value="${local.server.port}")
	private int port;

    @Autowired
	private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountsRepository;

    @Test
	public void testAccountControllerIT_missingAccountIdReturns404() {
        ResponseEntity<JsonNode> response = this.restTemplate.getForEntity("http://localhost:" + port + "/accounts/99",
                                                                           JsonNode.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        JsonNode responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Account not found", responseBody.get("message").textValue());
    }

    @Test
	public void testAccountControllerIT_invalidAccountReturns400() {
        ResponseEntity<JsonNode> response = this.restTemplate.getForEntity("http://localhost:" + port + "/accounts/AA",
                                                                           JsonNode.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JsonNode responseBody = response.getBody();
        assertNotNull(responseBody);
        JsonNode message = responseBody.get("message");
        assertEquals("Invalid parameter provided", message.textValue());
    }

    // Post without a body will fail as a bad request
    @Test
    void testAccountControllerIT_postNoBodyReturns400() {
        Account accountDo = new Account();
        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity("http://localhost:" + port + "/accounts", accountDo, JsonNode.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JsonNode responseBody = response.getBody();
        assertNotNull(responseBody);
        JsonNode message = responseBody.get("message");
        assertEquals("No document number found", message.textValue());
    }

    // Post of an account will create a new account
    @Test
    void testAccountControllerIT_postCreatesANewAccount() {
        String documentNumber = "11223344";
        Account accountDo = new Account().setDocumentNumber(documentNumber);
        ResponseEntity<Account> response = this.restTemplate.postForEntity("http://localhost:" + port + "/accounts", accountDo, Account.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Account account = response.getBody();
        assertNotNull(account);
        assertNotNull(account.getAccountId());
        assertEquals(documentNumber, account.getDocumentNumber());

        accountsRepository.deleteById(account.getAccountId());
    }

    //Once created an account can be looked up
    @Test
    void testAccountControllerIT_lookupNewAccount200() {
        String documentNumber = "11223344";
        Account accountDo = new Account().setDocumentNumber(documentNumber);
        ResponseEntity<Account> response = this.restTemplate.postForEntity("http://localhost:" + port + "/accounts", accountDo, Account.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Account responseBody = response.getBody();
        assertNotNull(responseBody);
        ResponseEntity<Account> getAccount = this.restTemplate.getForEntity("http://localhost:" + port + "/accounts/" + responseBody.getAccountId(),
                                                                    Account.class);

        Account account = getAccount.getBody();
        assertNotNull(account);
        assertNotNull(account.getAccountId());
        assertEquals(documentNumber, account.getDocumentNumber());

        accountsRepository.deleteById(account.getAccountId());
    }

    @Test
    void testAccountControllerIT_requestToCreateAccountWithAccountIdReturns400() {
        long accountNumber = 99L;
        String documentNumber = "11223344";
        Account accountDo = new Account().setDocumentNumber(documentNumber).setAccountId(accountNumber);
        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity("http://localhost:" + port + "/accounts", accountDo, JsonNode.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JsonNode responseBody = response.getBody();
        assertNotNull(responseBody);
        JsonNode message = responseBody.get("message");
        assertEquals("Account number found in create request", message.textValue());
    }

    @Test
    void testAccountControllerIT_rejectDuplicateDocumentIdWith400() {
        String documentNumber = "11223344";
        Account accountDo = new Account().setDocumentNumber(documentNumber);
        ResponseEntity<Account> response = this.restTemplate.postForEntity("http://localhost:" + port + "/accounts", accountDo, Account.class);

        Account responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<JsonNode> duplicateResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/accounts", accountDo, JsonNode.class);
        assertEquals(HttpStatus.BAD_REQUEST, duplicateResponse.getStatusCode());
        JsonNode duplicateBody = duplicateResponse.getBody();
        assertNotNull(duplicateBody);
        JsonNode message = duplicateBody.get("message");
        assertEquals("Account already exists", message.textValue());

        accountsRepository.deleteById(responseBody.getAccountId());
    }

}