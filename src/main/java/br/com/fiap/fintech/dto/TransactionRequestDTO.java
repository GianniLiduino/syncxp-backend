package br.com.fiap.fintech.dto;

import br.com.fiap.fintech.values.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TransactionRequestDTO {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    @NotNull
    private TransactionType type;

    @NotNull
    private UUID accountId;

    private UUID categoryId;

    public TransactionRequestDTO() {}

    public TransactionRequestDTO(String name, BigDecimal amount, LocalDate date, TransactionType type, UUID accountId, UUID categoryId) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.accountId = accountId;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
}
