package lt.ca.javau11.repository;

import lt.ca.javau11.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Lesson entities.
 * Provides methods for retrieving lessons by various criteria.
 */
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    /**
     * Finds lessons by schedule ID ordered by order number.
     *
     * @param scheduleId ID of the schedule.
     * @return List of lessons ordered by order number.
     */
    List<Lesson> findByScheduleIdOrderByOrderNumber(Long scheduleId);

    /**
     * Finds lessons by subject name in English.
     *
     * @param subject the subject name in English.
     * @return List of lessons with the specified subject.
     */
    List<Lesson> findBySubjectEn(String subject);

    /**
     * Finds lessons where the order number is greater than the specified value.
     *
     * @param orderNumber Order number threshold.
     * @return List of lessons with order numbers greater than the specified value.
     */
    List<Lesson> findByOrderNumberGreaterThan(int orderNumber);

    /**
     * Finds lessons by schedule ID and fetches associated call schedules.
     *
     * @param scheduleId ID of the schedule.
     * @return List of lessons with associated call schedules.
     */
    @Query("SELECT l FROM Lesson l LEFT JOIN FETCH l.callSchedule WHERE l.schedule.id = :scheduleId")
    List<Lesson> findByScheduleIdWithCallSchedule(@Param("scheduleId") Long scheduleId);

    /**
     * Finds a lesson by schedule ID and order number.
     *
     * @param scheduleId ID of the schedule.
     * @param orderNumber Order number of the lesson.
     * @return Optional containing the found lesson, or empty if not found.
     */
    Optional<Lesson> findByScheduleIdAndOrderNumber(Long scheduleId, int orderNumber);

    /**
     * Counts the number of lessons associated with a specific schedule.
     *
     * @param scheduleId ID of the schedule.
     * @return Number of lessons in the schedule.
     */
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.schedule.id = :scheduleId")
    int countLessonsBySchedule(@Param("scheduleId") Long scheduleId);
}
