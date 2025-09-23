package com.transfert.transfert.Mappers;

import com.transfert.transfert.Dto.Response.TransactionHistoryItem;
import com.transfert.transfert.Dto.Response.UserByIdResponse;
import com.transfert.transfert.Entities.Transaction;
import com.transfert.transfert.Entities.Users;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UsersMapper {
    public static UserByIdResponse toDto(Users user, BigDecimal balance, List<Transaction> sentTransaction, List<Transaction> receivedTransaction, List<TransactionHistoryItem> history) {
        if (user == null) return null;
        List<com.transfert.transfert.Dto.Response.TransactionSimpleResponse> depot = new ArrayList<>();
        List<com.transfert.transfert.Dto.Response.TransactionSimpleResponse> retrait = new ArrayList<>();
        List<com.transfert.transfert.Dto.Response.TransactionSimpleResponse> transfer = new ArrayList<>();
        List<com.transfert.transfert.Dto.Response.TransactionSimpleResponse> received = new ArrayList<>();
        if (sentTransaction != null) {
            for (Transaction t : sentTransaction) {
                if (t.getType() != null) {
                    var simple = new com.transfert.transfert.Dto.Response.TransactionSimpleResponse(
                        t.getId(),
                        t.getTransactionNumber(),
                        t.getAmount(),
                        t.getFee(),
                        t.getType().name(),
                        t.getStatus() != null ? t.getStatus().name() : null,
                        t.getCreatedAt()
                    );
                    switch (t.getType()) {
                        case DEPOSIT -> depot.add(simple);
                        case WITHDRAWAL -> retrait.add(simple);
                        case TRANSFER -> transfer.add(simple);
                    }
                }
            }
        }
        if (receivedTransaction != null) {
            for (Transaction t : receivedTransaction) {
                var simple = new com.transfert.transfert.Dto.Response.TransactionSimpleResponse(
                    t.getId(),
                    t.getTransactionNumber(),
                    t.getAmount(),
                    t.getFee(),
                    t.getType() != null ? t.getType().name() : null,
                    t.getStatus() != null ? t.getStatus().name() : null,
                    t.getCreatedAt()
                );
                received.add(simple);
            }
        }
        return new UserByIdResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getCountry(),
                user.getIdNumber(),
                user.getRole() != null ? user.getRole().name() : null,
                user.getStatus(),
                user.getSubscriptionType(),
                balance,
                depot,
                retrait,
                transfer,
                received,
                history
        );
    }

    // Backward-compatible overload: previous signature used in older compiled classes
    public static UserByIdResponse toDto(Users user, BigDecimal balance, List<Transaction> sentTransaction, List<Transaction> receivedTransaction) {
        // Delegate to the new method with an empty history to avoid NoSuchMethodError at runtime
        return toDto(user, balance, sentTransaction, receivedTransaction, new ArrayList<TransactionHistoryItem>());
    }
}
