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
        String photoUrl,
        String idNumber,
        String idPhotoUrl,
        UserStatus status,
        String role,
        SubscriptionType subscriptionType
) {
}
