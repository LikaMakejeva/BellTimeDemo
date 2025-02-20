package lt.ca.javau11.service;

import lt.ca.javau11.entity.Break;
import lt.ca.javau11.entity.SpecialSchedule;
import lt.ca.javau11.repository.BreakRepository;
import lt.ca.javau11.repository.SpecialScheduleRepository;
import lt.ca.javau11.validation.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing break periods in the school schedule.
 * Provides business logic for CRUD operations on break periods.
 *
 * @author Lika Makejeva
 * @version 2.0
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class BreakService {

    private static final Logger log = LoggerFactory.getLogger(BreakService.class);
    private final BreakRepository breakRepository;
    private final SpecialScheduleRepository specialScheduleRepository;
    private final ValidationService validationService;

    public BreakService(BreakRepository breakRepository, 
                       SpecialScheduleRepository specialScheduleRepository, 
                       ValidationService validationService) {
        this.breakRepository = breakRepository;
        this.specialScheduleRepository = specialScheduleRepository;
        this.validationService = validationService;
    }

    /**
     * Retrieves all break periods with their associated schedules.
     *
     * @return List of all break periods
     */
    public List<Break> findAllBreaks() {
        log.info("Fetching all break periods");
        List<Break> breaks = breakRepository.findAllWithSchedules();
        log.info("Found {} break periods", breaks.size());
        return breaks;
    }

    /**
     * Retrieves a break period by its name.
     *
     * @param name The name of the break to search for
     * @return Optional containing the break if found
     * @throws IllegalArgumentException if name is null or empty
     */
    public Optional<Break> getBreakByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.error("Attempt to search for a break with an invalid name");
            throw new IllegalArgumentException("Break name cannot be null or empty");
        }
        log.info("Searching for break with name: {}", name);
        return breakRepository.findByName(name);
    }

    /**
     * Retrieves a break period by its ID.
     *
     * @param id The ID of the break period
     * @return The found break period
     * @throws IllegalArgumentException if no break is found with the given ID
     */
    public Break findBreakById(Long id) {
        log.info("Searching for break period with ID: {}", id);
        return breakRepository.findByIdWithSchedule(id)
                .orElseThrow(() -> {
                    log.error("Break period with ID {} not found", id);
                    return new IllegalArgumentException("Break period not found with ID: " + id);
                });
    }

    /**
     * Retrieves all breaks associated with a specific schedule.
     *
     * @param scheduleId The ID of the schedule
     * @return List of breaks for the given schedule
     * @throws IllegalArgumentException if scheduleId is invalid
     */
    public List<Break> findBreaksBySchedule(Long scheduleId) {
        if (scheduleId == null || scheduleId <= 0) {
            log.error("Invalid schedule ID provided: {}", scheduleId);
            throw new IllegalArgumentException("Schedule ID must be a positive number.");
        }
        log.info("Fetching breaks for schedule ID: {}", scheduleId);
        return breakRepository.findByScheduleIdWithSchedule(scheduleId);
    }

    /**
     * Retrieves all breaks for a specific date based on schedule.
     *
     * @param date The date for which breaks should be retrieved
     * @return List of breaks for the given date
     * @throws IllegalArgumentException if date is null
     */
    public List<Break> findBreaksByScheduleDate(LocalDate date) {
        if (date == null) {
            log.error("Attempt to fetch breaks with a null date");
            throw new IllegalArgumentException("Date cannot be null");
        }
        log.info("Fetching breaks for schedule date: {}", date);
        return breakRepository.findBreaksByScheduleDateWithSchedule(date);
    }

    /**
     * Creates a new break period after validation.
     *
     * @param breakPeriod The break period to create
     * @return The created break period
     */
    @Transactional
    public Break createBreak(Break breakPeriod) {
        validationService.validateBreak(breakPeriod);
        log.info("Creating new break period: {}", breakPeriod);
        Break saved = breakRepository.save(breakPeriod);
        return breakRepository.findByIdWithSchedule(saved.getId()).orElseThrow();
    }

    /**
     * Saves a break period after validation.
     *
     * @param breakPeriod The break period to save
     * @return The saved break period
     */
    @Transactional
    public Break saveBreak(Break breakPeriod) {
        validationService.validateBreak(breakPeriod);
        log.info("Saving break period: {}", breakPeriod);
        Break saved = breakRepository.save(breakPeriod);
        return breakRepository.findByIdWithSchedule(saved.getId()).orElseThrow();
    }

    /**
     * Updates an existing break period.
     *
     * @param id The ID of the break period to update
     * @param breakPeriod The updated break period data
     * @return The updated break period
     */
    @Transactional
    public Break updateBreak(Long id, Break breakPeriod) {
        log.info("Updating break period with ID: {}", id);
        Break existingBreak = findBreakById(id);
        validationService.validateBreak(breakPeriod);

        existingBreak.setName(breakPeriod.getName());
        existingBreak.setStartTime(breakPeriod.getStartTime());
        existingBreak.setDuration(breakPeriod.getDuration());

        Break saved = breakRepository.save(existingBreak);
        return breakRepository.findByIdWithSchedule(saved.getId()).orElseThrow();
    }

    /**
     * Deletes a break period by its ID.
     *
     * @param id The ID of the break period to delete
     * @throws IllegalArgumentException if break not found
     */
    @Transactional
    public void deleteBreak(Long id) {
        log.info("Deleting break period with ID: {}", id);
        if (!breakRepository.existsById(id)) {
            log.error("Break period with ID {} not found", id);
            throw new IllegalArgumentException("Break period not found with ID: " + id);
        }
        breakRepository.deleteById(id);
        log.info("Break period with ID {} successfully deleted", id);
    }

    /**
     * Saves a special schedule.
     *
     * @param specialSchedule The special schedule to save
     * @return The saved special schedule
     */
    @Transactional
    public SpecialSchedule saveSpecialSchedule(SpecialSchedule specialSchedule) {
        validationService.validateSpecialSchedule(specialSchedule);
        log.info("Saving special schedule: {}", specialSchedule);
        return specialScheduleRepository.save(specialSchedule);
    }

    /**
     * Deletes a special schedule by ID.
     *
     * @param id The ID of the special schedule to delete
     * @throws IllegalArgumentException if schedule not found
     */
    @Transactional
    public void deleteSpecialSchedule(Long id) {
        log.info("Deleting special schedule with ID: {}", id);
        if (!specialScheduleRepository.existsById(id)) {
            log.error("Special schedule with ID {} does not exist", id);
            throw new IllegalArgumentException("Special schedule not found with ID: " + id);
        }
        specialScheduleRepository.deleteById(id);
        log.info("Special schedule with ID {} successfully deleted", id);
    }

    /**
     * Retrieves a special schedule by ID.
     *
     * @param id The ID of the special schedule
     * @return The found special schedule
     * @throws IllegalArgumentException if schedule not found
     */
    public SpecialSchedule getSpecialScheduleById(Long id) {
        log.info("Fetching special schedule with ID: {}", id);
        return specialScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Special schedule not found with ID: " + id));
    }

    /**
     * Retrieves all breaks within a date range.
     *
     * @param start Start date (inclusive)
     * @param end End date (inclusive)
     * @return List of breaks within the date range
     */
    public List<Break> getBreaksForDateRange(LocalDate start, LocalDate end) {
        log.info("Fetching breaks between {} and {}", start, end);
        return breakRepository.findBreaksByScheduleDateRangeWithSchedule(start, end);
    }

    /**
     * Checks if there are any active breaks for a given date.
     *
     * @param date The date to check
     * @return true if active breaks exist, false otherwise
     * @throws IllegalArgumentException if date is null
     */
    public boolean hasActiveBreaks(LocalDate date) {
        if (date == null) {
            log.error("Attempt to check active breaks with a null date");
            throw new IllegalArgumentException("Date cannot be null");
        }
        log.info("Checking if there are active breaks on {}", date);
        boolean exists = breakRepository.existsBySchedule_EffectiveDate(date);
        log.info("Active breaks on {}: {}", date, exists);
        return exists;
    }

    /**
     * Checks if a break period exists by its ID.
     *
     * @param id The ID to check
     * @return true if break exists, false otherwise
     */
    public boolean breakExistsById(Long id) {
        boolean exists = breakRepository.existsById(id);
        log.info("Checking existence of break period with ID {}: {}", id, exists);
        return exists;
    }

    /**
     * Retrieves all break periods for a specific date.
     *
     * @param date The specific date for which to retrieve breaks
     * @return List of break periods for the given date
     */
    public List<Break> getBreaksForDate(LocalDate date) {
        log.info("Fetching breaks for date: {}", date);
        return breakRepository.findBreaksByScheduleDateWithSchedule(date);
    }
}