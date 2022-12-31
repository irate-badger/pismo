package uk.snowhunter.pismo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import uk.snowhunter.pismo.domain.AccountDo;
import uk.snowhunter.pismo.entities.Account;
import uk.snowhunter.pismo.exceptions.InvalidAccountException;
import uk.snowhunter.pismo.respository.AccountsRepository;

@RestController
public class AccountsController {

    final AccountsRepository accountsRepository;

    public AccountsController(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    Logger logger = LoggerFactory.getLogger(AccountsController.class);

    @GetMapping("/accounts/{id}")
    public AccountDo get(@PathVariable Optional<Long> id) throws InvalidAccountException {
        logger.info(String.format("ID to process: %s", id));

        Account account = id.map(accountsRepository::findByAccountNumber).orElseThrow(InvalidAccountException::new);
        return new AccountDo()
            .setAccountNumber(account.getAccountNumber())
            .setDocumentNumber(account.getDocumentNumber());
    }

    @PostMapping("/accounts")
    public AccountDo post(@RequestBody AccountDo incomingEntity) {
        Account account = new Account().setDocumentNumber(incomingEntity.getDocumentNumber());
        account = accountsRepository.save(account);
        return new AccountDo()
            .setAccountNumber(account.getAccountNumber())
            .setDocumentNumber(account.getDocumentNumber());
    }
}