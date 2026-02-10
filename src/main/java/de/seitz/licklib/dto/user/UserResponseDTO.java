package de.seitz.licklib.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserResponseDTO(
        @NotNull
        UUID id,

        @NotBlank
        String username,

        @NotBlank
        @Email
        String email
){}