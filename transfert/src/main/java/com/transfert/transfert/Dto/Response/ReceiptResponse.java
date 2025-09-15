package com.transfert.transfert.Dto.Response;

import com.transfert.transfert.Entities.Receipt;
import com.transfert.transfert.Entities.Transaction;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceiptResponse {
    private Double amountCredited; // Pour dépôt
    private Double amountDebited;  // Pour retrait et transfert
    private String success;
    private Double newBalance; // Pour dépôt et retrait
    private Double newSenderBalance; 
    private String receiptNumber;
    private String pdfUrl;
    private Boolean emailSent;
    private Boolean smsSent;
    public static ReceiptResponse fromTransactionAndReceipt(Transaction transaction, Receipt receipt, String successType, Double balance, Double senderBalance) {
        return ReceiptResponse.builder()
                .amountCredited(transaction.getType().name().equals("DEPOSIT") ? transaction.getTotalAmount().doubleValue() : null)
                .amountDebited((transaction.getType().name().equals("WITHDRAWAL") || transaction.getType().name().equals("TRANSFER")) ? transaction.getTotalAmount().doubleValue() : null)
                .success(successType)
                .newBalance(balance)
                .newSenderBalance(senderBalance)
                .receiptNumber(receipt.getReceiptNumber())
                .pdfUrl(receipt.getPdfUrl())
                .emailSent(receipt.isEmailSent())
                .smsSent(receipt.isSmsSent())
                .build();
    }
}