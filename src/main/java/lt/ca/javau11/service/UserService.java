package lt.ca.javau11.service;

import lt.ca.javau11.entity.User;
import lt.ca.javau11.repository.UserRepository;
import lt.ca.javau11.validation.ValidationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing users.
 * Provides business logic for CRUD operations on user entities.
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private static final int MIN_PASSWORD_LENGTH = 6;
   

    /**
     * Constructor for dependency injection.
     *
     * @param userRepository the repository for user entities.
     * @param validationService the validation service.
     */
    public UserService(UserRepository userRepository, ValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    /**
     * Retrieves all users.
     *
     * @return list of all users.
     */
    public List<User> findAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user.
     * @return the found user.
     * @throws IllegalArgumentException if user is not found.
     */
    public User findUserById(Long id) {
        log.info("Searching for user with ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User with ID {} not found", id);
                    return new IllegalArgumentException("User not found with ID: " + id);
                });
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create.
     * @return the created user.
     */
    @Transactional
    public User createUser(User user) {
        log.info("Creating new user: {}", user);
        validationService.validateUser(user);
        validatePassword(user.getPassword());
        return userRepository.save(user);
    }

    /**
     * Updates an existing user.
     *
     * @param id   the ID of the user.
     * @param userDetails the updated user details.
     * @return the updated user.
     */
    @Transactional
    public User updateUser(Long id, User userDetails) {
        log.info("Updating user with ID: {}", id);
        User existingUser = findUserById(id);
        validationService.validateUser(userDetails);

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setPassword(userDetails.getPassword());
        existingUser.setRole(userDetails.getRole());
        existingUser.setActive(userDetails.isActive());

        return userRepository.save(existingUser);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.error("User with ID {} does not exist", id);
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        log.info("User with ID {} successfully deleted", id);
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for.
     * @return an Optional containing the found user, or empty if not found.
     */
    public Optional<User> findByUsername(String username) {
        log.info("Searching for user with username: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Sets a new password for the user.
     *
     * @param id the ID of the user.
     * @param newPassword the new password.
     * @return the updated user.
     */
    @Transactional
    public User setUserPassword(Long id, String newPassword) {
        log.info("Setting new password for user with ID: {}", id);
        validatePassword(newPassword);
        User user = findUserById(id);
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    /**
     * Validates that the password meets the minimum length requirement.
     *
     * @param password the password to validate.
     * @throws IllegalArgumentException if the password is too short.
     */
    private void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            log.error("Password is too short. Must be at least {} characters.", MIN_PASSWORD_LENGTH);
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
    }
}
