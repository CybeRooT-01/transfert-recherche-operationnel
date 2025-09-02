package com.transfert.transfert.Controllers;

import com.transfert.transfert.Dto.Requests.TransactionRequest;
import com.transfert.transfert.Services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private  final TransactionService transactionService;
    @PostMapping("")
    public ResponseEntity<?> deposit(@Valid @RequestBody TransactionRequest transactionRequest){
        return transactionService.handleTransaction(transactionRequest);
    }
}
