package com.transfert.transfert.Services;

import com.transfert.transfert.Dto.Requests.LoginRequest;
import com.transfert.transfert.Dto.Requests.RegisterUserRequest;
import com.transfert.transfert.Dto.Response.ErrorLoginResponse;
import com.transfert.transfert.Dto.Response.LoginResponse;
import com.transfert.transfert.Entities.Users;
import com.transfert.transfert.Enums.UserRole;
import com.transfert.transfert.Repository.UserRepository;
import com.transfert.transfert.Security.JwtService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;

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
        var token = jwtService.generateToken(user);
        return new LoginResponse(token, user.getUsername(), user.getRole().name());
    }

    public ResponseEntity<?> login(LoginRequest request) {
        System.out.println(request.phoneNumber());
        var user = userRepository.findByPhoneNumber(request.phoneNumber());

        if (user.isEmpty() || !passwordEncoder.matches(request.password(), user.get().getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorLoginResponse("Login ou mot de passe incorrect"));
        }
        var userExist = user.get();
        var token = jwtService.generateToken(userExist);

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
}
