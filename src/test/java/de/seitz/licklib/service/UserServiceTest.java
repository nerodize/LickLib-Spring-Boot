package de.seitz.licklib.service;

import de.seitz.licklib.dto.user.UserRequestDTO;
import de.seitz.licklib.dto.user.UserResponseDTO;
import de.seitz.licklib.model.User;
import de.seitz.licklib.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername("nero");
        testUser.setEmail("nero@example.com");
    }

    @Test
    @DisplayName("createUser: wirft CONFLICT wenn Email bereits existiert")
    void createUser_emailAlreadyExists_throwsConflict() {
        UserRequestDTO dto = new UserRequestDTO("nero", "nero@example.com");
        when(userRepository.existsByEmail("nero@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("email already in use");
    }

    @Test
    @DisplayName("createUser: speichert neuen User wenn Email frei ist")
    void createUser_validData_createsUser() {
        UserRequestDTO dto = new UserRequestDTO("nero", "nero@example.com");
        when(userRepository.existsByEmail("nero@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponseDTO result = userService.createUser(dto);

        assertThat(result.username()).isEqualTo("nero");
        assertThat(result.email()).isEqualTo("nero@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("deleteUser: gibt DTO zurück und ruft delete auf")
    void deleteUser_exists_deletesAndReturnsDTO() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        UserResponseDTO result = userService.deleteUser(userId);

        assertThat(result.username()).isEqualTo("nero");
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    @DisplayName("deleteUser: wirft Exception wenn nicht gefunden")
    void deleteUser_notFound_throwsException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(EntityNotFoundException.class);
    }
}