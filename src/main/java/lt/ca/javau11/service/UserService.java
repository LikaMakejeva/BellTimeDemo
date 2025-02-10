package lt.ca.javau11.service;

import lt.ca.javau11.entity.User;
import lt.ca.javau11.repository.UserRepository;
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

    /**
     * Constructor for dependency injection.
     *
     * @param userRepository the repository for user entities.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users.
     *
     * @return list of all users.
     */
    public List<User> findAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        log.info("Found {} users", users.size());
        return users;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user.
     * @return the found user, or null if not found.
     */
    public User findUserById(Long id) {
        log.info("Searching for user with ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.warn("User with ID {} not found", id);
        }
        return user.orElse(null);
    }

    /**
     * Saves a new user or updates an existing user.
     *
     * @param user the user to save.
     * @return the saved user.
     */
    public User saveUser(User user) {
        if (user == null) {
            log.error("Attempt to save null user");
            throw new IllegalArgumentException("User cannot be null");
        }
        log.info("Saving user: {}", user);
        return userRepository.save(user);
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
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.warn("User with username {} not found", username);
        }
        return user;
    }
}
