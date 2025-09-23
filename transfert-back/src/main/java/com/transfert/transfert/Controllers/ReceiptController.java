package com.transfert.transfert.Controllers;

import com.transfert.transfert.Repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptRepository receiptRepository;

    @GetMapping("/{receiptNumber}")
    public ResponseEntity<?> getByReceiptNumber(@PathVariable String receiptNumber) {
        return receiptRepository.findByReceiptNumber(receiptNumber)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-transaction/{transactionNumber}")
    public ResponseEntity<?> getByTransactionNumber(@PathVariable String transactionNumber) {
        return receiptRepository.findByTransaction_TransactionNumber(transactionNumber)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<?> getAllByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(receiptRepository.findAllByUserId(userId));
    }
}
