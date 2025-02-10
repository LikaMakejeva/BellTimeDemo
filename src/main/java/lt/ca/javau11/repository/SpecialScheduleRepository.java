package lt.ca.javau11.repository;

import lt.ca.javau11.entity.SpecialSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
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
    Optional<SpecialSchedule> findBySpecialDate(LocalDate specialDate);
}
