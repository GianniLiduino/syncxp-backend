package br.com.fiap.fintech.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "TB_TRANSFER")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "transfer_date", nullable = false)
    private LocalDate date;

    @OneToOne
    @JoinColumn(name = "origin_transaction_id", nullable = false)
    private Transaction originTransaction;

    @OneToOne
    @JoinColumn(name = "destination_transaction_id")
    private Transaction destinationTransaction;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Transfer() {}

    public Transfer(String description, BigDecimal amount, LocalDate date, Transaction originTransaction, Transaction destinationTransaction, User user) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.originTransaction = originTransaction;
        this.destinationTransaction = destinationTransaction;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Transaction getOriginTransaction() {
        return originTransaction;
    }

    public void setOriginTransaction(Transaction originTransaction) {
        this.originTransaction = originTransaction;
    }

    public Transaction getDestinationTransaction() {
        return destinationTransaction;
    }

    public void setDestinationTransaction(Transaction destinationTransaction) {
        this.destinationTransaction = destinationTransaction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
