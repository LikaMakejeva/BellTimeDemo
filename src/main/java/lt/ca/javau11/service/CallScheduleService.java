package lt.ca.javau11.service;

import lt.ca.javau11.entity.CallSchedule;
import lt.ca.javau11.entity.CallType;
import lt.ca.javau11.repository.CallScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service for managing the call schedule.
 * Includes methods for creating, finding, deleting, and working with call schedules.
 */
@Service
public class CallScheduleService {

    private static final Logger log = LoggerFactory.getLogger(CallScheduleService.class);
    private final CallScheduleRepository callScheduleRepository;

    // Constructor for dependency injection
    public CallScheduleService(CallScheduleRepository callScheduleRepository) {
        this.callScheduleRepository = callScheduleRepository;
    }

    /**
     * Retrieve all call schedules.
     *
     * @return list of all call schedules.
     */
    public List<CallSchedule> findAll() {
        log.info("Fetching all call schedules");
        List<CallSchedule> callSchedules = callScheduleRepository.findAll();
        log.info("Found {} call schedules", callSchedules.size());
        return callSchedules;
    }

    /**
     * Save a new call schedule.
     *
     * @param callSchedule the call schedule object to save.
     * @return the saved call schedule.
     * @throws IllegalArgumentException if the callSchedule is null.
     */
    public CallSchedule save(CallSchedule callSchedule) {
        if (callSchedule == null) {
            log.error("Attempt to save null call schedule");
            throw new IllegalArgumentException("CallSchedule cannot be null");
        }
        log.info("Saving new call schedule: {}", callSchedule);
        return callScheduleRepository.save(callSchedule);
    }

    /**
     * Find a call schedule by ID.
     *
     * @param id the ID of the call schedule.
     * @return the found call schedule.
     * @throws IllegalArgumentException if the call schedule with the specified ID is not found.
     */
    public CallSchedule findById(Long id) {
        log.info("Searching for CallSchedule with ID: {}", id);
        return callScheduleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("CallSchedule with ID {} not found", id);
                    return new IllegalArgumentException("CallSchedule with ID " + id + " not found");
                });
    }

    /**
     * Delete a call schedule by ID.
     *
     * @param id the ID of the call schedule to delete.
     * @throws IllegalArgumentException if the call schedule with the specified ID does not exist.
     */
    @Transactional
    public void delete(Long id) {
        log.info("Deleting call schedule with ID: {}", id);
        if (!callScheduleRepository.existsById(id)) {
            log.error("Call schedule with ID {} does not exist", id);
            throw new IllegalArgumentException("CallSchedule with ID " + id + " does not exist");
        }
        callScheduleRepository.deleteById(id);
        log.info("Call schedule with ID {} successfully deleted", id);
    }

    /**
     * Find call schedules by call type.
     *
     * @param callType the type of the call (e.g., lesson start, break).
     * @return list of call schedules with the specified type.
     * @throws IllegalArgumentException if the callType is null.
     */
    public List<CallSchedule> findByCallType(CallType callType) {
        if (callType == null) {
            log.error("CallType cannot be null");
            throw new IllegalArgumentException("CallType cannot be null");
        }
        log.info("Searching for call schedules with type: {}", callType);
        List<CallSchedule> callSchedules = callScheduleRepository.findByCallType(callType);
        log.info("Found {} call schedules for type: {}", callSchedules.size(), callType);
        return callSchedules;
    }

    /**
     * Check if a call schedule exists by ID.
     *
     * @param id the ID of the call schedule.
     * @return true if the schedule exists, false otherwise.
     */
    public boolean existsById(Long id) {
        boolean exists = callScheduleRepository.existsById(id);
        log.info("Checking existence of call schedule with ID {}: {}", id, exists);
        return exists;
    }
}
