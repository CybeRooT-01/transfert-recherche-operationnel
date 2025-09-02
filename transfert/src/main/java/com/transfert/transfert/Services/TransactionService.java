package com.transfert.transfert.Services;

import com.transfert.transfert.Dto.Requests.TransactionRequest;
import com.transfert.transfert.Enums.SubscriptionType;

import com.transfert.transfert.Repository.AccountRepository;
import com.transfert.transfert.Repository.TransactionRepository;
import com.transfert.transfert.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    public ResponseEntity<?> handleTransaction(TransactionRequest request) {
        return switch (request.transactiontype()) {
            case DEPOSIT -> handleDeposit(request);
            case WITHDRAWAL -> handleWithdrawals(request);
            case TRANSFER -> handleTransfers(request);
            default -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Type de transaction inconnu"));
        };
    }


    @Transactional
    public ResponseEntity<?> handleDeposit(TransactionRequest transactionRequest){
        var account = accountRepository.findById(transactionRequest.receiverAccountId())
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        BigDecimal amount = transactionRequest.amount();
        BigDecimal fees = BigDecimal.ZERO;
        BigDecimal total = amount.subtract(fees);
        account.setBalance(account.getBalance().add(total));
        accountRepository.save(account);
        return ResponseEntity.ok(Map.of(
                "success", "deposit",
                "amountCredited", total,
                "newBalance", account.getBalance()
        ));
    }


    @Transactional
    public ResponseEntity<?> handleWithdrawals(TransactionRequest transactionRequest){
        var account = accountRepository.findById(transactionRequest.senderAccountId())
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        BigDecimal amount = transactionRequest.amount();

        BigDecimal rate = account.getSubscriptionType() == SubscriptionType.FREE
                ? new BigDecimal("0.02")
                : new BigDecimal("0.01");

        BigDecimal fees = amount.multiply(rate);
        BigDecimal total = amount.add(fees);
        if(account.getBalance().compareTo(total) < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Solde insuffisant"));
        }
        account.setBalance(account.getBalance().subtract(total));
        var companyAccount = accountRepository.findFirstByIsCompanyAccountTrue();
        if(companyAccount == null) throw new RuntimeException("Compte company introuvable");
        companyAccount.setBalance(companyAccount.getBalance().add(fees));
        accountRepository.save(account);
        accountRepository.save(companyAccount);
        return ResponseEntity.ok(Map.of(
                "success", "withdrawal",
                "amountDebited", total,
                "fees", fees,
                "newBalance", account.getBalance()
        ));
    }


    @Transactional
    public ResponseEntity<?> handleTransfers(TransactionRequest transactionRequest){
        var senderAccount = accountRepository.findById(transactionRequest.senderAccountId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        var receiverAccount = accountRepository.findById(transactionRequest.receiverAccountId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        var companyAccount = accountRepository.findFirstByIsCompanyAccountTrue();
        if(companyAccount == null) throw new RuntimeException("Compte company introuvable");

        BigDecimal amount = transactionRequest.amount();
        BigDecimal rate = senderAccount.getSubscriptionType() == SubscriptionType.FREE ?
                new BigDecimal("0.02") : new BigDecimal("0.01");
        BigDecimal fees = amount.multiply(rate);
        BigDecimal total = amount.add(fees);

        if(senderAccount.getBalance().compareTo(total) < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Solde insuffisant"));
        }
        senderAccount.setBalance(senderAccount.getBalance().subtract(total));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));
        companyAccount.setBalance(companyAccount.getBalance().add(fees));
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
        accountRepository.save(companyAccount);

        return ResponseEntity.ok(Map.of(
                "success", "transfer",
                "amountDebited", total,
                "fees", fees,
                "newSenderBalance", senderAccount.getBalance()
        ));
    }


}
