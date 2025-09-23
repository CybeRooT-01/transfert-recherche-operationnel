package com.transfert.transfert.Dto.Response;

import com.transfert.transfert.Entities.Transaction;
import com.transfert.transfert.Enums.SubscriptionType;
import com.transfert.transfert.Enums.UserStatus;

import java.math.BigDecimal;
import java.util.List;

import java.util.List;
public record UserByIdResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String Country,
        String idNumber,
        String role,
        UserStatus status,
        SubscriptionType subscriptionType,
        BigDecimal balance,
        List<TransactionSimpleResponse> depot,
        List<TransactionSimpleResponse> retrait,
        List<TransactionSimpleResponse> transfer,
        List<TransactionSimpleResponse> receivedTransaction,
        List<TransactionHistoryItem> history
) {}
