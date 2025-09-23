package com.transfert.transfert.Controllers;

import com.transfert.transfert.Dto.Requests.ForgotPasswordRequest;
import com.transfert.transfert.Dto.Requests.ValidateCodeRequest;
import com.transfert.transfert.Dto.Requests.ResetPasswordRequest;
import com.transfert.transfert.Services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/password")
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordService passwordService;

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return passwordService.sendResetCode(request);
    }

    @PostMapping("/validate-code")
    public ResponseEntity<?> validateCode(@RequestBody ValidateCodeRequest request) {
        return passwordService.validateCode(request);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        return passwordService.resetPassword(request);
    }
}
