package com.transfert.transfert.Controllers;

import com.transfert.transfert.Dto.Requests.DepositRequest;
import com.transfert.transfert.Dto.Requests.TransferRequest;
import com.transfert.transfert.Dto.Requests.TransactionRequest;
import com.transfert.transfert.Dto.Requests.WithdrawRequest;
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

    //cette fonction est un legacy code askip le gar du frontend veux les fonctions separer
    @PostMapping("")
    public ResponseEntity<?> handle(@Valid @RequestBody TransactionRequest transactionRequest){
        return transactionService.handleTransaction(transactionRequest);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@Valid @RequestBody DepositRequest request){
        return transactionService.deposit(request);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@Valid @RequestBody WithdrawRequest request){
        return transactionService.withdraw(request);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest request){
        return transactionService.transfer(request);
    }
}
