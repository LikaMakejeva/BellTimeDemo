package lt.ca.javau11.repository;

import lt.ca.javau11.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Lesson entities.
 * Provides methods for retrieving lessons with their associated schedules.
 *
 * @author Lika Makejeva
 * @version 2.0
 * @since 1.0
 */
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    /**
     * Finds a lesson by ID with schedule loaded.
     *
     * @param id The ID of the lesson
     * @return Optional containing the lesson if found
     */
    @Query("SELECT l FROM Lesson l LEFT JOIN FETCH l.schedule WHERE l.id = :id")
    Optional<Lesson> findByIdWithSchedule(@Param("id") Long id);

    /**
     * Finds all lessons with their schedules loaded.
     *
     * @return List of all lessons with schedules
     */
    @Query("SELECT l FROM Lesson l LEFT JOIN FETCH l.schedule")
    List<Lesson> findAllWithSchedules();

    /**
     * Finds all lessons for a schedule with schedule loaded.
     *
     * @param scheduleId The ID of the schedule
     * @return List of lessons ordered by order number
     */
    @Query("SELECT l FROM Lesson l LEFT JOIN FETCH l.schedule WHERE l.schedule.id = :scheduleId ORDER BY l.orderNumber")
    List<Lesson> findByScheduleIdWithSchedule(@Param("scheduleId") Long scheduleId);

    /**
     * Finds lessons by subject name with schedule loaded.
     *
     * @param subject The subject name in English
     * @return List of lessons with the specified subject
     */
    @Query("SELECT l FROM Lesson l LEFT JOIN FETCH l.schedule WHERE l.subjectEn = :subject")
    List<Lesson> findBySubjectEnWithSchedule(@Param("subject") String subject);

    /**
     * Finds lessons for a specific date with schedule loaded.
     *
     * @param date The date of the schedule
     * @return List of lessons for the given date
     */
    @Query("SELECT l FROM Lesson l LEFT JOIN FETCH l.schedule WHERE l.schedule.effectiveDate = :date")
    List<Lesson> findByScheduleDateWithSchedule(@Param("date") LocalDate date);

    /**
     * Finds lessons within a date range with schedule loaded.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return List of lessons within the date range
     */
    @Query("SELECT l FROM Lesson l LEFT JOIN FETCH l.schedule " +
           "WHERE l.schedule.effectiveDate BETWEEN :startDate AND :endDate")
    List<Lesson> findByDateRangeWithSchedule(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Checks if there are any lessons for a specific date.
     *
     * @param date The date to check
     * @return true if lessons exist for the date
     */
    boolean existsBySchedule_EffectiveDate(LocalDate date);

    /**
     * Counts lessons for a specific schedule.
     *
     * @param scheduleId The ID of the schedule
     * @return Number of lessons in the schedule
     */
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.schedule.id = :scheduleId")
    int countLessonsBySchedule(@Param("scheduleId") Long scheduleId);

	List<Lesson> findByScheduleIdOrderByOrderNumber(Long id);
}