package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByUserId(UUID userId);
    Optional<Category> findByIdAndUserId(UUID id, UUID userId);
}
