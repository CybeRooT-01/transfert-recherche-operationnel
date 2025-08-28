package com.transfert.transfert.Dto.Requests;

import com.transfert.transfert.Annotation.UniqueEmail;
import com.transfert.transfert.Annotation.UniqueIdNumber;
import com.transfert.transfert.Annotation.UniquePhoneNumber;
import com.transfert.transfert.Annotation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "Username obligatoire")
        @UniqueUsername(message = "Ce nom d'utilisateur est déjà pris")
        String username,

        @Email(message = "Email invalide")
        @UniqueEmail(message = "Cet email est déjà utilisé")
        String email,

        @NotBlank(message = "Mot de passe obligatoire")
        @Size(min = 8, message = "Mot de passe trop court (8 caractères min)")
        String password,

        @NotBlank (message = "Le prenom est obligatoire")
        String firstName,

        @NotBlank (message = "Le nom est obligatoire")
        String lastName,

        @NotBlank(message = "le numero de telephone est obligatoire")
        @UniquePhoneNumber(message = "Ce numero de telephone est deja pris")
        String phoneNumber,

        @NotBlank(message = "le pays est obligatoire")
        String Country,

        @NotBlank (message = "Le numero de Carte d'identité est obligatoire")
        @UniqueIdNumber(message = "Le numero de Carte d'identié est deja utilisé")
        String idNumber
) {}

