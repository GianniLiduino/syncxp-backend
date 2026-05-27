package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dto.InvestmentRequestDTO;
import br.com.fiap.fintech.model.Account;
import br.com.fiap.fintech.model.Investment;
import br.com.fiap.fintech.model.User;
import br.com.fiap.fintech.repository.AccountRepository;
import br.com.fiap.fintech.repository.InvestmentRepository;
import br.com.fiap.fintech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    public List<Investment> findAll(UUID userId) {
        return investmentRepository.findAllByUserId(userId);
    }

    public Optional<Investment> findById(UUID id, UUID userId) {
        return investmentRepository.findByIdAndUserId(id, userId);
    }

    @Transactional
    public Investment save(InvestmentRequestDTO investmentDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByIdAndUserId(investmentDto.getAccountId(), userId)
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));

        // Validação usando o saldo dinâmico
        BigDecimal dynamicBalance = accountService.calculateDynamicBalance(account.getId());
        if (dynamicBalance.compareTo(investmentDto.getAmount()) < 0) {
            throw new RuntimeException("Saldo insuficiente na conta para realizar o investimento (Saldo disponível: R$ " + dynamicBalance + ")");
        }

        Investment investment = new Investment(
                investmentDto.getName(),
                investmentDto.getAmount(),
                investmentDto.getDate(),
                investmentDto.getType(),
                account,
                user
        );

        return investmentRepository.save(investment);
    }

    public void delete(UUID id, UUID userId) {
        Investment investment = investmentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Investment not found or access denied"));
        
        investmentRepository.delete(investment);
    }
}
