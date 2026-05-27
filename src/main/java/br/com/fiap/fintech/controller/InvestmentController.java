package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dto.InvestmentRequestDTO;
import br.com.fiap.fintech.dto.InvestmentTypeResponseDTO;
import br.com.fiap.fintech.model.Investment;
import br.com.fiap.fintech.service.InvestmentService;
import br.com.fiap.fintech.values.InvestmentType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    @GetMapping("/types")
    public List<InvestmentTypeResponseDTO> getInvestmentTypes() {
        return Arrays.stream(InvestmentType.values())
                .map(type -> new InvestmentTypeResponseDTO(type.name(), type.getDescription(), type.getAssetClass()))
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<Investment> getAll(@RequestAttribute("userId") UUID userId) {
        return investmentService.findAll(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investment> getById(@PathVariable UUID id, @RequestAttribute("userId") UUID userId) {
        return investmentService.findById(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Investment> create(@RequestBody @Valid InvestmentRequestDTO investmentDto, @RequestAttribute("userId") UUID userId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(investmentService.save(investmentDto, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestAttribute("userId") UUID userId) {
        try {
            investmentService.delete(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
