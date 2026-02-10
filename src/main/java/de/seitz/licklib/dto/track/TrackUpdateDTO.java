package de.seitz.licklib.dto.track;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TrackUpdateDTO (
    @NotBlank(message = "title must not be empty")
    @Size(min = 5, max = 100)
    String title,

    @Size(min = 10, max = 200)
    String description,
    @NotBlank(message = "the track has to be from someone")
    String artist
){}