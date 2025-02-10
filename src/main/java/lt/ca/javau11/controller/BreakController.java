package lt.ca.javau11.controller;

import lt.ca.javau11.entity.Break;
import lt.ca.javau11.service.BreakService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for managing break periods in the school schedule.
 * Provides CRUD operations for break periods.
 */
@RestController
@RequestMapping("/api/breaks")
public class BreakController {

    private final BreakService breakService;
    private static final Logger logger = LoggerFactory.getLogger(BreakController.class);

    /**
     * Constructor for dependency injection.
     *
     * @param breakService the service for break operations.
     */
    public BreakController(BreakService breakService) {
        this.breakService = breakService;
    }

    /**
     * Retrieves all break periods.
     *
     * @return a list of all break periods.
     */
    @GetMapping
    public List<Break> getAllBreaks() {
        logger.info("Fetching all break periods");
        return breakService.findAll();
    }

    /**
     * Creates a new break period.
     *
     * @param breakPeriod the break period to be created.
     * @return a ResponseEntity containing the created break period and HTTP status.
     */
    @PostMapping
    public ResponseEntity<Break> createBreak(@Valid @RequestBody Break breakPeriod) {
        logger.info("Creating new break period: {}", breakPeriod);
        Break createdBreak = breakService.save(breakPeriod);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBreak);
    }

    /**
     * Retrieves a break period by its ID.
     *
     * @param id the ID of the break period.
     * @return a ResponseEntity containing the break period, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Break> getBreakById(@PathVariable Long id) {
        logger.info("Fetching break period with ID: {}", id);
        Break breakPeriod = breakService.findById(id);
        if (breakPeriod == null) {
            logger.error("Break period with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(breakPeriod);
    }

    /**
     * Deletes a break period by its ID.
     *
     * @param id the ID of the break period to delete.
     * @return a ResponseEntity with HTTP status code 204 if deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBreak(@PathVariable Long id) {
        logger.info("Deleting break period with ID: {}", id);
        try {
            breakService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting break period with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Updates an existing break period by its ID.
     *
     * @param id the ID of the break period to update.
     * @param breakPeriod the updated break period data.
     * @return a ResponseEntity containing the updated break period, or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Break> updateBreak(@PathVariable Long id, @Valid @RequestBody Break breakPeriod) {
        logger.info("Updating break period with ID: {}", id);
        try {
            Break updatedBreak = breakService.update(id, breakPeriod);
            return ResponseEntity.ok(updatedBreak);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating break period with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
