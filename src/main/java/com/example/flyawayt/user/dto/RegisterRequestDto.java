package com.example.flyawayt.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequestDto {

    @NotBlank
    @Pattern(regexp = ".*[A-Z].*", message = "debe tener al menos una letra mayúscula")
    private String firstName;

    @NotBlank
    @Pattern(regexp = ".*[A-Z].*", message = "debe tener al menos una letra mayúscula")
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$", message = "debe tener mínimo 8 caracteres, al menos una letra y un número")
    private String password;
}
