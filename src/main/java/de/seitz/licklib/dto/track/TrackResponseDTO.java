package de.seitz.licklib.dto.track;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record TrackResponseDTO(
        @NotNull(message = "track ID is missing")
        UUID id,

        @NotBlank(message = "title must not be empty")
        @Size(max = 100)
        String title,

        @NotBlank(message = "artist must not be empty")
        @Size(max = 100)
        String artist,

        @NotBlank(message = "username must not be empty")
        String username,

        @Size(max = 500)
        String description,

        @Positive(message = "duration must be greater than zero")
        int duration
){}