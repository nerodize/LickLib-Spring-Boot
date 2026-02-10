package de.seitz.licklib.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Username darf nicht leer sein")
        @Size(min = 3, max = 20)
        String username,

        @NotBlank(message = "Email darf nicht leer sein")
        @Email(message = "Ungültiges Email-Format")
        String email
){}