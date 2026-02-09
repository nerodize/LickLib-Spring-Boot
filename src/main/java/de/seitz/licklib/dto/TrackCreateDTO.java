package de.seitz.licklib.dto;

import java.util.UUID;

public record TrackCreateDTO(
        String title,
        String description,
        String artist,
        int size,
        int duration,
        UUID creatorId
) {}