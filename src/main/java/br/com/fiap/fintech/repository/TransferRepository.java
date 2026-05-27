package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
    List<Transfer> findAllByUserId(UUID userId);
    Optional<Transfer> findByIdAndUserId(UUID id, UUID userId);
}
