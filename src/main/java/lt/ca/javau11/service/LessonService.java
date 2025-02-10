package lt.ca.javau11.service;

import lt.ca.javau11.entity.Lesson;
import lt.ca.javau11.repository.LessonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing lessons.
 */
@Service
public class LessonService {

    private static final Logger log = LoggerFactory.getLogger(LessonService.class);
    private final LessonRepository lessonRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param lessonRepository the repository for lessons.
     */
    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    /**
     * Retrieves all lessons.
     *
     * @return list of all lessons.
     */
    public List<Lesson> findAllLessons() {
        log.info("Fetching all lessons");
        List<Lesson> lessons = lessonRepository.findAll();
        log.info("Found {} lessons", lessons.size());
        return lessons;
    }

    /**
     * Retrieves a lesson by its ID.
     *
     * @param id the ID of the lesson.
     * @return the found lesson, or null if not found.
     */
    public Lesson findLessonById(Long id) {
        log.info("Searching for lesson with ID: {}", id);
        Optional<Lesson> lesson = lessonRepository.findById(id);
        if (!lesson.isPresent()) {
            log.warn("Lesson with ID {} not found", id);
        }
        return lesson.orElse(null);
    }

    /**
     * Saves a new lesson.
     *
     * @param lesson the lesson to save.
     * @return the saved lesson.
     * @throws IllegalArgumentException if the subject name is missing.
     */
    public Lesson saveLesson(Lesson lesson) {
        if (lesson.getSubjectEn() == null || lesson.getSubjectEn().trim().isEmpty()) {
            log.error("Subject name is required");
            throw new IllegalArgumentException("Subject name is required");
        }
        log.info("Saving lesson: {}", lesson);
        return lessonRepository.save(lesson);
    }

    /**
     * Updates an existing lesson by ID.
     *
     * @param id the ID of the lesson.
     * @param lessonDetails the lesson data to update.
     * @return the updated lesson, or null if not found.
     */
    public Lesson updateLesson(Long id, Lesson lessonDetails) {
        log.info("Updating lesson with ID: {}", id);
        Optional<Lesson> optionalLesson = lessonRepository.findById(id);
        if (optionalLesson.isPresent()) {
            Lesson existingLesson = optionalLesson.get();
            if (lessonDetails.getSubjectEn() != null) {
                existingLesson.setSubject(lessonDetails.getSubjectEn());
            }
            if (lessonDetails.getSchedule() != null) {
                existingLesson.setSchedule(lessonDetails.getSchedule());
            }
            // Update additional fields if needed.
            log.info("Saving updated lesson with ID: {}", id);
            return lessonRepository.save(existingLesson);
        }
        log.warn("Lesson with ID {} not found for update", id);
        return null;
    }

    /**
     * Deletes a lesson by its ID.
     *
     * @param id the ID of the lesson to delete.
     */
    public void deleteLesson(Long id) {
        log.info("Deleting lesson with ID: {}", id);
        lessonRepository.deleteById(id);
    }

    /**
     * Counts the number of lessons associated with a specific schedule.
     *
     * @param scheduleId the ID of the schedule.
     * @return the count of lessons.
     */
    public int countLessonsBySchedule(Long scheduleId) {
        int count = lessonRepository.countLessonsBySchedule(scheduleId);
        log.info("Count of lessons for schedule ID {}: {}", scheduleId, count);
        return count;
    }
}
