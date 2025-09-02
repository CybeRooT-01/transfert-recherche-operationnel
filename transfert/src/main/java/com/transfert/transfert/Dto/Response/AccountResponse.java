package com.transfert.transfert.Dto.Response;

import com.transfert.transfert.Entities.Transaction;
import com.transfert.transfert.Enums.SubscriptionType;

import java.math.BigDecimal;
import java.util.List;

public record AccountResponse(
        String accountNumber,
        BigDecimal balance,
        String currency,
        SubscriptionType subscriptionType,
        BigDecimal dailyLimit,
        BigDecimal monthlyLimit,
        List<Transaction> sentTransactions,
        List<Transaction> receivedTransactions
) {
}
