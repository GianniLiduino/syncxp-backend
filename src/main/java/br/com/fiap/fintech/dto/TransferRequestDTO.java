package br.com.fiap.fintech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TransferRequestDTO {

    private String description;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    @NotNull
    private UUID originAccountId;

    private UUID originCategoryId;

    private UUID destinationAccountId; // Null para transferência externa

    private UUID destinationCategoryId; // Null para transferência externa

    public TransferRequestDTO() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public UUID getOriginAccountId() {
        return originAccountId;
    }

    public void setOriginAccountId(UUID originAccountId) {
        this.originAccountId = originAccountId;
    }

    public UUID getOriginCategoryId() {
        return originCategoryId;
    }

    public void setOriginCategoryId(UUID originCategoryId) {
        this.originCategoryId = originCategoryId;
    }

    public UUID getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(UUID destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public UUID getDestinationCategoryId() {
        return destinationCategoryId;
    }

    public void setDestinationCategoryId(UUID destinationCategoryId) {
        this.destinationCategoryId = destinationCategoryId;
    }
}
