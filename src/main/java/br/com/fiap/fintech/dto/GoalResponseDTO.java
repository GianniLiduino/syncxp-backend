package br.com.fiap.fintech.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class GoalResponseDTO {
    private UUID id;
    private String name;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private LocalDate deadline;

    public GoalResponseDTO() {}

    public GoalResponseDTO(UUID id, String name, BigDecimal targetAmount, BigDecimal currentAmount, LocalDate deadline) {
        this.id = id;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
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

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
