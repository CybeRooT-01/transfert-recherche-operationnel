package com.transfert.transfert.Dto.Response;

import com.transfert.transfert.Enums.SubscriptionType;
import com.transfert.transfert.Enums.UserStatus;

public record LoginResponse(
        Long id,
        String token,
        String username,
        String country,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        UserStatus status,
        SubscriptionType subscriptionType,
        String role,
        String rectoCni,
        String versoCni,
        String photo
) {
}
