package de.seitz.licklib.controller;


import de.seitz.licklib.dto.TrackCreateDTO;
import de.seitz.licklib.dto.TrackResponseDTO;
import de.seitz.licklib.dto.UserResponseDTO;
import de.seitz.licklib.model.Track;
import de.seitz.licklib.model.User;
import de.seitz.licklib.service.TrackService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {
    public final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @RequestMapping("/{id}")
    public ResponseEntity<TrackResponseDTO> findTrackById(@PathVariable UUID id) {
        return trackService.findTrackById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping("/")
    public ResponseEntity<List<TrackResponseDTO>> findAllTracks() {
        List<TrackResponseDTO> tracks = trackService.findAllTracks().stream()
                .map(trackService::mapToResponseDTO)
                .toList();
        return ResponseEntity.ok(tracks);
    }

    @RequestMapping("/search/{username}")
    public ResponseEntity<List<TrackResponseDTO>> findTrackByUsername(@PathVariable String username) {
        List<TrackResponseDTO> tracks = trackService.findAllTracksByUsername(username).stream()
                .toList();

        return ResponseEntity.ok(tracks);
    }

    @PostMapping("/")
    public ResponseEntity<TrackResponseDTO> uploadTrack(@RequestBody TrackCreateDTO track) {
        TrackResponseDTO savedTrack = trackService.uploadTrack(track);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTrack);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TrackResponseDTO> updateTrack(@PathVariable UUID id, @RequestBody TrackResponseDTO track) {
        trackService.updateTrack(id, track);
        return ResponseEntity.ok().build();
    }

     @DeleteMapping("/{id}")
    public ResponseEntity<TrackResponseDTO> deleteTrack(@PathVariable UUID id) {
        trackService.deleteTrack(id);
        return ResponseEntity.noContent().build();
    }
}