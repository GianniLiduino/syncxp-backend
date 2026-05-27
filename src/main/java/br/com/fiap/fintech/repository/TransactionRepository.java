package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.model.Transaction;
import br.com.fiap.fintech.values.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findAllByUserId(UUID userId);
    Optional<Transaction> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.account.id = :accountId AND t.type = :type")
    BigDecimal sumAmountByAccountIdAndTransactionType(@Param("accountId") UUID accountId, @Param("type") TransactionType type);
}
