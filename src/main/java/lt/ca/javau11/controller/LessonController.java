package lt.ca.javau11.controller;

import lt.ca.javau11.entity.Lesson;
import lt.ca.javau11.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for managing lessons.
 * Provides endpoints for CRUD operations on lessons.
 */
@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    /**
     * Constructor for dependency injection.
     *
     * @param lessonService the service for lessons.
     */
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    /**
     * Retrieve all lessons.
     *
     * @return list of all lessons.
     */
    @GetMapping
    public List<Lesson> getAllLessons() {
        return lessonService.findAllLessons();
    }

    /**
     * Retrieve a lesson by its ID.
     *
     * @param id the ID of the lesson.
     * @return ResponseEntity with the found lesson or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
        Lesson lesson = lessonService.findLessonById(id);
        if (lesson == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lesson);
    }

    /**
     * Create a new lesson.
     *
     * @param lesson the lesson to create.
     * @return ResponseEntity with the created lesson.
     */
    @PostMapping
    public ResponseEntity<Lesson> createLesson(@Valid @RequestBody Lesson lesson) {
        Lesson createdLesson = lessonService.saveLesson(lesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    /**
     * Update an existing lesson by ID.
     *
     * @param id the ID of the lesson to update.
     * @param lesson the lesson data to update.
     * @return ResponseEntity with the updated lesson or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable Long id, @Valid @RequestBody Lesson lesson) {
        Lesson updatedLesson = lessonService.updateLesson(id, lesson);
        if (updatedLesson == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLesson);
    }

    /**
     * Delete a lesson by its ID.
     *
     * @param id the ID of the lesson to delete.
     * @return ResponseEntity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
