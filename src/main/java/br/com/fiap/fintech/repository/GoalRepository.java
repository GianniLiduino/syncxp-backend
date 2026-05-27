package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, UUID> {
    List<Goal> findAllByUserId(UUID userId);
    Optional<Goal> findByIdAndUserId(UUID id, UUID userId);
}
