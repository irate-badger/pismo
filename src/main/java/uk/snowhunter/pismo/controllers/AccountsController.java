package uk.snowhunter.pismo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import uk.snowhunter.pismo.domain.Account;
import uk.snowhunter.pismo.entities.AccountEntity;
import uk.snowhunter.pismo.exceptions.InvalidAccountException;
import uk.snowhunter.pismo.exceptions.InvalidRequestException;
import uk.snowhunter.pismo.respository.AccountRepository;

@RestController
@Tag(name = "Accounts", description = "Methods for interacting with accounts")
public class AccountsController {

    final AccountRepository accountsRepository;

    public AccountsController(AccountRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    @Operation(summary = "Retrive an account by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the account",
        content = { @Content(mediaType = "application/json",
          schema = @Schema(implementation = Account.class)) }),
        @ApiResponse(responseCode = "404", description = "Account not found",
        content = @Content) })
    @GetMapping("/accounts/{id}")
    public Account get(@PathVariable Optional<Long> id) throws InvalidAccountException {
        AccountEntity account = id.map(accountsRepository::findByAccountId).orElseThrow(InvalidAccountException::new);
        return new Account()
            .setAccountId(account.getAccountId())
            .setDocumentNumber(account.getDocumentNumber());
    }

    @Operation(summary = "Create a new account by document number")
    @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Successfully created the account",
        content = { @Content(mediaType = "application/json",
          schema = @Schema(implementation = Account.class)) }),
        @ApiResponse(responseCode = "400", description = "Document number not provided, or account already exists",
        content = @Content) })
    @PostMapping("/accounts")
    public Account post(@RequestBody Account incomingEntity) throws InvalidRequestException {
        if (incomingEntity.getAccountId() != null) {
            throw new InvalidRequestException("Account number found in create request");
        }
        if (incomingEntity.getDocumentNumber() == null) {
            throw new InvalidRequestException("No document number found");
        }

        AccountEntity account = new AccountEntity().setDocumentNumber(incomingEntity.getDocumentNumber());
        try {
            account = accountsRepository.save(account);
        } catch (Exception e) {
            throw new InvalidRequestException("Account already exists");
        }
        return new Account()
            .setAccountId(account.getAccountId())
            .setDocumentNumber(account.getDocumentNumber());
    }

    /**
     * Handles alphanumeric get account requests
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", "Cannot read value: %s".formatted(ex.getValue()));
        body.put("message","Invalid parameter provided");
        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }

}