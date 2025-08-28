package com.transfert.transfert.Services;

import com.transfert.transfert.Dto.Response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final AuthService authService;

    public ResponseEntity<?> refreshToken(String oldToken) {
        if (oldToken == null || oldToken.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le token est obligatoire"));
        }
        String username;
        try {
            username = authService.getUsernameFromToken(oldToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token invalide ou expiré"));
        }
        var userOpt = authService.getUserByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Utilisateur non trouvé"));
        }
        var newToken = authService.generateToken(userOpt.get());
        return ResponseEntity.ok(new LoginResponse(newToken, username, userOpt.get().getRole().name()));

    }

}
