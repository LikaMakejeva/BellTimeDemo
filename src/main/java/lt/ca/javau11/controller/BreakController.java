package lt.ca.javau11.controller;

import lt.ca.javau11.entity.Break;
import lt.ca.javau11.entity.SpecialSchedule;
import lt.ca.javau11.service.BreakService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing break periods in the school schedule.
 * Provides CRUD operations for break periods and special schedules.
 *
 * @author Lika Makejeva
 * @version 2.0
 * @since 1.0
 */
@Validated
@RestController
@RequestMapping("/api/breaks")
public class BreakController {

    private final BreakService breakService;
    private static final Logger logger = LoggerFactory.getLogger(BreakController.class);

    public BreakController(BreakService breakService) {
        this.breakService = breakService;
    }

    /**
     * Retrieves all break periods.
     *
     * @return ResponseEntity containing a list of all break periods
     */
    @GetMapping
    public ResponseEntity<List<Break>> getAllBreaks() {
        logger.info("Fetching all break periods");
        return ResponseEntity.ok(breakService.findAllBreaks());
    }
    
    /**
     * Retrieves a break period by its name.
     *
     * @param name The name of the break period to retrieve
     * @return ResponseEntity containing the break details if found, or 404 if not found
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Break> getBreakByName(@PathVariable String name) {
        logger.info("Fetching break period with name: {}", name);
        
        Optional<Break> breakOptional = breakService.getBreakByName(name);
        if (breakOptional.isPresent()) {
            return ResponseEntity.ok(breakOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves all breaks for a specific date range.
     *
     * @param start Start date (inclusive)
     * @param end End date (inclusive)
     * @return ResponseEntity containing a list of breaks within the date range
     */
    @GetMapping("/range")
    public ResponseEntity<List<Break>> getBreaksForDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        logger.info("Fetching breaks between {} and {}", start, end);
        return ResponseEntity.ok(breakService.getBreaksForDateRange(start, end));
    }

    /**
     * Retrieves all breaks for a specific date.
     *
     * @param date The specific date for which to retrieve breaks
     * @return ResponseEntity containing a list of breaks for the given date
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Break>> getBreaksForDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.info("Fetching breaks for date: {}", date);
        return ResponseEntity.ok(breakService.getBreaksForDate(date));
    }

    /**
     * Retrieves all breaks associated with a specific schedule ID.
     *
     * @param scheduleId The ID of the schedule
     * @return ResponseEntity containing a list of breaks linked to the schedule
     */
    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<Break>> getBreaksBySchedule(@PathVariable Long scheduleId) {
        logger.info("Fetching breaks for schedule ID: {}", scheduleId);
        return ResponseEntity.ok(breakService.findBreaksBySchedule(scheduleId));
    }

    /**
     * Checks if there are active breaks for a specific date.
     *
     * @param date The date to check for active breaks
     * @return ResponseEntity containing true if breaks exist, false otherwise
     */
    @GetMapping("/active/{date}")
    public ResponseEntity<Boolean> hasActiveBreaks(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.info("Checking if there are active breaks on {}", date);
        return ResponseEntity.ok(breakService.hasActiveBreaks(date));
    }

    /**
     * Saves a break period.
     *
     * @param breakPeriod The break period to save
     * @return ResponseEntity containing the saved break period
     */
    @PostMapping("/save")
    public ResponseEntity<Break> saveBreak(@Valid @RequestBody Break breakPeriod) {
        logger.info("Saving break period: {}", breakPeriod);
        return ResponseEntity.ok(breakService.saveBreak(breakPeriod));
    }

    /**
     * Creates a new break period.
     *
     * @param breakPeriod The break period to create
     * @return ResponseEntity containing the created break period
     */
    @PostMapping
    public ResponseEntity<Break> createBreak(@Valid @RequestBody Break breakPeriod) {
        logger.info("Creating new break period: {}", breakPeriod);
        return ResponseEntity.ok(breakService.createBreak(breakPeriod));
    }

    /**
     * Retrieves a break period by its ID.
     *
     * @param id The ID of the break period
     * @return ResponseEntity containing the break period if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Break> getBreakById(@PathVariable Long id) {
        logger.info("Fetching break period with ID: {}", id);
        try {
            return ResponseEntity.ok(breakService.findBreakById(id));
        } catch (IllegalArgumentException e) {
            logger.error("Break period with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a break period by its ID.
     *
     * @param id The ID of the break period to delete
     * @return ResponseEntity with no content if successful, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBreak(@PathVariable Long id) {
        logger.info("Deleting break period with ID: {}", id);
        try {
            breakService.deleteBreak(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Break period with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates an existing break period.
     *
     * @param id The ID of the break period to update
     * @param breakPeriod The updated break period data
     * @return ResponseEntity containing the updated break period if found, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Break> updateBreak(
            @PathVariable Long id, 
            @Valid @RequestBody Break breakPeriod) {
        logger.info("Updating break period with ID: {}", id);
        try {
            return ResponseEntity.ok(breakService.updateBreak(id, breakPeriod));
        } catch (IllegalArgumentException e) {
            logger.error("Break period with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Saves a special schedule.
     *
     * @param specialSchedule The special schedule to save
     * @return ResponseEntity containing the saved special schedule
     */
    @PostMapping("/special")
    public ResponseEntity<SpecialSchedule> saveSpecialSchedule(
            @Valid @RequestBody SpecialSchedule specialSchedule) {
        logger.info("Saving special schedule: {}", specialSchedule);
        try {
            return ResponseEntity.ok(breakService.saveSpecialSchedule(specialSchedule));
        } catch (IllegalArgumentException e) {
            logger.error("Failed to save special schedule: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a special schedule by ID.
     *
     * @param id The ID of the special schedule to delete
     * @return ResponseEntity with no content if successful, or 404 if not found
     */
    @DeleteMapping("/special/{id}")
    public ResponseEntity<Void> deleteSpecialSchedule(@PathVariable Long id) {
        logger.info("Deleting special schedule with ID: {}", id);
        try {
            breakService.deleteSpecialSchedule(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Special schedule with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves a special schedule by ID.
     *
     * @param id The ID of the special schedule
     * @return ResponseEntity containing the special schedule if found, or 404 if not found
     */
    @GetMapping("/special/{id}")
    public ResponseEntity<SpecialSchedule> getSpecialSchedule(@PathVariable Long id) {
        logger.info("Fetching special schedule with ID: {}", id);
        try {
            return ResponseEntity.ok(breakService.getSpecialScheduleById(id));
        } catch (IllegalArgumentException e) {
            logger.error("Special schedule with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}