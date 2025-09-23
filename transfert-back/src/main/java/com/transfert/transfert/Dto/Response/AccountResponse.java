package com.transfert.transfert.Dto.Response;

import com.transfert.transfert.Enums.SubscriptionType;

import java.math.BigDecimal;

public record AccountResponse(
        String accountNumber,
        BigDecimal balance,
        String currency,
        SubscriptionType subscriptionType,
        BigDecimal dailyLimit,
        BigDecimal monthlyLimit
) {
}
