package uk.snowhunter.pismo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long accountNumber;
    private String documentNumber;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public Account setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
        return this;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public Account setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }
}