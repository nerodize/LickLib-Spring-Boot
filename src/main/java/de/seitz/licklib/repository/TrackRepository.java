package de.seitz.licklib.repository;

import de.seitz.licklib.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrackRepository extends JpaRepository<Track, UUID> {
    // wizardry: Spring Data JPA generiert automatisch die Implementierung dieser Methode basierend auf der Namenskonvention
    // es sucht nach Tracks, deren Creator (User) einen bestimmten Username hat
    List<Track> findByCreatorUsername(String username);
}