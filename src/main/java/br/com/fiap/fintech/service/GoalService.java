package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dto.GoalDepositRequestDTO;
import br.com.fiap.fintech.dto.GoalRequestDTO;
import br.com.fiap.fintech.dto.GoalResponseDTO;
import br.com.fiap.fintech.model.*;
import br.com.fiap.fintech.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoalService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalDepositRepository goalDepositRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    public List<GoalResponseDTO> findAll(UUID userId) {
        return goalRepository.findAllByUserId(userId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Goal save(GoalRequestDTO request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Goal goal = new Goal(
                request.getName(),
                request.getTargetAmount(),
                request.getDeadline(),
                user
        );

        return goalRepository.save(goal);
    }

    @Transactional
    public GoalDeposit deposit(GoalDepositRequestDTO request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Goal goal = goalRepository.findByIdAndUserId(request.getGoalId(), userId)
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied"));

        Account account = accountRepository.findByIdAndUserId(request.getAccountId(), userId)
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));

        // Validar saldo dinâmico
        BigDecimal availableBalance = accountService.calculateDynamicBalance(account.getId());
        if (availableBalance.compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Saldo insuficiente na conta para este aporte (Disponível: R$ " + availableBalance + ")");
        }

        GoalDeposit deposit = new GoalDeposit(
                request.getAmount(),
                request.getDate(),
                goal,
                account,
                user
        );

        return goalDepositRepository.save(deposit);
    }

    public Goal update(UUID id, GoalRequestDTO request, UUID userId) {
        return goalRepository.findByIdAndUserId(id, userId).map(goal -> {
            goal.setName(request.getName());
            goal.setTargetAmount(request.getTargetAmount());
            goal.setDeadline(request.getDeadline());
            return goalRepository.save(goal);
        }).orElseThrow(() -> new RuntimeException("Goal not found or access denied"));
    }

    public void delete(UUID id, UUID userId) {
        Goal goal = goalRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Goal not found or access denied"));
        // Nota: Ao deletar uma meta, os GoalDeposits vinculados (tabela N) 
        // devem ser tratados. Por simplicidade e segurança do saldo, 
        // vamos deletar os depósitos também.
        goalRepository.delete(goal);
    }

    private GoalResponseDTO convertToResponseDTO(Goal goal) {
        BigDecimal currentAmount = goalDepositRepository.sumAmountByGoalId(goal.getId());

        return new GoalResponseDTO(
                goal.getId(),
                goal.getName(),
                goal.getTargetAmount(),
                currentAmount,
                goal.getDeadline()
        );
    }
}
