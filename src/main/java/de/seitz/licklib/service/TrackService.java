package de.seitz.licklib.service;

import de.seitz.licklib.dto.track.TrackCreateDTO;
import de.seitz.licklib.dto.track.TrackResponseDTO;
import de.seitz.licklib.dto.track.TrackUpdateDTO;
import de.seitz.licklib.model.Track;
import de.seitz.licklib.model.User;
import de.seitz.licklib.repository.TrackRepository;

import de.seitz.licklib.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// In dem Service kein Lombok => better practice mit Constructor Injection (scheinbar nicht)
@Service
public class TrackService {
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TrackService(TrackRepository trackRepository, UserRepository userRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Track> findAllTracks() {
        return trackRepository.findAll();
    }

    public Optional<TrackResponseDTO> findTrackById(UUID id) {
        return trackRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    // Die Methode muss man sich wahrscheinlich nochmal anschauen → wie sieht es aus bei nicht Trivialfunktionen
    public List<TrackResponseDTO> findAllTracksByUsername(String username) {
        return trackRepository.findByCreatorUsername(username)
                .stream()
                .map(this::mapToResponseDTO) // Deine Mapping-Logik
                .toList();
    }


    @Transactional
    public TrackResponseDTO uploadTrack(TrackCreateDTO inputTrack) {
        User user = userRepository.findById(inputTrack.creatorId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Track track = new Track();
        track.setTitle(inputTrack.title());
        track.setDescription(inputTrack.description());
        track.setArtist(inputTrack.artist());
        track.setSize(inputTrack.size());
        track.setDuration(inputTrack.duration());
        track.setCreator(user);

        trackRepository.save(track);

        kafkaTemplate.send("track.uploaded", track.getId().toString());
        return mapToResponseDTO(track);
    }

    @Transactional
    public void updateTrack(UUID id, TrackUpdateDTO trackData) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Track not found"));

        if (trackData.title() != null)
            track.setTitle(trackData.title());

        if (trackData.description() != null)
            track.setDescription(trackData.description());

        if (trackData.artist() != null)
            track.setArtist(trackData.artist());

        mapToResponseDTO(trackRepository.save(track));
    }

    public void deleteTrack(UUID id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Track not found" + id));

        trackRepository.delete(track);
    }

    public TrackResponseDTO mapToResponseDTO(Track track) {
        return new TrackResponseDTO(
                track.getId(),
                track.getTitle(),
                track.getArtist(),
                track.getCreator() != null ? track.getCreator().getUsername() : "unknown",
                track.getDescription(),
                track.getDuration()
        );
    }
}

