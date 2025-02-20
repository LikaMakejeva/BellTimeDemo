package lt.ca.javau11.controller;

import lt.ca.javau11.entity.CallSchedule;
import lt.ca.javau11.entity.CallType;
import lt.ca.javau11.service.CallScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * REST controller for managing school bell schedules.
 * Provides endpoints for CRUD operations and queries related to CallSchedule entities.
 */
@RestController
@RequestMapping("/api/call-schedules")
@Validated
public class CallScheduleController {

    private static final Logger log = LoggerFactory.getLogger(CallScheduleController.class);
    private final CallScheduleService callScheduleService;

    public CallScheduleController(CallScheduleService callScheduleService) {
        this.callScheduleService = callScheduleService;
    }

    /**
     * GET /api/call-schedules : Retrieve all call schedules.
     *
     * @return ResponseEntity containing a list of all call schedules
     */
    @GetMapping
    public ResponseEntity<List<CallSchedule>> getAllCallSchedules() {
        log.info("Fetching all call schedules");
        List<CallSchedule> schedules = callScheduleService.getAllCallSchedules();
        return schedules.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(schedules);
    }

    /**
     * GET /api/call-schedules/{id} : Retrieve a call schedule by ID.
     *
     * @param id the ID of the call schedule
     * @return ResponseEntity containing the call schedule, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CallSchedule> getCallScheduleById(@PathVariable Long id) {
        log.info("Fetching call schedule with ID: {}", id);
        try {
            CallSchedule schedule = callScheduleService.getCallScheduleById(id);
            return ResponseEntity.ok(schedule);
        } catch (IllegalArgumentException e) {
            log.warn("Call schedule not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * POST /api/call-schedules : Create a new call schedule.
     *
     * @param callSchedule the call schedule to be created
     * @return ResponseEntity containing the created call schedule
     */
    @PostMapping
    public ResponseEntity<CallSchedule> saveCallSchedule(@Valid @RequestBody CallSchedule callSchedule) {
        log.info("Saving new call schedule: {}", callSchedule);
        CallSchedule savedSchedule = callScheduleService.saveCallSchedule(callSchedule);
        return ResponseEntity.ok().body(savedSchedule);
    }

    /**
     * DELETE /api/call-schedules/{id} : Delete a call schedule by ID.
     *
     * @param id the ID of the call schedule
     * @return ResponseEntity with status 204 if deleted successfully, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCallSchedule(@PathVariable Long id) {
        log.info("Deleting call schedule with ID: {}", id);
        boolean deleted = callScheduleService.deleteCallScheduleById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * GET /api/call-schedules/type/{callType} : Retrieve call schedules by type.
     *
     * @param callType the type of call schedules to retrieve
     * @return ResponseEntity containing the list of matching call schedules
     */
    @GetMapping("/type/{callType}")
    public ResponseEntity<List<CallSchedule>> getCallSchedulesByType(@PathVariable CallType callType) {
        log.info("Fetching call schedules of type: {}", callType);
        List<CallSchedule> schedules = callScheduleService.findAllByCallType(callType);
        return schedules.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(schedules);
    }

    /**
     * GET /api/call-schedules/time-range : Retrieve call schedules within a time range.
     *
     * @param start start time of the range
     * @param end   end time of the range
     * @return ResponseEntity containing the list of call schedules within the range
     */
    @GetMapping("/time-range")
    public ResponseEntity<List<CallSchedule>> getCallSchedulesByTimeRange(
            @RequestParam LocalTime start,
            @RequestParam LocalTime end) {
        log.info("Fetching call schedules between {} and {}", start, end);
        List<CallSchedule> schedules = callScheduleService.findAllByTimeRange(start, end);
        return schedules.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(schedules);
    }

    /**
     * GET /api/call-schedules/break/{breakId} : Retrieve call schedules for a break.
     *
     * @param breakId the ID of the break
     * @return ResponseEntity containing the list of call schedules, or 404 if none found
     */
    @GetMapping("/break/{breakId}")
    public ResponseEntity<List<CallSchedule>> getCallSchedulesByBreak(@PathVariable Long breakId) {
        log.info("Fetching call schedules for break ID: {}", breakId);
        List<CallSchedule> schedules = callScheduleService.findAllByBreak(breakId);
        return schedules.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(schedules);
    }

    /**
     * GET /api/call-schedules/date/{date} : Retrieve call schedules for a specific date.
     *
     * @param date the date to retrieve schedules for
     * @return ResponseEntity containing the list of call schedules for the specified date
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<CallSchedule>> getCallSchedulesForDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching call schedules for date: {}", date);
        List<CallSchedule> schedules = callScheduleService.getCallSchedulesForDate(date);
        return schedules.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(schedules);
    }
}
