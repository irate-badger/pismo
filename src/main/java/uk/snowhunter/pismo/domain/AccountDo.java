package uk.snowhunter.pismo.domain;

public class AccountDo {

    private Long accountNumber;
    private String documentNumber;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public AccountDo setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
        return this;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public AccountDo setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }
}