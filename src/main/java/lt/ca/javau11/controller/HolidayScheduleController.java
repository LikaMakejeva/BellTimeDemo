package lt.ca.javau11.controller;

import lt.ca.javau11.entity.HolidaySchedule;
import lt.ca.javau11.service.HolidayScheduleService;
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
 * REST controller for managing holiday schedules.
 * Provides endpoints for CRUD operations on holiday schedules.
 */
@RestController
@RequestMapping("/api/holiday-schedules")
@Validated
public class HolidayScheduleController {

    private static final Logger log = LoggerFactory.getLogger(HolidayScheduleController.class);
    private final HolidayScheduleService holidayScheduleService;

    /**
     * Constructor for dependency injection.
     *
     * @param holidayScheduleService the service for holiday schedule operations.
     */
    public HolidayScheduleController(HolidayScheduleService holidayScheduleService) {
        this.holidayScheduleService = holidayScheduleService;
    }

    /**
     * Retrieve a holiday schedule by its ID.
     *
     * @param id the ID of the holiday schedule.
     * @return ResponseEntity containing the holiday schedule.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HolidaySchedule> getHolidayScheduleById(@PathVariable Long id) {
        log.info("Fetching holiday schedule with ID: {}", id);
        HolidaySchedule schedule = holidayScheduleService.findById(id);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Retrieve a holiday schedule by its special date.
     *
     * @param date the special date (ISO format) of the holiday.
     * @return ResponseEntity containing the holiday schedule.
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<HolidaySchedule> getHolidayScheduleByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching holiday schedule for date: {}", date);
        HolidaySchedule schedule = holidayScheduleService.findByHolidayDate(date);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Create a new holiday schedule.
     *
     * @param holidaySchedule the holiday schedule to create.
     * @return ResponseEntity containing the created holiday schedule.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HolidaySchedule> createHolidaySchedule(@Valid @RequestBody HolidaySchedule holidaySchedule) {
        log.info("Creating holiday schedule: {}", holidaySchedule);
        HolidaySchedule savedSchedule = holidayScheduleService.createHolidaySchedule(holidaySchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
    }

    /**
     * Update an existing holiday schedule.
     *
     * @param holidaySchedule the holiday schedule with updated data.
     * @return ResponseEntity containing the updated holiday schedule.
     */
    @PutMapping
    public ResponseEntity<HolidaySchedule> updateHolidaySchedule(@Valid @RequestBody HolidaySchedule holidaySchedule) {
        log.info("Updating holiday schedule: {}", holidaySchedule);
        HolidaySchedule updatedSchedule = holidayScheduleService.updateHolidaySchedule(holidaySchedule);
        return ResponseEntity.ok(updatedSchedule);
    }

    /**
     * Delete a holiday schedule by its ID.
     *
     * @param id the ID of the holiday schedule to delete.
     * @return ResponseEntity with a success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHolidaySchedule(@PathVariable Long id) {
        log.info("Deleting holiday schedule with ID: {}", id);
        holidayScheduleService.delete(id);
        String message = "Holiday schedule with ID " + id + " has been deleted.";
        log.info(message);
        return ResponseEntity.ok(message);
    }

    /**
     * Retrieve all holiday schedules.
     *
     * @return ResponseEntity containing a list of all holiday schedules.
     */
    @GetMapping
    public ResponseEntity<List<HolidaySchedule>> getAllHolidaySchedules() {
        log.info("Fetching all holiday schedules");
        List<HolidaySchedule> schedules = holidayScheduleService.findAll();
        log.info("Found {} holiday schedules", schedules.size());
        return ResponseEntity.ok(schedules);
    }

    /**
     * Handles validation errors (ConstraintViolationException).
     *
     * @param ex the exception containing validation errors.
     * @return ResponseEntity with a list of error messages and HTTP status BAD_REQUEST.
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
     * @return ResponseEntity with a list of error messages and HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        log.warn("Method argument validation error: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
