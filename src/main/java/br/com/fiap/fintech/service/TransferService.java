package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dto.TransferRequestDTO;
import br.com.fiap.fintech.model.*;
import br.com.fiap.fintech.repository.*;
import br.com.fiap.fintech.values.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    public List<Transfer> findAll(UUID userId) {
        return transferRepository.findAllByUserId(userId);
    }

    @Transactional
    public Transfer save(TransferRequestDTO request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1. Validar e buscar conta de origem
        Account originAccount = accountRepository.findByIdAndUserId(request.getOriginAccountId(), userId)
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));

        // 2. Validar saldo dinâmico da origem
        BigDecimal availableBalance = accountService.calculateDynamicBalance(originAccount.getId());
        if (availableBalance.compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Saldo insuficiente na conta de origem");
        }

        // 3. Buscar categoria de origem (opcional, deve ser DESPESA se presente)
        Category originCategory = null;
        if (request.getOriginCategoryId() != null) {
            originCategory = categoryRepository.findByIdAndUserId(request.getOriginCategoryId(), userId)
                    .orElseThrow(() -> new RuntimeException("Categoria de origem não encontrada"));
        }

        // 4. Criar Transação de Saída (TRANSFER_OUT)
        Transaction originTransaction = new Transaction(
                "Transferência: " + (request.getDescription() != null ? request.getDescription() : "Sem descrição"),
                request.getAmount(),
                request.getDate(),
                TransactionType.TRANSFER_OUT,
                originAccount,
                originCategory,
                user
        );
        transactionRepository.save(originTransaction);

        Transaction destinationTransaction = null;

        // 5. Se for transferência interna, processar destino
        if (request.getDestinationAccountId() != null) {
            Account destinationAccount = accountRepository.findByIdAndUserId(request.getDestinationAccountId(), userId)
                    .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));
            
            Category destinationCategory = null;
            if (request.getDestinationCategoryId() != null) {
                destinationCategory = categoryRepository.findByIdAndUserId(request.getDestinationCategoryId(), userId)
                        .orElseThrow(() -> new RuntimeException("Categoria de destino não encontrada"));
            }

            destinationTransaction = new Transaction(
                    "Recebido: " + (request.getDescription() != null ? request.getDescription() : "Sem descrição"),
                    request.getAmount(),
                    request.getDate(),
                    TransactionType.TRANSFER_IN,
                    destinationAccount,
                    destinationCategory,
                    user
            );
            transactionRepository.save(destinationTransaction);
        }

        // 6. Criar e salvar Transferência
        Transfer transfer = new Transfer(
                request.getDescription(),
                request.getAmount(),
                request.getDate(),
                originTransaction,
                destinationTransaction,
                user
        );

        return transferRepository.save(transfer);
    }
}
