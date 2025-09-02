package com.transfert.transfert.Services;

import com.transfert.transfert.Dto.Requests.LoginRequest;
import com.transfert.transfert.Dto.Requests.RegisterUserRequest;
import com.transfert.transfert.Dto.Response.ErrorResponse;
import com.transfert.transfert.Dto.Response.LoginResponse;
import com.transfert.transfert.Entities.Account;
import com.transfert.transfert.Entities.Users;
import com.transfert.transfert.Enums.AccountStatus;
import com.transfert.transfert.Enums.UserRole;
import com.transfert.transfert.Repository.AccountRepository;
import com.transfert.transfert.Repository.UserRepository;
import com.transfert.transfert.Security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final AccountRepository accountRepository;
    private  final JwtService jwtService;
    private String generateAccountNumber() {
        long timestamp = System.currentTimeMillis();
        int randomSuffix = (int) (Math.random() * 900) + 100;
        return "ACC-SN" + timestamp + randomSuffix;
    }

    private String generatePinHash(String pin) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(pin);
    }

    @Transactional
    public LoginResponse register(RegisterUserRequest request) {
        var user = new Users();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        user.setCountry(request.Country());
        user.setIdNumber(request.idNumber());
        user.setRole(UserRole.USER);
        userRepository.save(user);
        //creer le account relatif au user
        var account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency("XOF");
        account.setDailyLimit(new BigDecimal("50000"));
        account.setMonthlyLimit(new BigDecimal("200000"));
        account.setPinHash(generatePinHash(request.pinHash()));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);
        accountRepository.save(account);

        var token = jwtService.generateToken(user);
        return new LoginResponse(token, user.getUsername(), user.getRole().name());
    }

    public ResponseEntity<?> login(LoginRequest request) {
        System.out.println(request.phoneNumber());
        var user = userRepository.findByPhoneNumber(request.phoneNumber());

        if (user.isEmpty() || !passwordEncoder.matches(request.password(), user.get().getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Login ou mot de passe incorrect"));
        }
        var userExist = user.get();
        var token = jwtService.generateToken(userExist);
        Account account = accountRepository.findByUser(user);
        if(account == null || account.getStatus() != AccountStatus.ACTIVE) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Compte blqué veuillez contacter le service client"));
        }

        return ResponseEntity.ok(new LoginResponse(token, userExist.getUsername(), userExist.getRole().name()));
    }

    public String getUsernameFromToken(String token) {
        return jwtService.extractUsername(token);
    }

    public Optional<Users> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String generateToken(Users user) {
        return jwtService.generateToken(user);
    }

    public Users getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            throw new RuntimeException("Utilisateur non authentifié");
        }
    }
}
