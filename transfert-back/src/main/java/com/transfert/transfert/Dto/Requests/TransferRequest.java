package com.transfert.transfert.Dto.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "le montant est obligatoire")
        BigDecimal amount,

        @NotNull(message = "L'utilisateur est obligatoire")
        Long userId,

        @NotBlank(message = "Le numéro du destinataire est obligatoire")
        String recipientNumber
) {
}
