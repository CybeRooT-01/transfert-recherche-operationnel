package com.transfert.transfert.Dto.Requests;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String phoneOrEmail;
    private String newPassword;
}
