package com.transfert.transfert.Services;

import com.transfert.transfert.Dto.Response.LoginResponse;
import com.transfert.transfert.Dto.Response.UserByIdResponse;
import com.transfert.transfert.Dto.Response.UsersResponse;
import com.transfert.transfert.Entities.Account;
import com.transfert.transfert.Entities.Transaction;
import com.transfert.transfert.Entities.Users;
import com.transfert.transfert.Mappers.UsersMapper;
import com.transfert.transfert.Repository.AccountRepository;
import com.transfert.transfert.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<?> refreshToken(String oldToken) {
        if (oldToken == null || oldToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le token est obligatoire"));
        }
        String username;
        try {
            username = authService.getUsernameFromToken(oldToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token invalide ou expiré"));
        }
        var userOpt = authService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Utilisateur non trouvé"));
        }
        var newToken = authService.generateToken(userOpt.get());
        return ResponseEntity.ok(new LoginResponse(
                userOpt.get().getId(),
                newToken,
                userOpt.get().getRole().name(),
                userOpt.get().getUsername(),
                userOpt.get().getCountry(),
                userOpt.get().getFirstName(),
                userOpt.get().getEmail(),
                userOpt.get().getLastName(),
                userOpt.get().getStatus(),
                userOpt.get().getSubscriptionType(),
                userOpt.get().getPhoneNumber(),
                userOpt.get().getRectoCni(),
                userOpt.get().getVersoCni(),
                userOpt.get().getPhoto()
        ));
    }

    public ResponseEntity<?> getAllUsers() {
        List<com.transfert.transfert.Entities.Users> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<Users> updateUser(String id, Users user) {
        Long castedId = Long.parseLong(id);
        return userRepository.findById(castedId).map(u -> {
            if (user.getUsername() != null) u.setUsername(user.getUsername());
            if (user.getCountry() != null) u.setCountry(user.getCountry());
            if (user.getPhoneNumber() != null) u.setPhoneNumber(user.getPhoneNumber());
            if (user.getFirstName() != null) u.setFirstName(user.getFirstName());
            if (user.getIdNumber() != null) u.setIdNumber(user.getIdNumber());
            if (user.getEmail() != null) u.setEmail(user.getEmail());
            //if (user.getPhotoUrl() != null) u.setPhotoUrl(user.getPhotoUrl());
            Users savedUser = userRepository.save(u);
            return ResponseEntity.ok(savedUser);
        }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<UserByIdResponse> getUser(String id) {
        Long castedId = Long.parseLong(id);
        Account account = accountRepository.findByUserId(castedId);
        List<Transaction> sentTransaction = account.getSentTransactions();
        List<Transaction> receivedtransaction = account.getReceivedTransactions();
        BigDecimal balance = account.getBalance();

        // Build consolidated transaction history
        java.util.Map<Long, Transaction> unique = new java.util.LinkedHashMap<>();
        if (sentTransaction != null) {
            for (Transaction t : sentTransaction) {
                unique.putIfAbsent(t.getId(), t);
            }
        }
        if (receivedtransaction != null) {
            for (Transaction t : receivedtransaction) {
                unique.putIfAbsent(t.getId(), t);
            }
        }
        java.util.List<com.transfert.transfert.Dto.Response.TransactionHistoryItem> history = new java.util.ArrayList<>();
        for (Transaction t : unique.values()) {
            String typeLabel;
            switch (t.getType()) {
                case DEPOSIT -> typeLabel = "depot";
                case WITHDRAWAL -> typeLabel = "retrait";
                case TRANSFER -> {
                    boolean isSender = t.getSenderAccount() != null && t.getSenderAccount().getId().equals(account.getId());
                    boolean isReceiver = t.getReceiverAccount() != null && t.getReceiverAccount().getId().equals(account.getId());
                    if (isSender && !isReceiver) typeLabel = "transfert";
                    else if (!isSender && isReceiver) typeLabel = "reception";
                    else typeLabel = "transfert"; // fallback
                }
                default -> typeLabel = t.getType() != null ? t.getType().name().toLowerCase() : "inconnu";
            }
            history.add(new com.transfert.transfert.Dto.Response.TransactionHistoryItem(
                    typeLabel,
                    t.getAmount(),
                    t.getCreatedAt(),
                    t.getStatus() != null ? t.getStatus().name().toLowerCase() : null,
                    t.getTransactionNumber()
            ));
        }
        // Optionally sort by date desc
        history.sort((a, b) -> b.date().compareTo(a.date()));

        return userRepository.findById(castedId)
                .map(user -> ResponseEntity.ok(UsersMapper.toDto(user, balance, sentTransaction, receivedtransaction, history)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteUser(String id) {
        Long castedId = Long.parseLong(id);
        if (!userRepository.existsById(castedId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Utilisateur non trouvé"));
        }
        userRepository.deleteById(castedId);
        return ResponseEntity.ok().body(Map.of("message", "Utilisateur supprimé avec succès"));
    }
}
