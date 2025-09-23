package com.transfert.transfert.Controllers;

import com.transfert.transfert.Services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountservice;

    @PostMapping("/verify-pin")
    public ResponseEntity<?> verifyPin(@RequestBody Map<String, String> body){
        String pin = body.get("pin");
        return this.accountservice.verifyPin(pin);
    }

    @PutMapping("/update-subscription/{id}")
    public ResponseEntity<?> updateSubscription(@PathVariable("id") Long accountId) {
        return this.accountservice.updateSubscription(accountId);
    }
}
