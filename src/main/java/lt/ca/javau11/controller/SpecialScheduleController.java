package lt.ca.javau11.controller;

import jakarta.persistence.EntityNotFoundException;
import lt.ca.javau11.entity.SpecialSchedule;
import lt.ca.javau11.service.SpecialScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.ObjectError;

/**
 * REST controller for managing special schedules.
 * Provides endpoints for CRUD operations on special schedules.
 */
@RestController
@RequestMapping("/api/special-schedules")
@Validated
public class SpecialScheduleController {

    private static final Logger log = LoggerFactory.getLogger(SpecialScheduleController.class);
    private final SpecialScheduleService specialScheduleService;

    /**
     * Constructor for dependency injection.
     *
     * @param specialScheduleService the service for handling special schedule logic.
     */
    public SpecialScheduleController(SpecialScheduleService specialScheduleService) {
        this.specialScheduleService = specialScheduleService;
    }

    /**
     * Retrieves a special schedule by its ID.
     *
     * @param id the ID of the special schedule.
     * @return a ResponseEntity containing the found special schedule.
     * @throws EntityNotFoundException if no special schedule is found with the given ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecialSchedule> getSpecialScheduleById(@PathVariable Long id) {
        log.info("Fetching special schedule with ID: {}", id);
        SpecialSchedule specialSchedule = specialScheduleService.getSpecialScheduleById(id);
        return ResponseEntity.ok(specialSchedule);
    }

    /**
     * Retrieves a special schedule by its date.
     *
     * @param date the special date (ISO format) for the schedule.
     * @return a ResponseEntity containing the special schedule for the given date.
     * @throws EntityNotFoundException if no special schedule is found for the given date.
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<SpecialSchedule> getSpecialScheduleByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching special schedule for date: {}", date);
        SpecialSchedule specialSchedule = specialScheduleService.getSpecialScheduleByDate(date);
        return ResponseEntity.ok(specialSchedule);
    }

    /**
     * Creates or updates a special schedule.
     *
     * @param specialSchedule the special schedule to be created or updated.
     * @return a ResponseEntity containing the saved special schedule.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SpecialSchedule> createOrUpdateSpecialSchedule(@Valid @RequestBody SpecialSchedule specialSchedule) {
        log.info("Creating/updating special schedule: {}", specialSchedule);
        SpecialSchedule savedSchedule = specialScheduleService.saveSpecialSchedule(specialSchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
    }

    /**
     * Deletes a special schedule by its ID.
     *
     * @param id the ID of the special schedule to delete.
     * @return a ResponseEntity with a message indicating the deletion result.
     * @throws EntityNotFoundException if no special schedule is found with the given ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSpecialSchedule(@PathVariable Long id) {
        log.info("Deleting special schedule with ID: {}", id);
        specialScheduleService.deleteSpecialSchedule(id);
        String message = "Special schedule with ID " + id + " has been deleted.";
        log.info(message);
        return ResponseEntity.ok(message);
    }

    /**
     * Retrieves all special schedules.
     *
     * @return a ResponseEntity containing a list of all special schedules.
     */
    @GetMapping
    public ResponseEntity<List<SpecialSchedule>> getAllSpecialSchedules() {
        log.info("Fetching all special schedules");
        List<SpecialSchedule> schedules = specialScheduleService.getAllSpecialSchedules();
        log.info("Found {} special schedules", schedules.size());
        return ResponseEntity.ok(schedules);
    }

    /**
     * Handles validation errors (ConstraintViolationException).
     *
     * @param ex the exception containing validation errors.
     * @return a ResponseEntity with a list of error messages and HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationExceptions(jakarta.validation.ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.toList());
        log.warn("Validation error: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles method argument validation errors (MethodArgumentNotValidException).
     *
     * @param ex the exception containing method argument validation errors.
     * @return a ResponseEntity with a list of error messages and HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        log.warn("Method argument validation error: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles EntityNotFoundException.
     *
     * @param e the EntityNotFoundException.
     * @return a ResponseEntity with the error message and HTTP status NOT_FOUND.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("Entity not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
