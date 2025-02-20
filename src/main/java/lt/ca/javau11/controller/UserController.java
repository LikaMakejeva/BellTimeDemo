package lt.ca.javau11.controller;

import lt.ca.javau11.entity.User;
import lt.ca.javau11.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 * Provides endpoints for user-related operations.
 */
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    /**
     * Constructor for dependency injection.
     *
     * @param userService the service for managing users.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     *
     * @return a ResponseEntity containing the list of all users.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Fetching all users");
        return ResponseEntity.ok(userService.findAllUsers());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user.
     * @return a ResponseEntity containing the found user, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        return ResponseEntity.ok(userService.findUserById(id));
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username to search for.
     * @return a ResponseEntity containing the user if found, or 404 if not found.
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        log.info("Fetching user with username: {}", username);
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create.
     * @return a ResponseEntity containing the created user.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Creating new user: {}", user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    /**
     * Updates an existing user.
     *
     * @param id the ID of the user to update.
     * @param user the user details to update.
     * @return a ResponseEntity containing the updated user.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        log.info("Updating user with ID: {}", id);
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     * @return a ResponseEntity with 204 No Content status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Sets a new password for the user.
     *
     * @param id the ID of the user.
     * @param newPassword the new password.
     * @return a ResponseEntity containing the updated user.
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<User> setUserPassword(@PathVariable Long id, @RequestParam String newPassword) {
        log.info("Setting new password for user with ID: {}", id);
        return ResponseEntity.ok(userService.setUserPassword(id, newPassword));
    }
}
