package lt.ca.javau11.service;

import lt.ca.javau11.entity.HolidaySchedule;
import lt.ca.javau11.repository.HolidayScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing holiday schedules.
 * Provides methods to create, retrieve, update, and delete holiday schedules.
 */
@Service
public class HolidayScheduleService {

    private static final Logger log = LoggerFactory.getLogger(HolidayScheduleService.class);
    private final HolidayScheduleRepository holidayScheduleRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param holidayScheduleRepository the repository for holiday schedules.
     */
    public HolidayScheduleService(HolidayScheduleRepository holidayScheduleRepository) {
        this.holidayScheduleRepository = holidayScheduleRepository;
    }

    /**
     * Retrieves all holiday schedules.
     *
     * @return list of all holiday schedules.
     */
    public List<HolidaySchedule> findAll() {
        log.info("Fetching all holiday schedules");
        List<HolidaySchedule> schedules = holidayScheduleRepository.findAll();
        log.info("Found {} holiday schedules", schedules.size());
        return schedules;
    }

    /**
     * Creates a new holiday schedule.
     * The provided holiday schedule should not have an ID set.
     *
     * @param holidaySchedule the holiday schedule to create.
     * @return the created holiday schedule.
     * @throws IllegalArgumentException if the holiday schedule already has an ID.
     */
    public HolidaySchedule createHolidaySchedule(HolidaySchedule holidaySchedule) {
        if (holidaySchedule == null) {
            log.error("Attempt to create a null holiday schedule");
            throw new IllegalArgumentException("HolidaySchedule cannot be null");
        }
        if (holidaySchedule.getId() != null) {
            log.error("Attempt to create a holiday schedule with an existing ID: {}", holidaySchedule.getId());
            throw new IllegalArgumentException("New holiday schedule should not have an ID");
        }
        log.info("Creating new holiday schedule: {}", holidaySchedule);
        return holidayScheduleRepository.save(holidaySchedule);
    }

    /**
     * Updates an existing holiday schedule.
     * The provided holiday schedule must have a valid ID.
     *
     * @param holidaySchedule the holiday schedule with updated data.
     * @return the updated holiday schedule.
     * @throws IllegalArgumentException if the holiday schedule has no ID or is not found.
     */
    @Transactional
    public HolidaySchedule updateHolidaySchedule(HolidaySchedule holidaySchedule) {
        if (holidaySchedule == null) {
            log.error("Attempt to update a null holiday schedule");
            throw new IllegalArgumentException("HolidaySchedule cannot be null");
        }
        if (holidaySchedule.getId() == null) {
            log.error("Attempt to update a holiday schedule without an ID");
            throw new IllegalArgumentException("Holiday schedule must have an ID for update");
        }
        // Verify that the schedule exists
        if (!holidayScheduleRepository.existsById(holidaySchedule.getId())) {
            log.error("Holiday schedule with ID {} not found for update", holidaySchedule.getId());
            throw new IllegalArgumentException("Holiday schedule not found with ID: " + holidaySchedule.getId());
        }
        log.info("Updating holiday schedule with ID {}: {}", holidaySchedule.getId(), holidaySchedule);
        return holidayScheduleRepository.save(holidaySchedule);
    }

    /**
     * Retrieves a holiday schedule by its ID.
     *
     * @param id the ID of the holiday schedule.
     * @return the found holiday schedule.
     * @throws IllegalArgumentException if the holiday schedule is not found.
     */
    public HolidaySchedule findById(Long id) {
        log.info("Searching for holiday schedule with ID: {}", id);
        return holidayScheduleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Holiday schedule with ID {} not found", id);
                    return new IllegalArgumentException("Holiday schedule not found with ID: " + id);
                });
    }

    /**
     * Retrieves a holiday schedule by its holiday date.
     *
     * @param holidayDate the date of the holiday.
     * @return the found holiday schedule.
     * @throws IllegalArgumentException if the holiday schedule is not found.
     */
    public HolidaySchedule findByHolidayDate(LocalDate holidayDate) {
        log.info("Searching for holiday schedule with date: {}", holidayDate);
        return holidayScheduleRepository.findByHolidayDate(holidayDate)
                .orElseThrow(() -> {
                    log.error("Holiday schedule not found for date: {}", holidayDate);
                    return new IllegalArgumentException("Holiday schedule not found for date: " + holidayDate);
                });
    }

    /**
     * Deletes a holiday schedule by its ID.
     *
     * @param id the ID of the holiday schedule to delete.
     * @throws IllegalArgumentException if the holiday schedule does not exist.
     */
    @Transactional
    public void delete(Long id) {
        log.info("Deleting holiday schedule with ID: {}", id);
        if (!holidayScheduleRepository.existsById(id)) {
            log.error("Holiday schedule with ID {} does not exist", id);
            throw new IllegalArgumentException("Holiday schedule not found with ID: " + id);
        }
        holidayScheduleRepository.deleteById(id);
        log.info("Holiday schedule with ID {} successfully deleted", id);
    }
}
