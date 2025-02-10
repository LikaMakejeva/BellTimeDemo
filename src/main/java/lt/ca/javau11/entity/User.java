package lt.ca.javau11.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a user entity in the system.
 * Contains user information such as username, password, email, active status, and role.
 * This entity is mapped to the "users" table in the database.
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_username", columnList = "username", unique = true),
    @Index(name = "idx_user_email", columnList = "email", unique = true)
})
public class User {
    private static final Logger log = LoggerFactory.getLogger(User.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username of the user, must be unique and between 3 and 50 characters.
     */
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Password of the user, must be at least 6 characters long.
     */
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    /**
     * Email address of the user, must be unique and in a valid format.
     */
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Indicates whether the user is active.
     */
    @Column(name = "active", nullable = false)
    private boolean active = true;

    /**
     * Role of the user in the system.
     */
    @Column(name = "role", nullable = false)
    private String role = "USER";

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Constructor with required parameters.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @param email    the email address of the user.
     */
    public User(String username, String password, String email) {
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
    }

    // Getters

    /**
     * Gets the unique ID of the user.
     *
     * @return the user ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the username.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the email address.
     *
     * @return the email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Checks if the user is active.
     *
     * @return true if the user is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Gets the role of the user.
     *
     * @return the user role.
     */
    public String getRole() {
        return role;
    }

    // Setters with validation and logging

    /**
     * Sets the unique ID of the user.
     *
     * @param id the user ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the username with validation.
     *
     * @param username the username to set.
     * @throws IllegalArgumentException if the username is null, empty, or not between 3 and 50 characters.
     */
    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            log.error("Attempt to set empty username");
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (username.length() < 3 || username.length() > 50) {
            log.error("Attempt to set invalid username length: {}", username.length());
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }
        log.debug("Setting username to '{}'", username.trim());
        this.username = username.trim();
    }

    /**
     * Sets the password with validation.
     *
     * @param password the password to set.
     * @throws IllegalArgumentException if the password is null, empty, or shorter than 6 characters.
     */
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            log.error("Attempt to set empty password");
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 6) {
            log.error("Attempt to set too short password");
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        log.debug("Setting password");
        this.password = password;
    }

    /**
     * Sets the email address with validation.
     *
     * @param email the email address to set.
     * @throws IllegalArgumentException if the email is null, empty, or invalid in format.
     */
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            log.error("Attempt to set empty email");
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            log.error("Attempt to set invalid email format: {}", email);
            throw new IllegalArgumentException("Invalid email format");
        }
        log.debug("Setting email to '{}'", email.trim().toLowerCase());
        this.email = email.trim().toLowerCase();
    }

    /**
     * Sets whether the user is active.
     *
     * @param active true if active, false otherwise.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Sets the user role.
     *
     * @param role the role to set.
     */
    public void setRole(String role) {
        if (role != null && !role.trim().isEmpty()) {
            log.debug("Setting role to '{}'", role.trim().toUpperCase());
            this.role = role.trim().toUpperCase();
        }
    }

    /**
     * Validates the user entity before persisting or updating.
     * Throws an exception if required fields are missing.
     */
    @PrePersist
    @PreUpdate
    protected void validateBeforeSave() {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username must be set");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password must be set");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must be set");
        }
        log.debug("User validation passed before save");
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string representing the user.
     */
    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', email='%s', active=%s, role='%s'}",
                id, username, email, active, role);
    }

    /**
     * Compares this user with another for equality.
     *
     * @param o the object to compare with.
     * @return true if the users are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    /**
     * Generates a hash code for the user.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
