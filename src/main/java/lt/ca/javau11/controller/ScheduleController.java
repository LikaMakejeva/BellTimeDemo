package lt.ca.javau11.controller;

import jakarta.validation.Valid;
import lt.ca.javau11.entity.Schedule;
import lt.ca.javau11.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for managing schedules.
 * This controller provides endpoints for retrieving, creating, updating, and deleting schedules.
 * It supports both standard and custom schedules.
 * 
 * @author Lika Makejeva
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/schedules")
@Validated
public class ScheduleController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);
    private final ScheduleService scheduleService;

    /**
     * Constructor for dependency injection.
     *
     * @param scheduleService The service responsible for schedule operations.
     */
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Retrieves all schedules.
     *
     * @return List of all schedules.
     */
    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        logger.info("Fetching all schedules");
        return ResponseEntity.ok(scheduleService.findAllSchedules());
    }

    /**
     * Retrieves a schedule by its ID.
     *
     * @param id The schedule ID.
     * @return The found schedule.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        logger.info("Fetching schedule with ID: {}", id);
        return ResponseEntity.ok(scheduleService.findScheduleById(id));
    }

    /**
     * Retrieves a schedule for a specific date.
     *
     * @param date The date to search for a schedule.
     * @return The corresponding schedule.
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<Schedule> getScheduleByDate(@PathVariable LocalDate date) {
        logger.info("Fetching schedule for date: {}", date);
        return ResponseEntity.ok(scheduleService.getScheduleForDate(date));
    }

    /**
     * Retrieves schedules within a specific date range.
     *
     * @param start The start date of the range.
     * @param end   The end date of the range.
     * @return List of schedules within the range.
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Schedule>> getSchedulesByDateRange(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        logger.info("Fetching schedules between {} and {}", start, end);
        return ResponseEntity.ok(scheduleService.getSchedulesByDateRange(start, end));
    }

    /**
     * Creates a new schedule.
     *
     * @param schedule The schedule to create.
     * @return The created schedule.
     */
    @PostMapping
    public ResponseEntity<?> createSchedule(@Valid @RequestBody Schedule schedule) {
        logger.info("Creating new schedule: {}", schedule);
        try {
            Schedule createdSchedule = scheduleService.createSchedule(schedule);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
        } catch (Exception e) {
            logger.error("Error creating schedule: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Updates an existing schedule.
     *
     * @param id       The ID of the schedule to update.
     * @param schedule The updated schedule data.
     * @return The updated schedule.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @Valid @RequestBody Schedule schedule) {
        logger.info("Updating schedule with ID: {}", id);
        return ResponseEntity.ok(scheduleService.updateSchedule(id, schedule));
    }

    /**
     * Deletes a schedule by its ID.
     *
     * @param id The ID of the schedule to delete.
     * @return HTTP 204 No Content response if successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        logger.info("Deleting schedule with ID: {}", id);
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all regular schedules.
     *
     * @return List of regular schedules.
     */
    @GetMapping("/regular")
    public ResponseEntity<List<Schedule>> getRegularSchedules() {
        logger.info("Fetching all regular schedules");
        return ResponseEntity.ok(scheduleService.getRegularSchedules());
    }

    /**
     * Retrieves all special schedules.
     *
     * @return List of special schedules.
     */
    @GetMapping("/special")
    public ResponseEntity<List<Schedule>> getSpecialSchedules() {
        logger.info("Fetching all special schedules");
        return ResponseEntity.ok(scheduleService.getSpecialSchedules());
    }

    /**
     * Saves a corrected version of a schedule as a special schedule.
     *
     * @param baseScheduleId The ID of the base schedule.
     * @param schedule       The modified schedule details.
     * @param customName     The custom name for the new schedule.
     * @return The newly saved special schedule.
     */
    @PostMapping("/save-corrected/{baseScheduleId}")
    public ResponseEntity<Schedule> saveCorrectedSchedule(
            @PathVariable Long baseScheduleId,
            @Valid @RequestBody Schedule schedule,
            @RequestParam String customName) {
        logger.info("Saving corrected schedule based on template {} with name '{}'", baseScheduleId, customName);
        return ResponseEntity.ok(scheduleService.saveCorrectedSchedule(baseScheduleId, schedule, customName));
    }
}
