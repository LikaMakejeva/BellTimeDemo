package lt.ca.javau11.service;

import lt.ca.javau11.entity.Lesson;
import lt.ca.javau11.repository.LessonRepository;
import lt.ca.javau11.validation.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing lessons.
 * Provides optimized methods to create, retrieve, update, and delete lessons.
 * All methods ensure proper loading of associated entities and transaction management.
 *
 * @author Lika Makejeva
 * @version 2.0
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class LessonService {

    private static final Logger log = LoggerFactory.getLogger(LessonService.class);
    private final LessonRepository lessonRepository;
    private final ValidationService validationService;

    public LessonService(LessonRepository lessonRepository, ValidationService validationService) {
        this.lessonRepository = lessonRepository;
        this.validationService = validationService;
    }

    /**
     * Retrieves all lessons with their schedules.
     *
     * @return list of all lessons with schedules loaded
     */
    public List<Lesson> findAllLessons() {
        log.info("Fetching all lessons");
        List<Lesson> lessons = lessonRepository.findAllWithSchedules();
        log.info("Found {} lessons", lessons.size());
        return lessons;
    }

    /**
     * Retrieves a lesson by its ID with schedule loaded.
     *
     * @param id the ID of the lesson
     * @return the found lesson with schedule
     * @throws IllegalArgumentException if the lesson is not found
     */
    public Lesson findLessonById(Long id) {
        log.info("Searching for lesson with ID: {}", id);
        return lessonRepository.findByIdWithSchedule(id)
                .orElseThrow(() -> {
                    log.error("Lesson with ID {} not found", id);
                    return new IllegalArgumentException("Lesson not found with ID: " + id);
                });
    }

    /**
     * Saves a lesson and returns it with schedule loaded.
     *
     * @param lesson the lesson to save
     * @return the saved lesson with schedule
     */
    @Transactional
    public Lesson saveLesson(Lesson lesson) {
        validationService.validateLesson(lesson);
        log.info("Saving lesson: {}", lesson);
        Lesson saved = lessonRepository.save(lesson);
        return lessonRepository.findByIdWithSchedule(saved.getId()).orElseThrow();
    }

    /**
     * Retrieves all lessons for a specific schedule.
     *
     * @param scheduleId the ID of the schedule
     * @return list of lessons with schedules loaded
     */
    public List<Lesson> findLessonsBySchedule(Long scheduleId) {
        log.info("Fetching lessons for schedule ID: {}", scheduleId);
        List<Lesson> lessons = lessonRepository.findByScheduleIdWithSchedule(scheduleId);
        log.info("Found {} lessons for schedule ID: {}", lessons.size(), scheduleId);
        return lessons;
    }

    /**
     * Retrieves lessons for a specific date.
     *
     * @param date the date of the schedule
     * @return list of lessons with schedules loaded
     */
    public List<Lesson> findLessonsByScheduleDate(LocalDate date) {
        log.info("Fetching lessons for schedule date: {}", date);
        List<Lesson> lessons = lessonRepository.findByScheduleDateWithSchedule(date);
        log.info("Found {} lessons for date: {}", lessons.size(), date);
        return lessons;
    }

    /**
     * Retrieves lessons within a date range.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return list of lessons with schedules loaded
     */
    public List<Lesson> getLessonsForDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching lessons between {} and {}", startDate, endDate);
        return lessonRepository.findByDateRangeWithSchedule(startDate, endDate);
    }

    /**
     * Creates a new lesson and returns it with schedule loaded.
     *
     * @param lesson the lesson to create
     * @return the created lesson with schedule
     */
    @Transactional
    public Lesson createLesson(Lesson lesson) {
        validationService.validateLesson(lesson);
        log.info("Creating new lesson: {}", lesson);
        Lesson saved = lessonRepository.save(lesson);
        return lessonRepository.findByIdWithSchedule(saved.getId()).orElseThrow();
    }

    /**
     * Updates a lesson and returns it with schedule loaded.
     *
     * @param id the ID of the lesson
     * @param lessonDetails the lesson data to update
     * @return the updated lesson with schedule
     */
    @Transactional
    public Lesson updateLesson(Long id, Lesson lessonDetails) {
        log.info("Updating lesson with ID: {}", id);
        Lesson existingLesson = findLessonById(id);
        validationService.validateLesson(lessonDetails);

        existingLesson.setOrderNumber(lessonDetails.getOrderNumber());
        existingLesson.setSubjectEn(lessonDetails.getSubjectEn());
        existingLesson.setDuration(lessonDetails.getDuration());
        existingLesson.setSchedule(lessonDetails.getSchedule());

        Lesson saved = lessonRepository.save(existingLesson);
        log.info("Lesson with ID {} updated successfully", id);
        return lessonRepository.findByIdWithSchedule(saved.getId()).orElseThrow();
    }

    /**
     * Deletes a lesson by its ID.
     *
     * @param id the ID of the lesson to delete
     * @throws IllegalArgumentException if the lesson does not exist
     */
    @Transactional
    public void deleteLessonById(Long id) {
        log.info("Deleting lesson with ID: {}", id);
        if (!lessonRepository.existsById(id)) {
            log.error("Lesson with ID {} does not exist", id);
            throw new IllegalArgumentException("Lesson not found with ID: " + id);
        }
        lessonRepository.deleteById(id);
        log.info("Lesson with ID {} successfully deleted", id);
    }

    /**
     * Checks if a lesson exists by ID.
     *
     * @param id the ID of the lesson
     * @return true if the lesson exists
     */
    public boolean existsById(Long id) {
        boolean exists = lessonRepository.existsById(id);
        log.info("Checking existence of lesson with ID {}: {}", id, exists);
        return exists;
    }

    /**
     * Checks if there is an active lesson for a date.
     *
     * @param date the date to check
     * @return true if an active lesson exists
     */
    public boolean hasActiveLesson(LocalDate date) {
        log.info("Checking for active lesson on {}", date);
        return lessonRepository.existsBySchedule_EffectiveDate(date);
    }
}