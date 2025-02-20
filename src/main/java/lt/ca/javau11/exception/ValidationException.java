package lt.ca.javau11.exception;

/**
 * Custom exception for validation errors.
 * Thrown when an entity fails validation.
 *
 * <p>
 * This exception extends {@link RuntimeException}, meaning it is an unchecked exception.
 * It can be used to indicate validation failures throughout the application.
 * </p>
 *
 * @author Lika Makejeva
 * @version 1.1
 * @since 1.0
 */
public class ValidationException extends RuntimeException {
    
    private static final long serialVersionUID = 1L; // ✅ Prevents serialization issues

    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception (can be another exception).
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
