package com.transfert.transfert.Services;

import com.transfert.transfert.Dto.Requests.ForgotPasswordRequest;
import com.transfert.transfert.Dto.Requests.ValidateCodeRequest;
import com.transfert.transfert.Dto.Requests.ResetPasswordRequest;
import com.transfert.transfert.Entities.Users;
import com.transfert.transfert.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final Map<String, String> codeStore = new HashMap<>(); // phone/email -> code

    public ResponseEntity<?> sendResetCode(ForgotPasswordRequest request) {
        String phoneOrEmail = request.getPhoneOrEmail();
        Optional<Users> userOpt = userRepository.findByPhoneNumber(phoneOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(phoneOrEmail);
        }
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Utilisateur non trouvé"));
        }
        Users user = userOpt.get();
        String code = String.valueOf(100000 + new Random().nextInt(900000));
        codeStore.put(phoneOrEmail, code);
        // vrm g la flemme de styliser avec thymleaf etc...
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            emailService.sendEmail(user.getEmail(), "Code de réinitialisation", "Votre code : " + code);
        }
        return ResponseEntity.ok(Map.of("message", "Code envoyé par email"));
    }

    public ResponseEntity<?> validateCode(ValidateCodeRequest request) {
        String stored = codeStore.get(request.getPhoneOrEmail());
        if (stored != null && stored.equals(request.getCode())) {
            return ResponseEntity.ok(Map.of("message", "Code valide"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Code invalide"));
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest request) {
        String phoneOrEmail = request.getPhoneOrEmail();
        Optional<Users> userOpt = userRepository.findByPhoneNumber(phoneOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(phoneOrEmail);
        }
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Utilisateur non trouvé"));
        }
        String stored = codeStore.get(phoneOrEmail);
        if (stored == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Code non envoyé ou expiré"));
        }
        Users user = userOpt.get();
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        codeStore.remove(phoneOrEmail);
        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé"));
    }
}
