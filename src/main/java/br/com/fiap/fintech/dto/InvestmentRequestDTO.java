package br.com.fiap.fintech.dto;

import br.com.fiap.fintech.values.InvestmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class InvestmentRequestDTO {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    @NotNull
    private InvestmentType type;

    @NotNull
    private UUID accountId;

    public InvestmentRequestDTO() {}

    public InvestmentRequestDTO(String name, BigDecimal amount, LocalDate date, InvestmentType type, UUID accountId) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.accountId = accountId;
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

    public InvestmentType getType() {
        return type;
    }

    public void setType(InvestmentType type) {
        this.type = type;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }
}
