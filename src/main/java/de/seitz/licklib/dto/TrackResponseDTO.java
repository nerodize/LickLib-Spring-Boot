package de.seitz.licklib.dto;

import java.util.*;

public record TrackResponseDTO(
    UUID id,
    String title,
    String artist,
    String username,
    String description,
    int duration
){}