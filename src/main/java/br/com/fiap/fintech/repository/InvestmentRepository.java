package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvestmentRepository extends JpaRepository<Investment, UUID> {
    List<Investment> findAllByUserId(UUID userId);
    Optional<Investment> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT SUM(i.amount) FROM Investment i WHERE i.account.id = :accountId")
    BigDecimal sumAmountByAccountId(@Param("accountId") UUID accountId);
}
