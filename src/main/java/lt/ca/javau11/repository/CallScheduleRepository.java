package lt.ca.javau11.repository;

import lt.ca.javau11.entity.CallSchedule;
import lt.ca.javau11.entity.CallType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

/**
 * Repository interface for managing CallSchedule entities.
 * Provides methods to retrieve call schedules based on different criteria.
 */
public interface CallScheduleRepository extends JpaRepository<CallSchedule, Long> {

    /**
     * Retrieves all call schedules along with their associated lesson and break period.
     * Uses EntityGraph to fetch related entities in a single query.
     *
     * @return List of CallSchedule entities with relationships eagerly loaded.
     */
    @Override
    @EntityGraph(attributePaths = {"lesson", "breakPeriod"})
    List<CallSchedule> findAll();

    /**
     * Finds a call schedule by its ID with eagerly loaded relationships.
     *
     * @param id The ID of the call schedule.
     * @return The found CallSchedule entity with relationships eagerly loaded.
     */
    @EntityGraph(attributePaths = {"lesson", "breakPeriod"})
    CallSchedule findById(long id);

    /**
     * Finds all call schedules of a specific type.
     *
     * @param callType The type of the call schedule.
     * @return List of CallSchedule entities matching the specified type.
     */
    List<CallSchedule> findByCallType(CallType callType);

    /**
     * Finds all call schedules that fall within a specific time range.
     *
     * @param startTime The start of the time range.
     * @param endTime The end of the time range.
     * @return List of CallSchedule entities within the time range.
     */
    List<CallSchedule> findByCallTimeBetween(LocalTime startTime, LocalTime endTime);

    /**
     * Finds all call schedules associated with a specific break period.
     *
     * @param breakId The ID of the break period.
     * @return List of CallSchedule entities associated with the given break.
     */
    @Query("SELECT cs FROM CallSchedule cs WHERE cs.breakPeriod.id = :breakId")
    List<CallSchedule> findByBreakPeriodId(@Param("breakId") Long breakId);

    /**
     * Finds all call schedules associated with a specific lesson.
     *
     * @param lessonId The ID of the lesson.
     * @return List of CallSchedule entities associated with the given lesson.
     */
    @Query("SELECT cs FROM CallSchedule cs WHERE cs.lesson.id = :lessonId")
    List<CallSchedule> findByLessonId(@Param("lessonId") Long lessonId);

    /**
     * Checks if a call schedule exists at a specific time.
     *
     * @param callTime The time to check.
     * @return true if a schedule exists at the specified time, false otherwise.
     */
    boolean existsByCallTime(LocalTime callTime);

    /**
    * Retrieves all call schedules associated with a specific schedule ID.
    * 
    * * @param id The ID of the schedule for which call schedules should be retrieved.
    * @return An {@link Iterable} containing all call schedules associated with the given schedule ID.
    */
    List<CallSchedule> findByScheduleId(Long id);
}
