package com.transfert.transfert.Dto.Requests;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawRequest(
        @NotNull(message = "le montant est obligatoire")
        BigDecimal amount,

        @NotNull(message = "L'utilisateur est obligatoire")
        Long userId
) {
}
