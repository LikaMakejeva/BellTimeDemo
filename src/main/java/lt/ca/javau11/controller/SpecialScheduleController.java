package lt.ca.javau11.controller;

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
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecialSchedule> getSpecialScheduleById(@PathVariable Long id) {
        log.info("Fetching special schedule with ID: {}", id);
        return ResponseEntity.ok(specialScheduleService.getSpecialScheduleById(id));
    }

    /**
     * Retrieves a special schedule for a specific date.
     * <p>
     * If a special schedule exists for the given date, it is returned in the response.
     * If no schedule is found, a 404 Not Found response is returned.
     * </p>
     *
     * @param date the date for which the special schedule is requested (ISO format: YYYY-MM-DD).
     * @return a ResponseEntity containing the found special schedule, or a 404 response if none exists.
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<SpecialSchedule> getSpecialScheduleByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching special schedule for date: {}", date);
        SpecialSchedule schedule = specialScheduleService.getSpecialScheduleByDate(date);
        return schedule != null ? ResponseEntity.ok(schedule) : ResponseEntity.notFound().build();
    }

    /**
     * Retrieves all special schedules within a given date range.
     *
     * @param startDate the start date of the range (inclusive).
     * @param endDate the end date of the range (inclusive).
     * @return a ResponseEntity containing a list of special schedules within the specified range.
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<SpecialSchedule>> getSpecialSchedulesForDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Fetching special schedules between {} and {}", startDate, endDate);
        return ResponseEntity.ok(specialScheduleService.getSpecialScheduleForDateRange(startDate, endDate));
    }

    /**
     * Creates a new special schedule.
     *
     * @param specialSchedule the special schedule to be created.
     * @return a ResponseEntity containing the created special schedule.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SpecialSchedule> createSpecialSchedule(@Valid @RequestBody SpecialSchedule specialSchedule) {
        log.info("Creating special schedule: {}", specialSchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(specialScheduleService.createSpecialSchedule(specialSchedule));
    }

    /**
     * Updates an existing special schedule.
     *
     * @param id the ID of the special schedule to update.
     * @param specialSchedule the updated special schedule data.
     * @return a ResponseEntity containing the updated special schedule.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpecialSchedule> updateSpecialSchedule(
            @PathVariable Long id, @Valid @RequestBody SpecialSchedule specialSchedule) {
        log.info("Updating special schedule with ID: {}", id);
        return ResponseEntity.ok(specialScheduleService.updateSpecialSchedule(id, specialSchedule));
    }

    /**
     * Deletes a special schedule by its ID.
     * <p>
     * If the schedule with the given ID exists, it will be deleted.
     * If the schedule does not exist, an exception will be thrown.
     * This method returns a 204 No Content response to indicate successful deletion.
     * </p>
     *
     * @param id the ID of the special schedule to delete.
     * @return a ResponseEntity with a 204 No Content status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialSchedule(@PathVariable Long id) {
        log.info("Deleting special schedule with ID: {}", id);
        specialScheduleService.deleteSpecialSchedule(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Retrieves all special schedules.
     *
     * @return a ResponseEntity containing a list of all special schedules.
     */
    @GetMapping
    public ResponseEntity<List<SpecialSchedule>> getAllSpecialSchedules() {
        log.info("Fetching all special schedules");
        return ResponseEntity.ok(specialScheduleService.getAllSpecialSchedules());
    }
}
