package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dto.TransactionRequestDTO;
import br.com.fiap.fintech.model.Account;
import br.com.fiap.fintech.model.Category;
import br.com.fiap.fintech.model.Transaction;
import br.com.fiap.fintech.model.User;
import br.com.fiap.fintech.repository.*;
import br.com.fiap.fintech.repository.specification.TransactionSpecification;
import br.com.fiap.fintech.values.CategoryType;
import br.com.fiap.fintech.values.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Transaction> findAll(
            UUID userId,
            UUID accountId,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal minAmount,
            BigDecimal maxAmount
    ) {
        Specification<Transaction> spec = TransactionSpecification.filter(
                userId, accountId, startDate, endDate, minAmount, maxAmount
        );
        return transactionRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "date"));
    }

    public Optional<Transaction> findById(UUID id, UUID userId) {
        return transactionRepository.findByIdAndUserId(id, userId);
    }

    public Transaction save(TransactionRequestDTO transactionDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByIdAndUserId(transactionDto.getAccountId(), userId)
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));
        
        Category category = null;
        if (transactionDto.getCategoryId() != null) {
            category = categoryRepository.findByIdAndUserId(transactionDto.getCategoryId(), userId)
                    .orElseThrow(() -> new RuntimeException("Category not found or access denied"));
            // Validação Cruzada: TransactionType vs CategoryType
            validateTransactionTypeWithCategory(transactionDto.getType(), category.getType());
        }

        Transaction transaction = new Transaction(
                transactionDto.getName(),
                transactionDto.getAmount(),
                transactionDto.getDate(),
                transactionDto.getType(),
                account,
                category,
                user
        );

        return transactionRepository.save(transaction);
    }

    public Transaction update(UUID id, TransactionRequestDTO transactionDto, UUID userId) {
        return transactionRepository.findByIdAndUserId(id, userId).map(transaction -> {
            transaction.setName(transactionDto.getName());
            transaction.setAmount(transactionDto.getAmount());
            transaction.setDate(transactionDto.getDate());
            transaction.setType(transactionDto.getType());

            if (transactionDto.getAccountId() != null) {
                Account account = accountRepository.findByIdAndUserId(transactionDto.getAccountId(), userId)
                        .orElseThrow(() -> new RuntimeException("Account not found or access denied"));
                transaction.setAccount(account);
            }

            if (transactionDto.getCategoryId() != null) {
                Category category = categoryRepository.findByIdAndUserId(transactionDto.getCategoryId(), userId)
                        .orElseThrow(() -> new RuntimeException("Category not found or access denied"));
                
                validateTransactionTypeWithCategory(transaction.getType(), category.getType());
                transaction.setCategory(category);
            } else {
                transaction.setCategory(null);
            }

            return transactionRepository.save(transaction);
        }).orElseThrow(() -> new RuntimeException("Transaction not found or access denied"));
    }

    public void delete(UUID id, UUID userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Transaction not found or access denied"));
        transactionRepository.delete(transaction);
    }

    private void validateTransactionTypeWithCategory(TransactionType transType, CategoryType catType) {
        if ((transType == TransactionType.INCOME || transType == TransactionType.TRANSFER_IN) && catType != CategoryType.RECEITA) {
            throw new RuntimeException("Transações de Entrada exigem uma categoria do tipo RECEITA");
        }
        if ((transType == TransactionType.EXPENSE || transType == TransactionType.TRANSFER_OUT) && catType != CategoryType.DESPESA) {
            throw new RuntimeException("Transações de Saída exigem uma categoria do tipo DESPESA");
        }
    }
}
