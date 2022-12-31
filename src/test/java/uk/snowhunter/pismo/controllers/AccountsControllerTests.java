package uk.snowhunter.pismo.controllers;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.snowhunter.pismo.domain.AccountDo;
import uk.snowhunter.pismo.entities.Account;
import uk.snowhunter.pismo.exceptions.InvalidAccountException;
import uk.snowhunter.pismo.respository.AccountsRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountsControllerTests {

    @Mock
    AccountsRepository accountsRepository;

    @InjectMocks
    AccountsController accountsController;

    // Happy path of getting an existing account
    @Test
    void testGet() throws Exception {
        when(accountsRepository.findByAccountNumber(1L))
            .thenReturn(new Account().setAccountNumber(1L).setDocumentNumber("ABC123"));

        AccountDo dto = accountsController.get(Optional.of(1L));
        assertEquals(1L, dto.getAccountNumber());
        assertEquals("ABC123", dto.getDocumentNumber());
    }

    // When an invalid value is passed, no account number is set. This will raise a null pointer (to be managed in error handler)
    @Test
    void testGet_withNoAccountNumberSet() {
        assertThrows(InvalidAccountException.class,
                     () -> accountsController.get(Optional.empty()),
                     "Expected controller to throw InvalidAccountException, but it didn't");
    }

    // When a valid value is passed but no account is present, this will raise a nullpointer (to be managed in error handler)
    @Test
    void testGet_withInvalidAccountNumberSet() {
        when(accountsRepository.findByAccountNumber(5L))
            .thenThrow(new NullPointerException());

        assertThrows(NullPointerException.class,
                     () -> accountsController.get(Optional.of(5L)),
                     "Expected controller to throw NullPointer, but it didn't");
    }

    // Happy path of creating a new account
    @Test
    void testPost() {
        when(accountsRepository.save(any(Account.class)))
            .thenReturn(new Account().setAccountNumber(1L).setDocumentNumber("ABC123"));

        AccountDo dto = accountsController.post(new AccountDo().setAccountNumber(1L).setDocumentNumber("ABC123"));
        assertEquals(1L, dto.getAccountNumber());
        assertEquals("ABC123", dto.getDocumentNumber());
    }

    // When no document number is set then this returns an error
    @Test
    void testPost_missingDocumentNumberReturnsError() {
        assertThrows(InvalidAccountException.class,
                     () -> accountsController.get(Optional.empty()),
                     "Expected controller to throw InvalidAccountException, but it didn't");
    }

    // When an account number is set, this returns an error

}