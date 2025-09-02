package com.transfert.transfert.Mappers;

import com.transfert.transfert.Dto.Response.UsersResponse;
import com.transfert.transfert.Entities.Users;

public class UsersMapper {
    public static UsersResponse toDto(Users user) {
        if (user == null) return null;
        return new UsersResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getCountry(),
                user.getPhotoUrl(),
                user.getIdNumber(),
                user.getIdPhotoUrl(),
                user.getStatus(),
                user.getRole() != null ? user.getRole().name() : null,
                user.getSubscriptionType()
        );
    }
}
