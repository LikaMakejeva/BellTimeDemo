package lt.ca.javau11.repository;

import lt.ca.javau11.entity.Schedule;
import lt.ca.javau11.entity.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Schedule} entities.
 * <p>
 * Provides methods for CRUD operations and custom queries related to school schedules.
 * Supports querying schedules by date, type, and activity status.
 * </p>
 *
 * @author Lika Makejeva
 * @version 1.0
 * @since 1.0
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * Finds a schedule that becomes effective on a specific date.
     *
     * @param effectiveDate The date when the schedule takes effect.
     * @return An {@link Optional} containing the found schedule, or empty if none exists.
     */
    Optional<Schedule> findByEffectiveDate(LocalDate effectiveDate);

    /**
     * Retrieves all schedules for a specific day of the week.
     *
     * @param dayOfWeek The day of the week (e.g., MONDAY, TUESDAY).
     * @return A list of schedules applicable to the specified day.
     */
    List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek);

    /**
     * Retrieves all schedules that are effective within a given date range.
     *
     * @param startDate The start date of the range (inclusive).
     * @param endDate   The end date of the range (inclusive).
     * @return A list of schedules that fall within the specified date range.
     */
    List<Schedule> findByEffectiveDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Finds an active schedule for a given day of the week.
     * <p>
     * Only schedules that are marked as active will be returned.
     * </p>
     *
     * @param dayOfWeek The day of the week.
     * @return An {@link Optional} containing the active schedule if found, or empty if none exists.
     */
    Optional<Schedule> findByDayOfWeekAndActiveTrue(DayOfWeek dayOfWeek);

    /**
     * Finds all schedules of a specific type.
     * <p>
     * This method allows filtering schedules by their type, such as REGULAR or SPECIAL.
     * </p>
     *
     * @param scheduleType The type of schedule (e.g., REGULAR, SPECIAL).
     * @return A list of schedules matching the specified type.
     */
    List<Schedule> findByScheduleType(ScheduleType scheduleType);
}
