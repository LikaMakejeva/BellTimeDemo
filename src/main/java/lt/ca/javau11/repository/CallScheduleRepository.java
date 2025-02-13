package lt.ca.javau11.repository;

import lt.ca.javau11.entity.CallSchedule;
import lt.ca.javau11.entity.CallType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and manipulating CallSchedule entities.
 * Provides methods for querying the school bell schedule based on different criteria.
 */
public interface CallScheduleRepository extends JpaRepository<CallSchedule, Long> {

    /**
     * Finds all CallSchedule entities by the specified call type.
     *
     * @param callType the type of the call (e.g. lesson start, break, etc.).
     * @return a list of CallSchedule entities that match the specified call type.
     */
    List<CallSchedule> findByCallType(CallType callType);

    /**
     * Finds all CallSchedule entities where the call time is between the specified start and end times.
     *
     * @param startTime the starting time of the period.
     * @param endTime the ending time of the period.
     * @return a list of CallSchedule entities that fall within the specified time range.
     */
    List<CallSchedule> findByCallTimeBetween(LocalTime startTime, LocalTime endTime);

    /**
     * Finds a CallSchedule entity by the associated lesson ID.
     *
     * @param lessonId the ID of the lesson.
     * @return an Optional containing the found CallSchedule, or empty if not found.
     */
    Optional<CallSchedule> findByLessonId(Long lessonId);

    /**
     * Finds a CallSchedule entity by the associated break period ID.
     *
     * @param breakId the ID of the break period.
     * @return an Optional containing the found CallSchedule, or empty if not found.
     */
    Optional<CallSchedule> findByBreakPeriodId(Long breakId);

    /**
     * Finds all CallSchedule entities that are scheduled to ring at the specified time.
     *
     * @param callTime the exact time of the call (rounded to minutes).
     * @return a list of CallSchedule entities matching the specified time.
     */
    List<CallSchedule> findByCallTime(LocalTime callTime);
}
