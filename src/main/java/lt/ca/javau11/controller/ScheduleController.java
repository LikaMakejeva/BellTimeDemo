package lt.ca.javau11.controller;

import lt.ca.javau11.entity.Schedule;
import lt.ca.javau11.entity.DayOfWeek;
import lt.ca.javau11.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing school schedules.
 * Provides endpoints for CRUD operations on schedules.
 */
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);
    private final ScheduleService scheduleService;

    /**
     * Constructor for dependency injection.
     *
     * @param scheduleService the service for schedule operations.
     */
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Retrieves all schedules.
     *
     * @return ResponseEntity containing a list of all schedules.
     */
    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        logger.info("Request to get all schedules");
        List<Schedule> schedules = scheduleService.findAllSchedules();
        logger.info("Found {} schedules", schedules.size());
        return ResponseEntity.ok(schedules);
    }

    /**
     * Retrieves a schedule by its ID.
     *
     * @param id the ID of the schedule.
     * @return ResponseEntity containing the schedule.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        logger.info("Request to get schedule with ID: {}", id);
        Schedule schedule = scheduleService.findScheduleById(id);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Retrieves a schedule effective on a specific date.
     *
     * @param date the effective date in ISO format.
     * @return ResponseEntity containing the schedule for the given date.
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<Schedule> getScheduleForDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.info("Request to get schedule for date: {}", date);
        Schedule schedule = scheduleService.getScheduleForDate(date);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Retrieves schedules within a specified date range.
     *
     * @param start the start date (inclusive).
     * @param end   the end date (inclusive).
     * @return ResponseEntity containing a list of schedules in the date range.
     */
    @GetMapping("/range")
    public ResponseEntity<List<Schedule>> getSchedulesByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        logger.info("Request to get schedules from {} to {}", start, end);
        List<Schedule> schedules = scheduleService.getSchedulesByDateRange(start, end);
        return ResponseEntity.ok(schedules);
    }

    /**
     * Checks for an active schedule on a specific day of the week.
     *
     * @param dayOfWeek the day of the week.
     * @return ResponseEntity containing true if an active schedule exists, false otherwise.
     */
    @GetMapping("/active/{dayOfWeek}")
    public ResponseEntity<Boolean> checkActiveSchedule(@PathVariable DayOfWeek dayOfWeek) {
        logger.info("Request to check active schedule for weekday: {}", dayOfWeek);
        boolean hasActiveSchedule = scheduleService.hasActiveSchedule(dayOfWeek);
        return ResponseEntity.ok(hasActiveSchedule);
    }

    /**
     * Creates a new schedule.
     *
     * @param schedule the schedule to create.
     * @return ResponseEntity containing the created schedule.
     */
    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        logger.info("Request to create a new schedule");
        Schedule createdSchedule = scheduleService.saveSchedule(schedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
    }

    /**
     * Updates an existing schedule.
     *
     * @param id              the ID of the schedule to update.
     * @param scheduleDetails the updated schedule data.
     * @return ResponseEntity containing the updated schedule.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id, @RequestBody Schedule scheduleDetails) {
        logger.info("Request to update schedule with ID: {}", id);
        Schedule updatedSchedule = scheduleService.updateSchedule(id, scheduleDetails);
        return ResponseEntity.ok(updatedSchedule);
    }

    /**
     * Deletes a schedule by its ID.
     *
     * @param id the ID of the schedule to delete.
     * @return ResponseEntity with HTTP status NO_CONTENT if deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        logger.info("Request to delete schedule with ID: {}", id);
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
