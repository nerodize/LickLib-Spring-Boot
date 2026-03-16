package de.seitz.licklib.repository;

import de.seitz.licklib.model.Track;
import de.seitz.licklib.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// hier Integration-Tests
@DataJpaTest          // Nur JPA-Schicht – kein Web-Layer
@Testcontainers       // Testcontainers-Integration
class TrackRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15") // könnte man supressen
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource // Überschreibt die DB-Config aus application.yml
    static void configureProperties(DynamicPropertyRegistry registry) {
        // postgres schien nicht am Laufen gewesen zu sein
        if(!postgres.isRunning()) {
            postgres.start();
        }
        // hierdurch werden die Einträge in der application.yml redundant bzw. ~"überschrieben"
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TestEntityManager entityManager; // Für Test-Setup direkt in DB schreiben

    @Test
    void findByCreatorUsername_returnsTracksOfUser() {
        // Arrange: User + Track direkt in die Test-DB persistieren
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        entityManager.persist(user);

        Track track = new Track();
        track.setTitle("Test Lick");
        track.setArtist("Test Artist");
        track.setDescription("Some description");
        track.setSize(100);
        track.setDuration(10);
        track.setCreator(user);
        // "merke" dir das Objekt
        entityManager.persist(track);
        // sofort in die DB schreiben
        entityManager.flush();

        // Act
        List<Track> result = trackRepository.findByCreatorUsername("testuser");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("Test Lick");
    }

    @Test
    void findByCreatorUsername_returnsEmptyList() {
        // Arrange: User + Track direkt in die Test-DB persistieren
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        entityManager.persist(user);

        Track track = new Track();
        track.setTitle("Test Lick");
        track.setArtist("Test Artist");
        track.setDescription("Some description");
        track.setSize(100);
        track.setDuration(10);
        track.setCreator(user);
        // "merke" dir das Objekt
        entityManager.persist(track);
        // sofort in die DB schreiben
        entityManager.flush();

        // Act
        List<Track> result = trackRepository.findByCreatorUsername("testuserNONE");

        // Assert
        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("findByCreatorUsername: sollte alle Tracks von einem User finden")
    void findByCreatorUsername_returnsTracks_whenUserExists() {
        // arrange
        User creator = new User();
        creator.setUsername("hero");
        creator.setEmail("hero@test.com");

        entityManager.persist(creator);

        Track track1 = new Track();
        track1.setTitle("E-Minor Pentatonic Lick");
        track1.setCreator(creator);

        Track track2 = new Track();
        track2.setTitle("Blues scale run");
        track2.setCreator(creator);

        entityManager.persist(track1);
        entityManager.persist(track2);

        entityManager.flush();
        entityManager.clear();

        // Act
        List<Track> result = trackRepository.findByCreatorUsername("hero");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Track::getTitle)
                .containsExactly("E-Minor Pentatonic Lick", "Blues scale run");
    }

}