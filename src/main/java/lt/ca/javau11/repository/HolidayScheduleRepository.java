package lt.ca.javau11.repository;

import lt.ca.javau11.entity.HolidaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing HolidaySchedule entities.
 * Provides methods to retrieve holiday schedules by date, ID, and date range.
 */
public interface HolidayScheduleRepository extends JpaRepository<HolidaySchedule, Long> {

    /**
     * Finds a holiday schedule by ID with all related entities fetched.
     * This prevents LazyInitializationException when Open Session in View (OSIV) is disabled.
     *
     * @param id the ID of the holiday schedule.
     * @return an optional containing the holiday schedule with fetched related entities.
     */
    @Query("SELECT hs FROM HolidaySchedule hs LEFT JOIN FETCH hs.relatedEntity WHERE hs.id = :id")
    Optional<HolidaySchedule> findByIdWithDetails(@Param("id") Long id);

    /**
     * Finds a holiday schedule by its holiday date.
     *
     * @param holidayDate the date of the holiday.
     * @return an optional containing the holiday schedule if found.
     */
    Optional<HolidaySchedule> findByHolidayDate(LocalDate holidayDate);

    /**
     * Finds all holiday schedules within a given date range.
     *
     * @param startDate the start date (inclusive).
     * @param endDate   the end date (inclusive).
     * @return a list of holiday schedules within the specified date range.
     */
    List<HolidaySchedule> findByHolidayDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Checks if a holiday schedule exists for the given date.
     *
     * @param holidayDate the date to check.
     * @return true if a holiday exists on the given date, false otherwise.
     */
    boolean existsByHolidayDate(LocalDate holidayDate);
}
