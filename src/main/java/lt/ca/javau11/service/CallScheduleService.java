package lt.ca.javau11.service;

import lt.ca.javau11.entity.CallSchedule;
import lt.ca.javau11.entity.CallType;
import lt.ca.javau11.repository.CallScheduleRepository;
import lt.ca.javau11.validation.ValidationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Service for managing school bell schedules (CallSchedule).
 * Provides CRUD operations and various search methods.
 */
@Service
@Transactional(readOnly = true)
public class CallScheduleService {
    private static final Logger log = LoggerFactory.getLogger(CallScheduleService.class);
    private final CallScheduleRepository callScheduleRepository;
    private final ValidationService validationService;

    public CallScheduleService(CallScheduleRepository callScheduleRepository, ValidationService validationService) {
        this.callScheduleRepository = callScheduleRepository;
        this.validationService = validationService;
    }

    /**
     * Retrieves all call schedules from the database.
     *
     * @return List of all CallSchedule entities
     */
    public List<CallSchedule> getAllCallSchedules() {
        log.info("Fetching all call schedules");
        return callScheduleRepository.findAll();
    }

    /**
     * Retrieves a call schedule by its ID.
     *
     * @param id The ID of the call schedule
     * @return CallSchedule if found, otherwise throws an exception
     * @throws IllegalArgumentException if the call schedule is not found
     */
    public CallSchedule getCallScheduleById(Long id) {
        log.info("Fetching call schedule with ID: {}", id);
        return callScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Call schedule not found"));
    }

    /**
     * Saves a new or updates an existing call schedule.
     *
     * @param callSchedule The call schedule to save
     * @return The saved CallSchedule entity
     */
    @Transactional
    public CallSchedule saveCallSchedule(CallSchedule callSchedule) {
        log.info("Saving call schedule: {}", callSchedule);
        validationService.validateCallSchedule(callSchedule);
        return callScheduleRepository.save(callSchedule);
    }

    /**
     * Updates an existing call schedule.
     *
     * @param id The ID of the call schedule to update
     * @param callSchedule The call schedule with updated data
     * @return The updated CallSchedule entity
     * @throws IllegalArgumentException if the call schedule does not exist
     */
    @Transactional
    public CallSchedule updateCallSchedule(Long id, CallSchedule callSchedule) {
        log.info("Updating call schedule with ID: {}", id);
        CallSchedule existingSchedule = getCallScheduleById(id);

        existingSchedule.setCallTime(callSchedule.getCallTime());
        existingSchedule.setCallType(callSchedule.getCallType());
        existingSchedule.setLesson(callSchedule.getLesson());
        existingSchedule.setBreakPeriod(callSchedule.getBreakPeriod());

        validationService.validateCallSchedule(existingSchedule);
        return callScheduleRepository.save(existingSchedule);
    }

    /**
     * Deletes a call schedule by its ID.
     *
     * @param id The ID of the call schedule to delete
     * @return true if the call schedule was deleted, false if not found
     */
    @Transactional
    public boolean deleteCallScheduleById(Long id) {
        log.info("Deleting call schedule with ID: {}", id);
        if (!callScheduleRepository.existsById(id)) {
            log.warn("Call schedule with ID {} not found", id);
            return false;
        }
        callScheduleRepository.deleteById(id);
        return true;
    }

    /**
     * Finds all call schedules by call type.
     *
     * @param callType The type of the call schedule
     * @return List of CallSchedule entities of the given type
     */
    public List<CallSchedule> findAllByCallType(CallType callType) {
        log.info("Searching for call schedules with type: {}", callType);
        return callScheduleRepository.findByCallType(callType);
    }

    /**
     * Finds all call schedules within a specified time range.
     *
     * @param startTime Start time of the range
     * @param endTime End time of the range
     * @return List of CallSchedule entities within the time range
     */
    public List<CallSchedule> findAllByTimeRange(LocalTime startTime, LocalTime endTime) {
        log.info("Searching for call schedules between {} and {}", startTime, endTime);
        return callScheduleRepository.findByCallTimeBetween(startTime, endTime);
    }

    /**
     * Finds all call schedules associated with a specific break period.
     *
     * @param breakId The ID of the break period
     * @return List of CallSchedule entities associated with the break
     */
    public List<CallSchedule> findAllByBreak(Long breakId) {
        log.info("Searching for call schedules for break ID: {}", breakId);
        return callScheduleRepository.findByBreakPeriodId(breakId);
    }

    /**
     * Finds all call schedules for a specific date.
     *
     * @param date The date to find schedules for
     * @return List of CallSchedule entities for the given date
     * @throws IllegalArgumentException if date is null
     */
    public List<CallSchedule> getCallSchedulesForDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        log.info("Fetching call schedules for date: {}", date);
        return callScheduleRepository.findByCallTimeBetween(LocalTime.MIN, LocalTime.MAX);
    }

    /**
     * Finds all call schedules associated with a lesson.
     *
     * @param lessonId The ID of the lesson
     * @return List of CallSchedule entities associated with the lesson
     */
    public List<CallSchedule> findAllByLesson(Long lessonId) {
        log.info("Searching for call schedules for lesson ID: {}", lessonId);
        return callScheduleRepository.findByLessonId(lessonId);
    }

    /**
     * Checks if a call schedule exists at a specific time.
     *
     * @param callTime The time to check
     * @return true if a schedule exists at the specified time, false otherwise
     */
    public boolean existsAtTime(LocalTime callTime) {
        log.info("Checking for call schedule at time: {}", callTime);
        return callScheduleRepository.existsByCallTime(callTime);
    }
}
