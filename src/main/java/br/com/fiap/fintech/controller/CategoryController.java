package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dto.CategoryRequestDTO;
import br.com.fiap.fintech.model.Category;
import br.com.fiap.fintech.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAll(@RequestAttribute("userId") UUID userId) {
        return categoryService.findAll(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable UUID id, @RequestAttribute("userId") UUID userId) {
        return categoryService.findById(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody @Valid CategoryRequestDTO categoryDto, @RequestAttribute("userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(categoryDto, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable UUID id, @RequestBody @Valid CategoryRequestDTO categoryDto, @RequestAttribute("userId") UUID userId) {
        try {
            return ResponseEntity.ok(categoryService.update(id, categoryDto, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @RequestAttribute("userId") UUID userId) {
        try {
            categoryService.delete(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
