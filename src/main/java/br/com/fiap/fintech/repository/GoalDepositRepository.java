package br.com.fiap.fintech.repository;

import br.com.fiap.fintech.model.GoalDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface GoalDepositRepository extends JpaRepository<GoalDeposit, UUID> {
    List<GoalDeposit> findAllByGoalIdAndUserId(UUID goalId, UUID userId);

    @Query("SELECT COALESCE(SUM(gd.amount), 0) FROM GoalDeposit gd WHERE gd.goal.id = :goalId")
    BigDecimal sumAmountByGoalId(@Param("goalId") UUID goalId);

    @Query("SELECT COALESCE(SUM(gd.amount), 0) FROM GoalDeposit gd WHERE gd.account.id = :accountId")
    BigDecimal sumAmountByAccountId(@Param("accountId") UUID accountId);
}
