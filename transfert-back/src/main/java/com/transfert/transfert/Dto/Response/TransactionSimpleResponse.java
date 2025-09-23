package com.transfert.transfert.Dto.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionSimpleResponse(
    Long id,
    String transactionNumber,
    BigDecimal amount,
    BigDecimal fee,
    String type,
    String status,
    LocalDateTime createdAt
) {}
