package com.transfert.transfert.Dto.Requests;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String phoneOrEmail;
}
