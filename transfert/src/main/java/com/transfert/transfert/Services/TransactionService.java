package com.transfert.transfert.Services;

import com.transfert.transfert.Dto.Requests.DepositRequest;
import com.transfert.transfert.Dto.Requests.TransferRequest;
import com.transfert.transfert.Dto.Requests.TransactionRequest;
import com.transfert.transfert.Dto.Requests.WithdrawRequest;
import com.transfert.transfert.Entities.Account;
import com.transfert.transfert.Entities.Transaction;
import com.transfert.transfert.Entities.Receipt;
import com.transfert.transfert.Entities.Users;
import com.transfert.transfert.Enums.SubscriptionType;

import com.transfert.transfert.Enums.TransactionStatus;
import com.transfert.transfert.Enums.TransactionType;
import com.transfert.transfert.Repository.AccountRepository;
import com.transfert.transfert.Repository.TransactionRepository;
import com.transfert.transfert.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    private String generateReference() {
        long timestamp = System.currentTimeMillis();
        int randomSuffix = (int) (Math.random() * 9) + 100;
        return "REF-" + timestamp + randomSuffix;
    }

    private String generateReceiptNumber() {
        long timestamp = System.currentTimeMillis();
        int randomSuffix = (int) (Math.random() * 900) + 100; // 100-999
        return "RCT-" + timestamp + randomSuffix;
    }
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
        BigDecimal rate = account.getSubscriptionType() == SubscriptionType.FREE
                ? new BigDecimal("0.02")
                : new BigDecimal("0.01");
        BigDecimal fees = amount.multiply(rate);
        BigDecimal total = amount.subtract(fees);

        var companyAccount = accountRepository.findFirstByIsCompanyAccountTrue();
        if(companyAccount == null) throw new RuntimeException("Compte company introuvable");

        account.setBalance(account.getBalance().add(total));
        companyAccount.setBalance(companyAccount.getBalance().add(fees));
        accountRepository.save(account);
        accountRepository.save(companyAccount);

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setReceiverAccount(account);
        transaction.setAmount(amount);
        transaction.setFee(fees);
        String ref = generateReference();
        transaction.setTransactionNumber(ref);
        transaction.setReference(ref);
        transaction.setDescription("Deposit");
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setTotalAmount(total);
        transaction.setType(TransactionType.DEPOSIT);
        transactionRepository.save(transaction);

        var receipt = new Receipt();
        receipt.setReceiptNumber(generateReceiptNumber());
        receipt.setTransaction(transaction);
        transaction.setReceipt(receipt);
        transactionRepository.save(transaction);

        var response = com.transfert.transfert.Dto.Response.ReceiptResponse.fromTransactionAndReceipt(
                transaction,
                receipt,
                "deposit",
                account.getBalance().doubleValue(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<?> deposit(DepositRequest request) {
        Account account = accountRepository.findByUserId(request.userId());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Utilisateur introuvable ou compte non associé"));
        }
        TransactionRequest internal = new TransactionRequest(
                account.getId(),
                account.getId(),
                request.amount(),
                TransactionType.DEPOSIT
        );
        return handleDeposit(internal);
    }

    @Transactional
    public ResponseEntity<?> withdraw(WithdrawRequest request) {
        Account account = accountRepository.findByUserId(request.userId());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Utilisateur introuvable ou compte non associé"));
        }
        TransactionRequest internal = new TransactionRequest(
                account.getId(),
                account.getId(),
                request.amount(),
                TransactionType.WITHDRAWAL
        );
        return handleWithdrawals(internal);
    }

    @Transactional
    public ResponseEntity<?> transfer(TransferRequest request) {
        Account sender = accountRepository.findByUserId(request.userId());
        if (sender == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Expéditeur introuvable ou compte non associé"));
        }
        Optional<Users> recipientUserOpt = userRepository.findByPhoneNumber(request.recipientNumber());
        if (recipientUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Destinataire introuvable"));
        }
        Account receiver = accountRepository.findByUserId(recipientUserOpt.get().getId());
        if (receiver == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Compte du destinataire introuvable"));
        }
        TransactionRequest internal = new TransactionRequest(
                sender.getId(),
                receiver.getId(),
                request.amount(),
                TransactionType.TRANSFER
        );
        return handleTransfers(internal);
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
        checkFonds(account, total);
        checkLimits(account, total);
        account.setBalance(account.getBalance().subtract(total));
        var companyAccount = accountRepository.findFirstByIsCompanyAccountTrue();
        if(companyAccount == null) throw new RuntimeException("Compte company introuvable");
        companyAccount.setBalance(companyAccount.getBalance().add(fees));
        accountRepository.save(account);
        accountRepository.save(companyAccount);

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setReceiverAccount(account);
        transaction.setAmount(amount);
        transaction.setFee(fees);
        String ref = generateReference();
        transaction.setTransactionNumber(ref);
        transaction.setReference(ref);
        transaction.setDescription("Withdrawal");
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setTotalAmount(total);
        transaction.setType(TransactionType.WITHDRAWAL);
        transactionRepository.save(transaction);

        var receipt = new Receipt();
        receipt.setReceiptNumber(generateReceiptNumber());
        receipt.setTransaction(transaction);
        transaction.setReceipt(receipt);
        transactionRepository.save(transaction);

        var response = com.transfert.transfert.Dto.Response.ReceiptResponse.fromTransactionAndReceipt(
                transaction,
                receipt,
                "withdrawal",
                account.getBalance().doubleValue(),
                null
        );
        return ResponseEntity.ok(response);
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

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);
        transaction.setAmount(amount);
        transaction.setFee(fees);
        String ref = generateReference();
        transaction.setTransactionNumber(ref);
        transaction.setReference(ref);
        transaction.setDescription("Transfer");
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setTotalAmount(total);
        transaction.setType(TransactionType.TRANSFER);
        transactionRepository.save(transaction);

        var receipt = new Receipt();
        receipt.setReceiptNumber(generateReceiptNumber());
        receipt.setTransaction(transaction);
        transaction.setReceipt(receipt);
        transactionRepository.save(transaction);

        var response = com.transfert.transfert.Dto.Response.ReceiptResponse.fromTransactionAndReceipt(
                transaction,
                receipt,
                "transfer",
                null,
                senderAccount.getBalance().doubleValue()
        );
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<?> checkLimits(Account account, BigDecimal newAmount) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

        BigDecimal dailyTotal = transactionRepository.sumTransactionsByAccountAndDateRange(
                account.getId(), startOfDay, endOfDay
        ).orElse(BigDecimal.ZERO);

        BigDecimal monthlyTotal = transactionRepository.sumTransactionsByAccountAndDateRange(
                account.getId(), startOfMonth, endOfMonth
        ).orElse(BigDecimal.ZERO);

        dailyTotal = dailyTotal.add(newAmount);
        monthlyTotal = monthlyTotal.add(newAmount);

        if (dailyTotal.compareTo(account.getDailyLimit()) > 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Limite journalière atteinte. Réessayez dans 24h."));
        }

        if (monthlyTotal.compareTo(account.getMonthlyLimit()) > 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Limite mensuelle dépassée. Réessayez le mois prochain."));
        }
        return ResponseEntity.ok(Map.of());
    }

    private Optional<ResponseEntity<?>> checkFonds(Account account, BigDecimal amountToDebit) {
        if (account.getBalance().compareTo(amountToDebit) < 0) {
            return Optional.of(
                    ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Solde insuffisant pour effectuer la transaction."))
            );
        }
        return Optional.empty();
    }




}
