package com.transfert.transfert.Services;

import com.transfert.transfert.Dto.Response.AccountResponse;
import com.transfert.transfert.Entities.Account;
import com.transfert.transfert.Entities.Users;
import com.transfert.transfert.Enums.AccountStatus;
import com.transfert.transfert.Enums.SubscriptionType;
import com.transfert.transfert.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private static final int MAX_ATTEMPTS = 5;

    public ResponseEntity<?> verifyPin(String pin) {
        Map<String, Object> response = new HashMap<>();
        if (pin == null || pin.isEmpty()) {
            response.put("message", "PIN vide ou incorrect");
            return ResponseEntity.badRequest().body(response);
        }
        Users user = authService.getCurrentUser();
        Account account = accountRepository.findByUser(Optional.ofNullable(user));
        if (account == null || account.getStatus() != AccountStatus.ACTIVE) {
            response.put("message", "Compte introuvable ou inactif");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        boolean isPinCorrect = passwordEncoder.matches(pin, account.getPinHash());
        if (!isPinCorrect) {
            account.setFailedAttempts(account.getFailedAttempts() + 1);

            if (account.getFailedAttempts() >= MAX_ATTEMPTS) {
                account.setStatus(AccountStatus.FROZEN);
                response.put("message", "PIN incorrect, compte bloqué, contactez le service client");
            } else {
                int remainingAttempts = MAX_ATTEMPTS - account.getFailedAttempts();
                response.put("message", "PIN incorrect");
                response.put("tentatives restants", remainingAttempts);
            }

            accountRepository.save(account);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        account.setFailedAttempts(0);
        accountRepository.save(account);
        var accountDetails = new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.getSubscriptionType(),
                account.getDailyLimit(),
                account.getMonthlyLimit()
        );
        response.put("message", "PIN validé");
        response.put("account", accountDetails);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> updateSubscription(Long accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null || account.getStatus() != AccountStatus.ACTIVE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Compte introuvable ou inactif"));
        }
        account.setSubscriptionType(SubscriptionType.PREMIUM);
        account.setDailyLimit(new BigDecimal("200000"));
        account.setMonthlyLimit(new BigDecimal("1000000"));
        accountRepository.save(account);

        return ResponseEntity.ok(Map.of("account", new AccountResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.getSubscriptionType(),
                account.getDailyLimit(),
                account.getMonthlyLimit()
                )));
    }
}
