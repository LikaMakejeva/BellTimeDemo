package lt.ca.javau11.repository;

import lt.ca.javau11.entity.SpecialSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and manipulating SpecialSchedule entities.
 * Provides methods for querying special schedules based on different criteria.
 */
public interface SpecialScheduleRepository extends JpaRepository<SpecialSchedule, Long> {

    /**
     * Finds a SpecialSchedule by its special date.
     *
     * @param specialDate the special date for the schedule.
     * @return an Optional containing the SpecialSchedule if found, or empty if not found.
     */
	Optional<SpecialSchedule> findBySpecialDate(LocalDate date);

    
    /**
     * Deletes all special schedules associated with the given schedule ID.
     * This method ensures that when a main schedule is deleted, all related special schedules are removed.
     *
     * @param scheduleId the ID of the schedule whose special schedules should be deleted.
     */
    void deleteByScheduleId(Long scheduleId);

    /**
     * Checks if there is an active special schedule for the given date.
     * This method verifies whether a special schedule exists for the specified date.
     *
     * @param today the date to check for an active special schedule.
     * @return true if an active special schedule exists for the given date, false otherwise.
     */
    boolean existsBySpecialDate(LocalDate today);


	/**
	 * Retrieves a list of special schedules that fall within the specified date range.
	 * This method is useful for fetching special schedules for a given period (e.g., a week or a month).
	 *
	 * @param startDate the start date of the range (inclusive).
	 * @param endDate the end date of the range (inclusive).
	 * @return a list of special schedules that exist within the given date range.
	 */
	List<SpecialSchedule> findBySpecialDateBetween(LocalDate startDate, LocalDate endDate);
}   