package com.transfert.transfert.Dto.Requests;

import lombok.Data;

@Data
public class ValidateCodeRequest {
    private String phoneOrEmail;
    private String code;
}
