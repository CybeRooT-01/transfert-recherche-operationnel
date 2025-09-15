package com.transfert.transfert.Dto.Response;

import com.transfert.transfert.Enums.SubscriptionType;
import com.transfert.transfert.Enums.UserStatus;

public record UsersResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String Country,
        String idNumber,
        String role,
        UserStatus status,
        SubscriptionType subscriptionType
) {
}
