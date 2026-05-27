package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dto.GoalDepositRequestDTO;
import br.com.fiap.fintech.dto.GoalRequestDTO;
import br.com.fiap.fintech.dto.GoalResponseDTO;
import br.com.fiap.fintech.model.Goal;
import br.com.fiap.fintech.model.GoalDeposit;
import br.com.fiap.fintech.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @GetMapping
    public List<GoalResponseDTO> getAll(@RequestAttribute("userId") UUID userId) {
        return goalService.findAll(userId);
    }

    @PostMapping
    public ResponseEntity<Goal> create(@RequestBody @Valid GoalRequestDTO goalDto, @RequestAttribute("userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(goalService.save(goalDto, userId));
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody @Valid GoalDepositRequestDTO depositDto, @RequestAttribute("userId") UUID userId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(goalService.deposit(depositDto, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody @Valid GoalRequestDTO goalDto, @RequestAttribute("userId") UUID userId) {
        try {
            return ResponseEntity.ok(goalService.update(id, goalDto, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id, @RequestAttribute("userId") UUID userId) {
        try {
            goalService.delete(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
