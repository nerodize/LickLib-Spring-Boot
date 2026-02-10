package de.seitz.licklib.controller;

import de.seitz.licklib.dto.user.UserRequestDTO;
import de.seitz.licklib.dto.user.UserResponseDTO;
import de.seitz.licklib.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        return userService.findUserByID(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping("/search/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping("/findAll")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UserRequestDTO user) {
        userService.updateUser(id, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO createData) {
        UserResponseDTO savedUser = userService.createUser(createData);

        // Wir bauen die URL zum neuen User (z.B. /api/users/uuid)
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.id())
                .toUri();

        return ResponseEntity.created(location).body(savedUser);
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable UUID id) {
        UserResponseDTO deletedUser = userService.deleteUser(id);
        if (deletedUser != null) {
            return ResponseEntity.ok(deletedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}