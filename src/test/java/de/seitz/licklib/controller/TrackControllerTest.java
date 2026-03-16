package de.seitz.licklib.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.seitz.licklib.dto.track.TrackCreateDTO;
import de.seitz.licklib.dto.track.TrackResponseDTO;
import de.seitz.licklib.service.TrackService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrackController.class) // Nur TrackController wird geladen
class TrackControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simuliert HTTP-Requests ohne echten Server

    @MockitoBean // Spring-managed Mock (statt @Mock)
    private TrackService trackService;

    @Autowired
    private ObjectMapper objectMapper; // JSON-Serialisierung

    private final UUID trackId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();

    @Test
    void getTrackById_found_returns200() throws Exception {
        TrackResponseDTO dto = new TrackResponseDTO(
                trackId, "E-Minor Lick", "Paul Gilbert", "nero", "Fast run", 12
        );
        when(trackService.findTrackById(trackId)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/tracks/{id}", trackId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("E-Minor Lick"))
                .andExpect(jsonPath("$.artist").value("Paul Gilbert"))
                .andExpect(jsonPath("$.duration").value(12));
    }

    @Test
    void getTrackById_notFound_returns404() throws Exception {
        when(trackService.findTrackById(trackId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tracks/{id}", trackId))
                .andExpect(status().isNotFound());
    }

    @Test
    void uploadTrack_validInput_returns201() throws Exception {
        TrackCreateDTO createDTO = new TrackCreateDTO(
                "E-Minor Lick", "Fast picking run desc", "Paul Gilbert", 1024, 12, userId
        );
        TrackResponseDTO responseDTO = new TrackResponseDTO(
                trackId, "E-Minor Lick", "Paul Gilbert", "nero", "Fast picking run desc", 12
        );
        when(trackService.uploadTrack(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/tracks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("E-Minor Lick"));
    }

    @Test
    void deleteTrack_exists_returns204() throws Exception {
        doNothing().when(trackService).deleteTrack(trackId);

        mockMvc.perform(delete("/api/tracks/{id}", trackId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllTracks_returnsListOf2() throws Exception {
        // findAllTracks() gibt List<Track> zurück, mapToResponseDTO wird im Controller aufgerufen
        // → hier musst du beide mocken
        de.seitz.licklib.model.Track t1 = new de.seitz.licklib.model.Track();
        TrackResponseDTO dto1 = new TrackResponseDTO(trackId, "Lick 1", "Artist 1", "nero", "desc", 10);
        TrackResponseDTO dto2 = new TrackResponseDTO(UUID.randomUUID(), "Lick 2", "Artist 2", "nero", "desc", 20);

        when(trackService.findAllTracks()).thenReturn(List.of(t1));
        when(trackService.mapToResponseDTO(any())).thenReturn(dto1);

        mockMvc.perform(get("/api/tracks/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}