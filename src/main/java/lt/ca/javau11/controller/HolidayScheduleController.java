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

/**
 * REST controller for managing holiday schedules.
 * Provides endpoints for creating, retrieving, updating, and deleting holiday schedules.
 */
@RestController
@RequestMapping("/api/holiday-schedules")
@Validated
public class HolidayScheduleController {

    private static final Logger log = LoggerFactory.getLogger(HolidayScheduleController.class);
    private final HolidayScheduleService holidayScheduleService;

    public HolidayScheduleController(HolidayScheduleService holidayScheduleService) {
        this.holidayScheduleService = holidayScheduleService;
    }

    /**
     * GET /api/holiday-schedules : Retrieves all holiday schedules.
     *
     * @return ResponseEntity with a list of all holiday schedules.
     */
    @GetMapping
    public ResponseEntity<List<HolidaySchedule>> getAllHolidaySchedules() {
        log.info("Fetching all holiday schedules");
        List<HolidaySchedule> schedules = holidayScheduleService.getAllHolidaySchedules();
        return schedules.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(schedules);
    }

    /**
     * GET /api/holiday-schedules/{id} : Retrieves a holiday schedule by ID.
     *
     * @param id the ID of the holiday schedule.
     * @return ResponseEntity with the found holiday schedule or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HolidaySchedule> getHolidayScheduleById(@PathVariable Long id) {
        log.info("Fetching holiday schedule with ID: {}", id);
        try {
            HolidaySchedule schedule = holidayScheduleService.getHolidayScheduleById(id);
            return ResponseEntity.ok(schedule);
        } catch (IllegalArgumentException e) {
            log.warn("Holiday schedule not found for ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/holiday-schedules/date/{date} : Retrieves a holiday schedule for a specific date.
     *
     * @param date the date for which the holiday schedule is requested.
     * @return ResponseEntity with the found holiday schedule or 404 if not found.
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<HolidaySchedule> getHolidayScheduleByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching holiday schedule for date: {}", date);
        try {
            HolidaySchedule schedule = holidayScheduleService.getHolidayScheduleByDate(date);
            return ResponseEntity.ok(schedule);
        } catch (IllegalArgumentException e) {
            log.warn("No holiday schedule found for date: {}", date);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/holiday-schedules/date-range : Retrieves holiday schedules within a date range.
     *
     * @param startDate the start date (inclusive).
     * @param endDate the end date (inclusive).
     * @return ResponseEntity with a list of holiday schedules within the specified range.
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<HolidaySchedule>> getHolidaySchedulesForDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Fetching holiday schedules between {} and {}", startDate, endDate);
        List<HolidaySchedule> schedules = holidayScheduleService.getHolidayScheduleForDateRange(startDate, endDate);
        return schedules.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(schedules);
    }

    /**
     * POST /api/holiday-schedules : Creates a new holiday schedule.
     *
     * @param holidaySchedule the holiday schedule to be created.
     * @return ResponseEntity with the created holiday schedule.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HolidaySchedule> createHolidaySchedule(@Valid @RequestBody HolidaySchedule holidaySchedule) {
        log.info("Creating holiday schedule: {}", holidaySchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(holidayScheduleService.createHolidaySchedule(holidaySchedule));
    }

    /**
     * PUT /api/holiday-schedules/{id} : Updates an existing holiday schedule.
     *
     * @param id the ID of the holiday schedule to update.
     * @param holidaySchedule the updated holiday schedule data.
     * @return ResponseEntity with the updated holiday schedule.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HolidaySchedule> updateHolidaySchedule(
            @PathVariable Long id, @Valid @RequestBody HolidaySchedule holidaySchedule) {
        log.info("Updating holiday schedule with ID: {}", id);
        try {
            HolidaySchedule updatedSchedule = holidayScheduleService.updateHolidaySchedule(id, holidaySchedule);
            return ResponseEntity.ok(updatedSchedule);
        } catch (IllegalArgumentException e) {
            log.warn("Holiday schedule not found for ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/holiday-schedules/save : Saves or updates a holiday schedule.
     *
     * @param holidaySchedule the holiday schedule to save or update.
     * @return ResponseEntity with the saved holiday schedule.
     */
    @PostMapping("/save")
    public ResponseEntity<HolidaySchedule> saveHolidaySchedule(@Valid @RequestBody HolidaySchedule holidaySchedule) {
        log.info("Saving holiday schedule: {}", holidaySchedule);
        return ResponseEntity.ok(holidayScheduleService.saveHolidaySchedule(holidaySchedule));
    }

    /**
     * DELETE /api/holiday-schedules/{id} : Deletes a holiday schedule by ID.
     *
     * @param id the ID of the holiday schedule to delete.
     * @return ResponseEntity with no content on success or 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHolidaySchedule(@PathVariable Long id) {
        log.info("Deleting holiday schedule with ID: {}", id);
        try {
            holidayScheduleService.deleteHolidayScheduleById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Holiday schedule not found for ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/holiday-schedules/exists/{date} : Checks if a holiday schedule exists for a given date.
     *
     * @param date the date to check.
     * @return ResponseEntity with true if a holiday exists, false otherwise.
     */
    @GetMapping("/exists/{date}")
    public ResponseEntity<Boolean> holidayExists(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Checking if holiday schedule exists for date: {}", date);
        return ResponseEntity.ok(holidayScheduleService.holidayExists(date));
    }

    /**
     * GET /api/holiday-schedules/active/{date} : Checks if there is an active holiday for a given date.
     *
     * @param date the date to check for an active holiday.
     * @return ResponseEntity with true if an active holiday exists, false otherwise.
     */
    @GetMapping("/active/{date}")
    public ResponseEntity<Boolean> hasActiveHoliday(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Checking for active holiday on {}", date);
        return ResponseEntity.ok(holidayScheduleService.hasActiveHoliday(date));
    }
}
