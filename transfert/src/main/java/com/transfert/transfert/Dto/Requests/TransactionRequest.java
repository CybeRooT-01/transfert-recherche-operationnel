package com.transfert.transfert.Dto.Requests;

import com.transfert.transfert.Annotation.ExistInDatabase;
import com.transfert.transfert.Annotation.ExistInEnum;
import com.transfert.transfert.Entities.Account;
import com.transfert.transfert.Enums.TransactionType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = "L'envoyeur est obligatoire ")
        @ExistInDatabase(entity = Account.class, message = "L'envoyeur doit exister")
        Long senderAccountId,

        @NotNull(message = "Le destinataire est obligatoire")
        @ExistInDatabase(entity = Account.class, message = "L'envoyeur doit exister")

        Long receiverAccountId,

        @NotNull(message = "le montant est obligatoire")
        BigDecimal amount,

        @NotNull(message = "Le type de transaction est obligatoire (DEPOSIT, WITHDRAWAL, TRANSFER")
        @ExistInEnum(enumClass = TransactionType.class, message = "Type de transaction invalide")
        TransactionType transactiontype

) {
}
