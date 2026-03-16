package de.seitz.licklib.service;

import de.seitz.licklib.dto.track.TrackCreateDTO;
import de.seitz.licklib.dto.track.TrackResponseDTO;
import de.seitz.licklib.dto.track.TrackUpdateDTO;
import de.seitz.licklib.model.Track;
import de.seitz.licklib.model.User;
import de.seitz.licklib.repository.TrackRepository;
import de.seitz.licklib.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TrackServiceTest {

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TrackService trackService;

    private User testUser;
    private Track testTrack;
    private UUID trackId;
    private UUID userId;

    @BeforeEach // läuft vor jedem Test
    void setup() {
        userId = UUID.randomUUID();
        trackId = UUID.randomUUID();

        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername("nero");
        testUser.setEmail("nero@example.com");

        testTrack = new Track();
        testTrack.setId(trackId);
        testTrack.setTitle("chronostasis");
        testTrack.setArtist("Josh Dela");
        testTrack.setDescription("insanely proggy");
        testTrack.setDuration(240);
        testTrack.setSize(888);
        testTrack.setCreator(testUser);
    }

    @Test
    @DisplayName("findTrackById: returns DTO () if track exists") // naming convention?
    void findTrackById_exists_returnsDTO() {
        // ARRANGE: was soll zurückgegeben werden vom Mock
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(testTrack));

        // ACT: Die eigentliche Methode aufrufen
        Optional<TrackResponseDTO> result = trackService.findTrackById(trackId);

        // ASSERT: Das Ergebnis prüfen
        assertThat(result).isPresent();
        assertThat(result.get().title()).isEqualTo(testTrack.getTitle()); // nicht besser so als hardcoded value
        assertThat(result.get().artist()).isEqualTo(testTrack.getArtist());
        assertThat(result.get().duration()).isEqualTo(testTrack.getDuration());
        assertThat(result.get().username()).isEqualTo(testUser.getUsername());

        verify(trackRepository, times(1)).findById(trackId);
    }

    @Test
    @DisplayName("findTrackById: gibt empty zurück wenn Track nicht existiert")
    void findTrackById_notFound_returnsEmpty() {
        when(trackRepository.findById(trackId)).thenReturn(Optional.empty());

        Optional<TrackResponseDTO> result = trackService.findTrackById(trackId);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("uploadTrack: speichert Track und gibt DTO zurück")
    void uploadTrack_validInput_savesAndReturnsDTO() {
        TrackCreateDTO createDTO = new TrackCreateDTO(
                "chronostasis",
                "insanely proggy",
                "josh dela",
                888,
                240,
                userId
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        // save() gibt den übergebenen Track zurück – wir simulieren das
        when(trackRepository.save(any(Track.class))).thenReturn(testTrack);

        TrackResponseDTO result = trackService.uploadTrack(createDTO);

        assertThat(result.title()).isEqualTo("chronostasis");
        assertThat(result.username()).isEqualTo("nero");
        verify(trackRepository, times(1)).save(any(Track.class));
    }


    @Test
    @DisplayName("uploadTrack: wirft Exception wenn User nicht gefunden")
    void uploadTrack_userNotFound_throwsException() {
        TrackCreateDTO createDTO = new TrackCreateDTO(
                "chronostasis", "insanely proggy", "josh dela", 888, 240, userId
        );

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // assertThatThrownBy prüft ob eine Exception geworfen wird
        assertThatThrownBy(() -> trackService.uploadTrack(createDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("updateTrack: aktualisiert nur nicht-null Felder")
    void updateTrack_partialUpdate_onlyUpdatesProvidedFields() {
        TrackUpdateDTO updateDTO = new TrackUpdateDTO(
                "New Title",
                null, // description bleibt unverändert
                "New Artist"
        );

        when(trackRepository.findById(trackId)).thenReturn(Optional.of(testTrack));
        when(trackRepository.save(any(Track.class))).thenReturn(testTrack);

        trackService.updateTrack(trackId, updateDTO);

        // Prüfen, ob die Felder am Track-Objekt wirklich gesetzt wurden
        assertThat(testTrack.getTitle()).isEqualTo("New Title");
        assertThat(testTrack.getArtist()).isEqualTo("New Artist");
        assertThat(testTrack.getDescription()).isEqualTo("insanely proggy"); // unverändert
    }


    @Test
    @DisplayName("updateTrack: wirft Exception wenn Track nicht existiert")
    void updateTrack_notFound_throwsException() {
        TrackUpdateDTO updateDTO = new TrackUpdateDTO("Title", "desc", "Artist");
        when(trackRepository.findById(trackId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trackService.updateTrack(trackId, updateDTO))
                .isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    @DisplayName("deleteTrack: wirft Exception wenn Track nicht gefunden")
    void deleteTrack_notFound_throwsException() {
        when(trackRepository.findById(trackId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trackService.deleteTrack(trackId))
                .isInstanceOf(EntityNotFoundException.class);

        // Sicherstellen dass delete() NIE aufgerufen wurde
        verify(trackRepository, never()).delete(any());
    }

    // --- findAllTracksByUsername ---

    @Test
    @DisplayName("findAllTracksByUsername: gibt Liste von DTOs zurück")
    void findAllTracksByUsername_returnsListOfDTOs() {
        Track track2 = new Track();
        track2.setId(UUID.randomUUID());
        track2.setTitle("Blues Shuffle");
        track2.setArtist("SRV");
        track2.setCreator(testUser);
        track2.setDuration(45);
        track2.setSize(2048);

        when(trackRepository.findByCreatorUsername("nero"))
                .thenReturn(List.of(testTrack, track2));

        List<TrackResponseDTO> result = trackService.findAllTracksByUsername("nero");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(TrackResponseDTO::username)
                .containsOnly("nero"); // alle gehören "nero"
    }
}
