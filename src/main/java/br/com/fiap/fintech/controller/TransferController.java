package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dto.TransferRequestDTO;
import br.com.fiap.fintech.model.Transfer;
import br.com.fiap.fintech.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @GetMapping
    public List<Transfer> getAll(@RequestAttribute("userId") UUID userId) {
        return transferService.findAll(userId);
    }

    @PostMapping
    public ResponseEntity<Transfer> create(@RequestBody @Valid TransferRequestDTO transferDto, @RequestAttribute("userId") UUID userId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(transferService.save(transferDto, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
