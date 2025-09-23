package com.transfert.transfert.Dto.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionHistoryItem(
        String type,
        BigDecimal amount,
        LocalDateTime date,
        String status,
        String number
) {
}
