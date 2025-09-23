package com.transfert.transfert.Dto.Requests;

public record LoginRequest(
        String phoneNumber,
        String password
) {}