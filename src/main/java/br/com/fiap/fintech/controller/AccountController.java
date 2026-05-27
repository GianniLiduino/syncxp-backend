package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dto.AccountRequestDTO;
import br.com.fiap.fintech.dto.AccountResponseDTO;
import br.com.fiap.fintech.model.Account;
import br.com.fiap.fintech.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<AccountResponseDTO> getAll(@RequestAttribute("userId") UUID userId) {
        return accountService.findAll(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getById(@PathVariable UUID id, @RequestAttribute("userId") UUID userId) {
        return accountService.findById(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(@RequestBody @Valid AccountRequestDTO accountDto, @RequestAttribute("userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.save(accountDto, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid AccountRequestDTO accountDto, @RequestAttribute("userId") UUID userId) {
        try {
            return ResponseEntity.ok(accountService.update(id, accountDto, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestAttribute("userId") UUID userId) {
        try {
            accountService.delete(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
