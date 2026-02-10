package de.seitz.licklib.service;

import de.seitz.licklib.dto.user.UserRequestDTO;
import de.seitz.licklib.dto.user.UserResponseDTO;
import de.seitz.licklib.model.User;
import de.seitz.licklib.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll()
                .stream().map(this::mapToResponseDTO)
                .toList();
    }


    // so schöner mit DTO -> allerdings kinda useless
    // hier außerdem Optional als ret val von findById => kein stream nötig, Optional hat das ebenfalls implementiert
    public Optional<UserResponseDTO> findUserByID(UUID id) {
        return userRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    public Optional<UserResponseDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::mapToResponseDTO);
    }


    // User im param könnte Probleme machen evevntuell
    @Transactional
    public void updateUser(UUID id, UserRequestDTO userUpdate) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found"));
        // das sollte ein wenig besser strukturiert sein, komischer code
        if (userUpdate.username() != null ) {
            user.setUsername(userUpdate.username());
        }

        if (userUpdate.email() != null ) {
            user.setEmail(userUpdate.email());
        }

        mapToResponseDTO(userRepository.save(user));
    }

    @Transactional
    public UserResponseDTO deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        UserResponseDTO response = mapToResponseDTO(user);
        // delete gibt nichts zurück offensichtlich
        userRepository.delete(user);

        return response;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already in use");
        }

        User newUser = new User();
        newUser.setUsername(userRequest.username());
        newUser.setEmail(userRequest.email());

        return mapToResponseDTO(userRepository.save(newUser));
    }


    // Hilfsmethode für das Mapping
    private UserResponseDTO mapToResponseDTO(User entity) {
        return new UserResponseDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail()
        );
    }
}