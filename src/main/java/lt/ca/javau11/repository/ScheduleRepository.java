package lt.ca.javau11.repository;


import lt.ca.javau11.entity.Schedule;
import java.time.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Schedule entities.
 * Provides methods for CRUD operations and custom queries for school schedules.
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * Finds a schedule that becomes effective on a specific date.
     *
     * @param effectiveDate the effective date.
     * @return an Optional containing the schedule if found, or empty if not.
     */
    Optional<Schedule> findByEffectiveDate(LocalDate effectiveDate);

    /**
     * Retrieves all schedules for a specific day of the week.
     *
     * @param dayOfWeek the day of the week.
     * @return a list of schedules for the specified day.
     */
    List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek);

    /**
     * Finds all schedules effective within a specified date range.
     *
     * @param startDate the start date of the range (inclusive).
     * @param endDate   the end date of the range (inclusive).
     * @return a list of schedules within the specified date range.
     */
    List<Schedule> findByEffectiveDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Finds an active schedule for a specific day of the week.
     * Returns only schedules that are marked as active.
     *
     * @param dayOfWeek the day of the week.
     * @return an Optional containing the active schedule if found, or empty if not.
     */
    Optional<Schedule> findByDayOfWeekAndActiveTrue(DayOfWeek dayOfWeek);
}
