package br.com.fiap.fintech.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountResponseDTO {
    private UUID id;
    private String name;
    private BigDecimal balance;
    private BigDecimal investmentBalance;
    private BigDecimal goalsBalance;

    public AccountResponseDTO() {}

    public AccountResponseDTO(UUID id, String name, BigDecimal balance, BigDecimal investmentBalance, BigDecimal goalsBalance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.investmentBalance = investmentBalance;
        this.goalsBalance = goalsBalance;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getInvestmentBalance() {
        return investmentBalance;
    }

    public void setInvestmentBalance(BigDecimal investmentBalance) {
        this.investmentBalance = investmentBalance;
    }

    public BigDecimal getGoalsBalance() {
        return goalsBalance;
    }

    public void setGoalsBalance(BigDecimal goalsBalance) {
        this.goalsBalance = goalsBalance;
    }
}
