package lt.ca.javau11.service;

import lt.ca.javau11.entity.HolidaySchedule;
import lt.ca.javau11.repository.HolidayScheduleRepository;
import lt.ca.javau11.validation.ValidationService;

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
@Transactional(readOnly = true)
public class HolidayScheduleService {

    private static final Logger log = LoggerFactory.getLogger(HolidayScheduleService.class);
    private final HolidayScheduleRepository holidayScheduleRepository;
    private final ValidationService validationService;

    public HolidayScheduleService(HolidayScheduleRepository holidayScheduleRepository, ValidationService validationService) {
        this.holidayScheduleRepository = holidayScheduleRepository;
        this.validationService = validationService;
    }

    /**
     * Retrieves all holiday schedules.
     * 
     * @return a list of all holiday schedules.
     */
    public List<HolidaySchedule> getAllHolidaySchedules() {
        log.info("Fetching all holiday schedules");
        List<HolidaySchedule> schedules = holidayScheduleRepository.findAll();
        log.info("Found {} holiday schedules", schedules.size());
        return schedules;
    }

    /**
     * Retrieves a holiday schedule by its holiday date.
     *
     * @param holidayDate the date of the holiday.
     * @return the found holiday schedule.
     * @throws IllegalArgumentException if the holiday schedule is not found.
     */
    public HolidaySchedule getHolidayScheduleByDate(LocalDate holidayDate) {
        log.info("Searching for holiday schedule with date: {}", holidayDate);
        return holidayScheduleRepository.findByHolidayDate(holidayDate)
                .orElseThrow(() -> {
                    log.error("Holiday schedule not found for date: {}", holidayDate);
                    return new IllegalArgumentException("Holiday schedule not found for date: " + holidayDate);
                });
    }
    /**
     * Retrieves all holiday schedules within a given date range.
     * <p>
     * Fetches holiday schedules that fall within the specified date range.
     * This is useful for generating reports, checking holidays for a given period,
     * or planning school events.
     * </p>
     *
     * @param startDate the start date of the range (inclusive).
     * @param endDate the end date of the range (inclusive).
     * @return a list of holiday schedules within the specified range.
     */
    public List<HolidaySchedule> getHolidayScheduleForDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching holiday schedules between {} and {}", startDate, endDate);
        return holidayScheduleRepository.findByHolidayDateBetween(startDate, endDate);
    }


    /**
     * Retrieves a holiday schedule by its unique ID.
     *
     * @param id the ID of the holiday schedule.
     * @return the found holiday schedule.
     * @throws IllegalArgumentException if the holiday schedule does not exist.
     */
    public HolidaySchedule getHolidayScheduleById(Long id) {
        log.info("Searching for holiday schedule with ID: {}", id);
        return holidayScheduleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Holiday schedule with ID {} not found", id);
                    return new IllegalArgumentException("Holiday schedule not found with ID: " + id);
                });
    }

    /**
     * Creates a new holiday schedule.
     * <p>
     * Validates the holiday schedule before saving it in the database.
     * Ensures that the provided holiday does not have an existing entry on the same date.
     * </p>
     *
     * @param holidaySchedule the holiday schedule to be created.
     * @return the created holiday schedule.
     * @throws IllegalArgumentException if validation fails or the holiday date already exists.
     */
    @Transactional
    public HolidaySchedule createHolidaySchedule(HolidaySchedule holidaySchedule) {
        validationService.validateHolidaySchedule(holidaySchedule);
        log.info("Creating new holiday schedule: {}", holidaySchedule);
        return holidayScheduleRepository.save(holidaySchedule);
    }

    /**
     * Updates an existing holiday schedule.
     * <p>
     * Validates the holiday schedule before updating it in the database.
     * The holiday schedule must have a valid ID and must already exist.
     * </p>
     *
     * @param holidaySchedule the holiday schedule with updated data.
     * @return the updated holiday schedule.
     * @throws IllegalArgumentException if validation fails or the holiday schedule does not exist.
     */
    @Transactional
    public HolidaySchedule updateHolidaySchedule(Long id, HolidaySchedule holidaySchedule) {
        log.info("Updating holiday schedule with ID: {}", id);

        HolidaySchedule existingSchedule = holidayScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Holiday schedule with ID " + id + " not found"));

        existingSchedule.setHolidayDate(holidaySchedule.getHolidayDate());
        existingSchedule.setDescription(holidaySchedule.getDescription());

        return holidayScheduleRepository.save(existingSchedule);
    }

    /**
     * Saves or updates a holiday schedule.
     * <p>
     * If an ID is present, the existing record is updated. Otherwise, a new holiday schedule is created.
     * </p>
     *
     * @param holidaySchedule the holiday schedule to save or update.
     * @return the saved holiday schedule.
     * @throws IllegalArgumentException if the holiday schedule is invalid.
     */
    @Transactional
    public HolidaySchedule saveHolidaySchedule(HolidaySchedule holidaySchedule) {
        validationService.validateHolidaySchedule(holidaySchedule);
        log.info("Saving holiday schedule: {}", holidaySchedule);
        return holidayScheduleRepository.save(holidaySchedule);
    }

    /**
     * Deletes a holiday schedule by its unique ID.
     * <p>
     * Removes the holiday schedule associated with the given ID from the database.
     * </p>
     *
     * @param id the ID of the holiday schedule to delete.
     * @throws IllegalArgumentException if the holiday schedule does not exist.
     */
    @Transactional
    public void deleteHolidayScheduleById(Long id) {
        log.info("Deleting holiday schedule with ID: {}", id);
        if (!holidayScheduleRepository.existsById(id)) {
            log.error("Holiday schedule with ID {} does not exist", id);
            throw new IllegalArgumentException("Holiday schedule not found with ID: " + id);
        }
        holidayScheduleRepository.deleteById(id);
        log.info("Holiday schedule with ID {} successfully deleted", id);
    }

    /**
     * Checks if a holiday schedule exists for the given date.
     * <p>
     * Queries the database to determine whether a holiday is already scheduled
     * on the specified date. Helps prevent duplicate holiday entries.
     * </p>
     *
     * @param holidayDate the date to check for an existing holiday schedule.
     * @return {@code true} if a holiday exists on the given date, {@code false} otherwise.
     * @throws IllegalArgumentException if the holiday date is null.
     */
    public boolean holidayExists(LocalDate holidayDate) {
        if (holidayDate == null) {
            throw new IllegalArgumentException("Holiday date cannot be null");
        }
        return holidayScheduleRepository.existsByHolidayDate(holidayDate);
    }

    /**
     * Checks if there is an active holiday for a given date.
     * <p>
     * Determines whether a holiday exists for the specified date.
     * </p>
     *
     * @param date the date to check for an active holiday.
     * @return {@code true} if an active holiday exists, {@code false} otherwise.
     */
    public boolean hasActiveHoliday(LocalDate date) {
        log.info("Checking for active holiday on {}", date);
        return holidayScheduleRepository.existsByHolidayDate(date);
    }
}
