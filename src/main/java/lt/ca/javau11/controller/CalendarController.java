package lt.ca.javau11.controller;

import jakarta.validation.Valid;
import lt.ca.javau11.dto.ScheduleEventDTO;
import lt.ca.javau11.service.ScheduleEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for managing calendar events.
 * 
 * This controller provides endpoints to retrieve, add, update, and delete
 * lesson and break schedules formatted as calendar events.
 * 
 *
 * @author Lika Makejeva
 * @version 1.3
 * @since 1.0
 */
@RestController
@RequestMapping("/api/calendar")
@Validated 
public class CalendarController {

    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);
    private final ScheduleEventService scheduleEventService;

    /**
     * Constructor for dependency injection.
     *
     * @param scheduleEventService Service responsible for retrieving and managing schedule events.
     */
    public CalendarController(ScheduleEventService scheduleEventService) {
        this.scheduleEventService = scheduleEventService;
    }

    /**
     * Retrieves all calendar events within a specified date range.
     *
     * @param start Start date (inclusive).
     * @param end   End date (inclusive).
     * @return A list of schedule events within the specified range.
     */
    @GetMapping("/events/range")
    public ResponseEntity<List<ScheduleEventDTO>> getEventsForDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        logger.info("Fetching calendar events between {} and {}", start, end);
        List<ScheduleEventDTO> events = scheduleEventService.getEventsForDateRange(start, end);
        return ResponseEntity.ok(events);
    }

    /**
     * Retrieves all calendar events for a specific date.
     *
     * @param date The specific date for which to retrieve events.
     * @return A list of schedule events for the given date.
     */
    @GetMapping("/events/date/{date}")
    public ResponseEntity<List<ScheduleEventDTO>> getEventsForDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        logger.info("Fetching calendar events for date: {}", date);
        List<ScheduleEventDTO> events = scheduleEventService.getEventsForDate(date);
        return ResponseEntity.ok(events);
    }

    /**
     * Adds a new calendar event.
     * This method allows adding a new lesson or break event to the schedule.
     *
     * @param eventDTO The event details.
     * @return The created event.
     */
    @PostMapping("/events")
    public ResponseEntity<ScheduleEventDTO> createEvent(@Valid @RequestBody ScheduleEventDTO eventDTO) { // ✅ @Valid для валидации DTO
        logger.info("Creating new calendar event: {}", eventDTO);
        ScheduleEventDTO createdEvent = scheduleEventService.createEvent(eventDTO);
        return ResponseEntity.ok(createdEvent);
    }

    /**
     * Updates an existing calendar event.
     * This method allows modifying an existing lesson or break.
     *
     * @param id       The ID of the event to update.
     * @param eventDTO The new event details.
     * @return The updated event.
     */
    @PutMapping("/events/{id}")
    public ResponseEntity<ScheduleEventDTO> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleEventDTO eventDTO) { // ✅ @Valid для валидации DTO
        logger.info("Updating calendar event with ID {}: {}", id, eventDTO);
        ScheduleEventDTO updatedEvent = scheduleEventService.updateEvent(id, eventDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    /**
     * Deletes a calendar event by its ID.
     *
     * @param id The ID of the event to delete.
     * @return ResponseEntity with status 204 No Content.
     */
    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        logger.info("Deleting calendar event with ID {}", id);
        scheduleEventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
