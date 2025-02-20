package lt.ca.javau11.service;

import jakarta.persistence.EntityNotFoundException;
import lt.ca.javau11.entity.SpecialSchedule;
import lt.ca.javau11.repository.SpecialScheduleRepository;
import lt.ca.javau11.validation.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing special schedules.
 * Provides methods for retrieving, creating, updating, and deleting special schedules.
 */
@Service
public class SpecialScheduleService {

    private static final Logger log = LoggerFactory.getLogger(SpecialScheduleService.class);
    private final SpecialScheduleRepository specialScheduleRepository;
    private final ValidationService validationService;

    /**
     * Constructor for dependency injection.
     *
     * @param specialScheduleRepository the repository for special schedules.
     * @param validationService the validation service for validating schedules.
     */
    public SpecialScheduleService(SpecialScheduleRepository specialScheduleRepository, ValidationService validationService) {
        this.specialScheduleRepository = specialScheduleRepository;
        this.validationService = validationService;
    }

    /**
     * Retrieves a special schedule by its ID.
     *
     * @param id the ID of the special schedule.
     * @return the found special schedule.
     * @throws EntityNotFoundException if no special schedule is found with the given ID.
     */
    public SpecialSchedule getSpecialScheduleById(Long id) {
        log.info("Searching for special schedule with ID: {}", id);
        return specialScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Special schedule not found with ID: " + id));
    }

    /**
     * Creates a new special schedule.
     *
     * @param specialSchedule the special schedule to be created.
     * @return the created special schedule.
     */
    @Transactional
    public SpecialSchedule createSpecialSchedule(SpecialSchedule specialSchedule) {
        validationService.validateSpecialSchedule(specialSchedule);
        log.info("Creating new special schedule: {}", specialSchedule);
        return specialScheduleRepository.save(specialSchedule);
    }

    /**
     * Updates an existing special schedule.
     *
     * @param id the ID of the special schedule to update.
     * @param specialSchedule the updated special schedule data.
     * @return the updated special schedule.
     * @throws EntityNotFoundException if the special schedule does not exist.
     */
    @Transactional
    public SpecialSchedule updateSpecialSchedule(Long id, SpecialSchedule specialSchedule) {
        log.info("Updating special schedule with ID: {}", id);
        SpecialSchedule existingSchedule = getSpecialScheduleById(id);
        validationService.validateSpecialSchedule(specialSchedule);
        
        existingSchedule.setSpecialDate(specialSchedule.getSpecialDate());
        existingSchedule.setSchedule(specialSchedule.getSchedule());
        existingSchedule.setLessons(specialSchedule.getLessons());
        existingSchedule.setDescription(specialSchedule.getDescription());
        
        log.info("Special schedule with ID {} updated successfully", id);
        return specialScheduleRepository.save(existingSchedule);
    }
    

    /**
     * Retrieves all special schedules within a given date range.
     *
     * @param startDate the start date of the range (inclusive).
     * @param endDate the end date of the range (inclusive).
     * @return a list of special schedules within the specified range.
     */
    public List<SpecialSchedule> getSpecialScheduleForDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching special schedules between {} and {}", startDate, endDate);
        return specialScheduleRepository.findBySpecialDateBetween(startDate, endDate);
    }

    /**
     * Retrieves a special schedule by its date.
     *
     * @param date the date for the special schedule.
     * @return the found special schedule or null if not found.
     */
    public SpecialSchedule getSpecialScheduleByDate(LocalDate date) {
        log.info("Searching for special schedule for date: {}", date);
        return specialScheduleRepository.findBySpecialDate(date)
                .orElse(null);
    }

    /**
     * Deletes a special schedule by its ID.
     *
     * @param id the ID of the special schedule to delete.
     */
    @Transactional
    public void deleteSpecialSchedule(Long id) {
        log.info("Deleting special schedule with ID: {}", id);
        if (!specialScheduleRepository.existsById(id)) {
            log.error("Special schedule with ID {} does not exist", id);
            throw new EntityNotFoundException("Special schedule not found with ID: " + id);
        }
        specialScheduleRepository.deleteById(id);
        log.info("Special schedule with ID {} successfully deleted", id);
    }

    /**
     * Deletes all special schedules associated with a given schedule ID.
     * This ensures that when a main schedule is deleted, 
     * all special schedules referring to it are also removed.
     *
     * @param scheduleId the ID of the schedule.
     */
    @Transactional
    public void deleteSpecialSchedulesBySchedule(Long scheduleId) {
        log.info("Deleting all special schedules linked to schedule ID: {}", scheduleId);
        specialScheduleRepository.deleteByScheduleId(scheduleId);
    }

    /**
     * Retrieves all special schedules.
     *
     * @return a list of all special schedules.
     */
    public List<SpecialSchedule> getAllSpecialSchedules() {
        log.info("Fetching all special schedules");
        return specialScheduleRepository.findAll();
    }

    /**
     * Checks if there is an active special schedule for the given date.
     * This method determines whether a special schedule is set for a specific day.
     *
     * @param date the date to check for an active special schedule.
     * @return true if an active special schedule exists, false otherwise.
     */
    public boolean hasActiveSpecialSchedule(LocalDate date) {
        log.info("Checking for active special schedule on {}", date);
        return specialScheduleRepository.existsBySpecialDate(date);
    }
}
