package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dto.AccountRequestDTO;
import br.com.fiap.fintech.dto.AccountResponseDTO;
import br.com.fiap.fintech.model.Account;
import br.com.fiap.fintech.model.User;
import br.com.fiap.fintech.repository.*;
import br.com.fiap.fintech.values.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private GoalDepositRepository goalDepositRepository;

    public List<AccountResponseDTO> findAll(UUID userId) {
        return accountRepository.findAllByUserId(userId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<AccountResponseDTO> findById(UUID id, UUID userId) {
        return accountRepository.findByIdAndUserId(id, userId)
                .map(this::convertToResponseDTO);
    }

    public AccountResponseDTO save(AccountRequestDTO accountDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Account account = new Account();
        account.setName(accountDto.getName());
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        
        Account savedAccount = accountRepository.save(account);
        return convertToResponseDTO(savedAccount);
    }

    public AccountResponseDTO update(UUID id, AccountRequestDTO accountDto, UUID userId) {
        return accountRepository.findByIdAndUserId(id, userId).map(account -> {
            account.setName(accountDto.getName());
            Account updatedAccount = accountRepository.save(account);
            return convertToResponseDTO(updatedAccount);
        }).orElseThrow(() -> new RuntimeException("Account not found or access denied"));
    }

    public void delete(UUID id, UUID userId) {
        Account account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));
        accountRepository.delete(account);
    }

    /**
     * Calcula o saldo disponível (dinâmico) de uma conta.
     * Fórmula: (SUM(INCOME) + SUM(TRANSFER_IN)) - (SUM(EXPENSE) + SUM(TRANSFER_OUT)) - (SUM(INVESTMENTS)) - (SUM(GOAL_DEPOSITS))
     */
    public BigDecimal calculateDynamicBalance(UUID accountId) {
        BigDecimal incomes = transactionRepository.sumAmountByAccountIdAndTransactionType(accountId, TransactionType.INCOME);
        BigDecimal transfersIn = transactionRepository.sumAmountByAccountIdAndTransactionType(accountId, TransactionType.TRANSFER_IN);
        
        BigDecimal expenses = transactionRepository.sumAmountByAccountIdAndTransactionType(accountId, TransactionType.EXPENSE);
        BigDecimal transfersOut = transactionRepository.sumAmountByAccountIdAndTransactionType(accountId, TransactionType.TRANSFER_OUT);
        
        BigDecimal totalInvestments = investmentRepository.sumAmountByAccountId(accountId);
        if (totalInvestments == null) totalInvestments = BigDecimal.ZERO;

        BigDecimal totalGoals = goalDepositRepository.sumAmountByAccountId(accountId);
        if (totalGoals == null) totalGoals = BigDecimal.ZERO;

        BigDecimal totalAvailable = incomes.add(transfersIn);
        BigDecimal totalOut = expenses.add(transfersOut).add(totalInvestments).add(totalGoals);

        return totalAvailable.subtract(totalOut);
    }

    private AccountResponseDTO convertToResponseDTO(Account account) {
        BigDecimal dynamicBalance = calculateDynamicBalance(account.getId());
        
        BigDecimal totalInvestments = investmentRepository.sumAmountByAccountId(account.getId());
        if (totalInvestments == null) totalInvestments = BigDecimal.ZERO;

        BigDecimal totalGoals = goalDepositRepository.sumAmountByAccountId(account.getId());
        if (totalGoals == null) totalGoals = BigDecimal.ZERO;
        
        return new AccountResponseDTO(
                account.getId(),
                account.getName(),
                dynamicBalance,
                totalInvestments,
                totalGoals
        );
    }
}
