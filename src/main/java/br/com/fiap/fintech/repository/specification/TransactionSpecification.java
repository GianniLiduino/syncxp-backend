package br.com.fiap.fintech.repository.specification;

import br.com.fiap.fintech.model.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TransactionSpecification {

    public static Specification<Transaction> filter(
            UUID userId,
            UUID accountId,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal minAmount,
            BigDecimal maxAmount
    ) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Filtro obrigatório: User ID (Multi-tenant)
            predicates.add(cb.equal(root.get("user").get("id"), userId));

            // Filtro opcional: Account ID
            if (accountId != null) {
                predicates.add(cb.equal(root.get("account").get("id"), accountId));
            }

            // Filtro opcional: Período
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), endDate));
            }

            // Filtro opcional: Valores
            if (minAmount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), minAmount));
            }
            if (maxAmount != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), maxAmount));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
