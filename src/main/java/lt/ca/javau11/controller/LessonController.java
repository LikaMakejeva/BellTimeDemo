package lt.ca.javau11.controller;

import lt.ca.javau11.entity.Lesson;
import lt.ca.javau11.service.LessonService;
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
 * REST controller for managing lessons.
 * Provides endpoints for CRUD operations and additional queries on lessons.
 *
 * @author Lika Makejeva
 * @version 2.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/lessons")
@Validated
public class LessonController {

    private static final Logger logger = LoggerFactory.getLogger(LessonController.class);
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    /**
     * Retrieves all lessons.
     *
     * @return ResponseEntity containing a list of all lessons
     */
    @GetMapping
    public ResponseEntity<List<Lesson>> getAllLessons() {
        logger.info("Fetching all lessons");
        return ResponseEntity.ok(lessonService.findAllLessons());
    }

    /**
     * Retrieves a lesson by its ID.
     *
     * @param id The ID of the lesson
     * @return ResponseEntity containing the lesson if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
        logger.info("Fetching lesson with ID: {}", id);
        try {
            return ResponseEntity.ok(lessonService.findLessonById(id));
        } catch (IllegalArgumentException e) {
            logger.warn("Lesson not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all lessons for a specific schedule.
     *
     * @param scheduleId The ID of the schedule
     * @return ResponseEntity containing a list of lessons
     */
    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<Lesson>> getLessonsBySchedule(@PathVariable Long scheduleId) {
        logger.info("Fetching lessons for schedule ID: {}", scheduleId);
        return ResponseEntity.ok(lessonService.findLessonsBySchedule(scheduleId));
    }

    /**
     * Retrieves all lessons for a specific date.
     *
     * @param date The date to retrieve lessons for
     * @return ResponseEntity containing a list of lessons
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Lesson>> getLessonsByScheduleDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.info("Fetching lessons for schedule date: {}", date);
        List<Lesson> lessons = lessonService.findLessonsByScheduleDate(date);
        if (lessons.isEmpty()) {
            logger.warn("No lessons found for date: {}", date);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lessons);
    }

    /**
     * Retrieves lessons within a date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return ResponseEntity containing a list of lessons
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Lesson>> getLessonsForDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        logger.info("Fetching lessons between {} and {}", startDate, endDate);
        return ResponseEntity.ok(lessonService.getLessonsForDateRange(startDate, endDate));
    }

    /**
     * Creates a new lesson.
     *
     * @param lesson The lesson to create
     * @return ResponseEntity containing the created lesson
     */
    @PostMapping
    public ResponseEntity<Lesson> createLesson(@Valid @RequestBody Lesson lesson) {
        logger.info("Creating new lesson: {}", lesson);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lessonService.createLesson(lesson));
    }

    /**
     * Updates an existing lesson.
     *
     * @param id The ID of the lesson to update
     * @param lesson The updated lesson data
     * @return ResponseEntity containing the updated lesson
     */
    @PutMapping("/{id}")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Long id, 
            @Valid @RequestBody Lesson lesson) {
        logger.info("Updating lesson with ID: {}", id);
        try {
            return ResponseEntity.ok(lessonService.updateLesson(id, lesson));
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update lesson: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Saves a lesson (creates new or updates existing).
     *
     * @param lesson The lesson to save
     * @return ResponseEntity containing the saved lesson
     */
    @PostMapping("/save")
    public ResponseEntity<Lesson> saveLesson(@Valid @RequestBody Lesson lesson) {
        logger.info("Saving lesson: {}", lesson);
        if (lesson.getId() == null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(lessonService.createLesson(lesson));
        } else {
            return ResponseEntity.ok(lessonService.updateLesson(lesson.getId(), lesson));
        }
    }

    /**
     * Deletes a lesson.
     *
     * @param id The ID of the lesson to delete
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        logger.info("Deleting lesson with ID: {}", id);
        try {
            lessonService.deleteLessonById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete lesson: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Checks if a lesson exists.
     *
     * @param id The ID of the lesson
     * @return ResponseEntity containing true if exists, false otherwise
     */
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> lessonExists(@PathVariable Long id) {
        logger.info("Checking if lesson exists with ID: {}", id);
        return ResponseEntity.ok(lessonService.existsById(id));
    }

    /**
     * Checks for active lessons on a specific date.
     *
     * @param date The date to check
     * @return ResponseEntity containing true if active lessons exist
     */
    @GetMapping("/active/{date}")
    public ResponseEntity<Boolean> hasActiveLesson(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.info("Checking if active lessons exist for date: {}", date);
        return ResponseEntity.ok(lessonService.hasActiveLesson(date));
    }
}