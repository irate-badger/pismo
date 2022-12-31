package uk.snowhunter.pismo.controllers;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.snowhunter.pismo.domain.Account;
import uk.snowhunter.pismo.entities.AccountEntity;
import uk.snowhunter.pismo.exceptions.InvalidAccountException;
import uk.snowhunter.pismo.exceptions.InvalidRequestException;
import uk.snowhunter.pismo.respository.AccountRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountsControllerTests {

    @Mock
    AccountRepository accountsRepository;

    @InjectMocks
    AccountsController accountsController;

    @Test
    void testGetAccountsController_success() throws Exception {
        when(accountsRepository.findByAccountId(1L))
            .thenReturn(new AccountEntity().setAccountId(1L).setDocumentNumber("ABC123"));

        Account dto = accountsController.get(Optional.of(1L));
        assertEquals(1L, dto.getAccountId());
        assertEquals("ABC123", dto.getDocumentNumber());
    }

    // When an invalid value is passed, no account number is set. This will raise an invalid account error (to be managed in error handler)
    @Test
    void testGetAccountsController_noAccountNumberThrowsException() {
        assertThrows(InvalidAccountException.class,
                     () -> accountsController.get(Optional.empty()),
                     "Expected controller to throw InvalidAccountException, but it didn't");
    }

    // When a valid value is passed but no account is present, this will raise a nullpointer (to be managed in error handler)
    @Test
    void testGetAccountsController_invalidAccountNumberThrowsException() {
        when(accountsRepository.findByAccountId(5L))
            .thenThrow(new NullPointerException());

        assertThrows(NullPointerException.class,
                     () -> accountsController.get(Optional.of(5L)),
                     "Expected controller to throw NullPointer, but it didn't");
    }

    // Happy path of creating a new account
    @Test
    void testPostAccountsController_success() {
        when(accountsRepository.save(any(AccountEntity.class)))
            .thenReturn(new AccountEntity().setAccountId(1L).setDocumentNumber("ABC123"));

        Account dto = accountsController.post(new Account().setDocumentNumber("ABC123"));
        assertEquals(1L, dto.getAccountId());
        assertEquals("ABC123", dto.getDocumentNumber());
    }

    // When no document number is set then this returns an error
    @Test
    void testPostAccountsController_missingDocumentNumberReturnsError() {
        assertThrows(InvalidRequestException.class,
                     () -> accountsController.post(new Account()),
                     "Expected controller to throw InvalidRequestException, but it didn't");
    }

    // When an account number is set, this returns an error
    @Test
    void testPostAccountsController_setAccountNumberReturnsError() {
        assertThrows(InvalidRequestException.class,
                     () -> accountsController.post(new Account().setDocumentNumber("ABC123").setAccountId(1L)),
                     "Expected controller to throw InvalidRequestException, but it didn't");
    }

}