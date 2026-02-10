package de.seitz.licklib.dto.track;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record TrackCreateDTO(
        @NotBlank(message = "title must not be empty")
        @Size(min = 5, max = 100)
        String title,

        @Size(min = 10, max = 200)
        String description,

        @NotBlank(message = "artist has to exist")
        String artist,
        int size,

        @Positive(message = "duration must be positive")
        int duration,

        @NotNull(message = "creator ID is missing")
        UUID creatorId
) {}