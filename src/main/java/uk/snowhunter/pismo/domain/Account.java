package uk.snowhunter.pismo.domain;


import io.swagger.v3.oas.annotations.media.Schema;

public class Account {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "account_id")
    private Long accountId;

    @Schema(name = "document_number", defaultValue = "111-222-333-444")
    private String documentNumber;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public Account setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
        return this;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Account setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }
}