package lt.ca.javau11.repository;

import lt.ca.javau11.entity.HolidaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository interface for accessing and manipulating HolidaySchedule entities.
 * Provides basic CRUD operations and a custom query method.
 */
public interface HolidayScheduleRepository extends JpaRepository<HolidaySchedule, Long> {

    /**
     * Finds a HolidaySchedule by its holiday date.
     *
     * @param holidayDate the holiday date.
     * @return an Optional containing the HolidaySchedule if found, or empty if not found.
     */
    Optional<HolidaySchedule> findByHolidayDate(LocalDate holidayDate);
}
