package lt.ca.javau11.service;

import jakarta.persistence.EntityNotFoundException;
import lt.ca.javau11.entity.SpecialSchedule;
import lt.ca.javau11.repository.SpecialScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing special schedules.
 * Provides methods for retrieving, creating/updating, and deleting special schedules.
 */
@Service
public class SpecialScheduleService {

    private static final Logger log = LoggerFactory.getLogger(SpecialScheduleService.class);
    private final SpecialScheduleRepository specialScheduleRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param specialScheduleRepository the repository for special schedules.
     */
    public SpecialScheduleService(SpecialScheduleRepository specialScheduleRepository) {
        this.specialScheduleRepository = specialScheduleRepository;
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
                .orElseThrow(() -> {
                    log.error("Special schedule with ID {} not found", id);
                    return new EntityNotFoundException("Special schedule not found with ID: " + id);
                });
    }

    /**
     * Retrieves a special schedule by its date.
     *
     * @param date the date for the special schedule.
     * @return the found special schedule.
     * @throws EntityNotFoundException if no special schedule is found for the given date.
     */
    public SpecialSchedule getSpecialScheduleByDate(LocalDate date) {
        log.info("Searching for special schedule for date: {}", date);
        return specialScheduleRepository.findBySpecialDate(date)
                .orElseThrow(() -> {
                    log.error("Special schedule not found for date: {}", date);
                    return new EntityNotFoundException("Special schedule not found for date: " + date);
                });
    }

    /**
     * Creates or updates a special schedule.
     *
     * @param specialSchedule the special schedule to be saved.
     * @return the saved special schedule.
     */
    public SpecialSchedule saveSpecialSchedule(SpecialSchedule specialSchedule) {
        log.info("Saving special schedule: {}", specialSchedule);
        SpecialSchedule savedSchedule = specialScheduleRepository.save(specialSchedule);
        log.info("Special schedule saved with ID: {}", savedSchedule.getId());
        return savedSchedule;
    }

    /**
     * Deletes a special schedule by its ID.
     *
     * @param id the ID of the special schedule to delete.
     * @throws EntityNotFoundException if no special schedule exists with the given ID.
     */
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
     * Retrieves all special schedules.
     *
     * @return a list of all special schedules.
     */
    public List<SpecialSchedule> getAllSpecialSchedules() {
        log.info("Fetching all special schedules");
        List<SpecialSchedule> schedules = specialScheduleRepository.findAll();
        log.info("Found {} special schedules", schedules.size());
        return schedules;
    }
}
