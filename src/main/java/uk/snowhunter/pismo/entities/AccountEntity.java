package uk.snowhunter.pismo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ACCOUNTS")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long accountId;

    @Column(unique = true)
    private String documentNumber;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public AccountEntity setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
        return this;
    }

    public Long getAccountId() {
        return accountId;
    }

    public AccountEntity setAccountId(long accountNumber) {
        this.accountId = accountNumber;
        return this;
    }
}