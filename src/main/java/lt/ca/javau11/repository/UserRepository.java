package lt.ca.javau11.repository;

import lt.ca.javau11.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * Extends {@link JpaRepository} to provide basic CRUD operations and query methods.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for.
     * @return an Optional containing the User if found, or empty if not.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user with the given username exists.
     *
     * @param username the username to check.
     * @return {@code true} if the user exists, otherwise {@code false}.
     */
    boolean existsByUsername(String username);
}
