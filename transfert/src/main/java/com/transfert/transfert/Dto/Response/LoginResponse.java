package com.transfert.transfert.Dto.Response;

public record LoginResponse(
        String token,
        String username,
        String role
) {
}

