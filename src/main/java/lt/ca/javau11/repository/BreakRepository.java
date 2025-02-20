package lt.ca.javau11.repository;

import lt.ca.javau11.entity.Break;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Break} entities.
 * Provides optimized methods for retrieving breaks with their associated schedules.
 * All methods use fetch joins to prevent N+1 query problems and LazyInitializationException.
 *
 * @author Lika Makejeva
 * @version 2.0
 * @since 1.0
 */
public interface BreakRepository extends JpaRepository<Break, Long> {

    /**
     * Finds a break by its name, eagerly loading the associated schedule.
     *
     * @param name The name of the break to search for
     * @return An Optional containing the Break with its schedule if found, or empty if not found
     */
    @Query("SELECT b FROM Break b LEFT JOIN FETCH b.schedule WHERE b.name = :name")
    Optional<Break> findByName(@Param("name") String name);

    /**
     * Finds a break by its start time, eagerly loading the associated schedule.
     *
     * @param startTime The start time of the break
     * @return An Optional containing the Break with its schedule if found, or empty if not found
     */
    @Query("SELECT b FROM Break b LEFT JOIN FETCH b.schedule WHERE b.startTime = :startTime")
    Optional<Break> findByStartTime(@Param("startTime") LocalTime startTime);

    /**
     * Finds a break by its ID, eagerly loading the associated schedule.
     * This method prevents LazyInitializationException when accessing schedule outside transaction.
     *
     * @param id The ID of the break to find
     * @return An Optional containing the Break with its schedule if found, or empty if not found
     */
    @Query("SELECT b FROM Break b LEFT JOIN FETCH b.schedule WHERE b.id = :id")
    Optional<Break> findByIdWithSchedule(@Param("id") Long id);

    /**
     * Retrieves all breaks with their associated schedules.
     * Uses DISTINCT to prevent duplicate results when using fetch join.
     *
     * @return A list of all breaks with their schedules eagerly loaded
     */
    @Query("SELECT DISTINCT b FROM Break b LEFT JOIN FETCH b.schedule")
    List<Break> findAllWithSchedules();

    /**
     * Finds all breaks associated with a specific schedule ID, eagerly loading the schedule data.
     *
     * @param scheduleId The ID of the schedule to find breaks for
     * @return A list of breaks belonging to the given schedule, with schedule data loaded
     */
    @Query("SELECT b FROM Break b LEFT JOIN FETCH b.schedule WHERE b.schedule.id = :scheduleId")
    List<Break> findByScheduleIdWithSchedule(@Param("scheduleId") Long scheduleId);

    /**
     * Finds all breaks occurring on a specific date, eagerly loading their schedules.
     * Results are ordered by start time for consistent presentation.
     *
     * @param date The date for which to retrieve breaks
     * @return A list of breaks occurring on the given date, ordered by start time
     */
    @Query("SELECT b FROM Break b LEFT JOIN FETCH b.schedule " +
           "WHERE b.schedule.effectiveDate = :date ORDER BY b.startTime")
    List<Break> findBreaksByScheduleDateWithSchedule(@Param("date") LocalDate date);

    /**
     * Finds all breaks within a date range, eagerly loading their schedules.
     * Results are ordered by start time for consistent presentation.
     *
     * @param start The start date (inclusive)
     * @param end The end date (inclusive)
     * @return A list of breaks occurring within the date range, ordered by start time
     */
    @Query("SELECT b FROM Break b LEFT JOIN FETCH b.schedule " +
           "WHERE b.schedule.effectiveDate BETWEEN :start AND :end ORDER BY b.startTime")
    List<Break> findBreaksByScheduleDateRangeWithSchedule(
            @Param("start") LocalDate start, 
            @Param("end") LocalDate end);

    /**
     * Checks if there are any breaks scheduled for a specific date.
     * This is an optimized query that doesn't load any entities, just checks existence.
     *
     * @param date The date to check for breaks
     * @return true if at least one break exists for the given date, false otherwise
     */
    boolean existsBySchedule_EffectiveDate(LocalDate date);
}