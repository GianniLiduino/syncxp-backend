package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dto.TransactionRequestDTO;
import br.com.fiap.fintech.model.Transaction;
import br.com.fiap.fintech.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAll(
            @RequestAttribute("userId") UUID userId,
            @RequestParam(required = false) UUID accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount
    ) {
        return transactionService.findAll(userId, accountId, startDate, endDate, minAmount, maxAmount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable UUID id, @RequestAttribute("userId") UUID userId) {
        return transactionService.findById(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody @Valid TransactionRequestDTO transactionDto, @RequestAttribute("userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.save(transactionDto, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> update(@PathVariable UUID id, @RequestBody @Valid TransactionRequestDTO transactionDto, @RequestAttribute("userId") UUID userId) {
        try {
            return ResponseEntity.ok(transactionService.update(id, transactionDto, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestAttribute("userId") UUID userId) {
        try {
            transactionService.delete(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
