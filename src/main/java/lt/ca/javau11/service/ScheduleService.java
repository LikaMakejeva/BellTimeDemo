package lt.ca.javau11.service;

import lt.ca.javau11.entity.Schedule;
import lt.ca.javau11.entity.DayOfWeek;
import lt.ca.javau11.repository.ScheduleRepository;
import lt.ca.javau11.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing school schedules.
 * Provides core business logic for working with schedules.
 */
@Service
@Transactional
public class ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);
    private final ScheduleRepository scheduleRepository;

    /**
     * Constructor for schedule service.
     *
     * @param scheduleRepository the repository for working with schedules.
     */
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * Retrieves a list of all schedules.
     *
     * @return a list of all schedules.
     */
    public List<Schedule> findAllSchedules() {
        logger.info("Fetching all schedules");
        List<Schedule> schedules = scheduleRepository.findAll();
        logger.info("Found {} schedules", schedules.size());
        return schedules;
    }

    /**
     * Finds a schedule by its ID.
     *
     * @param id the ID of the schedule.
     * @return the found schedule.
     * @throws ResourceNotFoundException if no schedule is found with the given ID.
     */
    public Schedule findScheduleById(Long id) {
        logger.info("Fetching schedule with ID: {}", id);
        return scheduleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Schedule with ID {} not found", id);
                    return new ResourceNotFoundException("Schedule not found with ID: " + id);
                });
    }

    /**
     * Retrieves a schedule by its effective date.
     *
     * @param effectiveDate the effective date.
     * @return the found schedule.
     * @throws ResourceNotFoundException if no schedule is found for the given date.
     */
    public Schedule getScheduleForDate(LocalDate effectiveDate) {
        logger.info("Fetching schedule for date: {}", effectiveDate);
        return scheduleRepository.findByEffectiveDate(effectiveDate)
                .orElseThrow(() -> {
                    logger.error("Schedule not found for date: {}", effectiveDate);
                    return new ResourceNotFoundException("Schedule not found for date: " + effectiveDate);
                });
    }

    /**
     * Retrieves all schedules within a specified date range.
     *
     * @param startDate the start date (inclusive).
     * @param endDate   the end date (inclusive).
     * @return a list of schedules in the specified range.
     */
    public List<Schedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching schedules from {} to {}", startDate, endDate);
        List<Schedule> schedules = scheduleRepository.findByEffectiveDateBetween(startDate, endDate);
        logger.info("Found {} schedules in range", schedules.size());
        return schedules;
    }

    /**
     * Checks if an active schedule exists for a specific day of the week.
     *
     * @param dayOfWeek the day of the week.
     * @return true if an active schedule exists, false otherwise.
     */
    public boolean hasActiveSchedule(DayOfWeek dayOfWeek) {
        logger.info("Checking for active schedule for day: {}", dayOfWeek);
        Optional<Schedule> activeSchedule = scheduleRepository.findByDayOfWeekAndActiveTrue(dayOfWeek);
        logger.debug("Active schedule for {}: {}", dayOfWeek, activeSchedule.isPresent() ? "found" : "not found");
        return activeSchedule.isPresent();
    }

    /**
     * Creates a new schedule.
     * The schedule must not have an existing ID.
     *
     * @param schedule the schedule to create.
     * @return the created schedule.
     * @throws IllegalArgumentException if the schedule is null or has an ID.
     */
    public Schedule saveSchedule(Schedule schedule) {
        logger.info("Saving new schedule");
        if (schedule == null) {
            logger.error("Received null schedule data");
            throw new IllegalArgumentException("Schedule data cannot be null");
        }
        if (schedule.getId() != null) {
            logger.error("Attempt to create schedule with existing ID: {}", schedule.getId());
            throw new IllegalArgumentException("New schedule should not have an ID");
        }
        validateSchedule(schedule);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        logger.info("Schedule successfully saved with ID: {}", savedSchedule.getId());
        return savedSchedule;
    }

    /**
     * Updates an existing schedule.
     *
     * @param id the ID of the schedule to update.
     * @param scheduleDetails the schedule data to update.
     * @return the updated schedule.
     * @throws ResourceNotFoundException if no schedule exists with the given ID.
     * @throws IllegalArgumentException if the provided schedule data is invalid.
     */
    public Schedule updateSchedule(Long id, Schedule scheduleDetails) {
        logger.info("Updating schedule with ID: {}", id);
        if (scheduleDetails == null) {
            logger.error("Received null data for schedule update");
            throw new IllegalArgumentException("Schedule data cannot be null");
        }
        Schedule existingSchedule = findScheduleById(id);
        // Update fields as needed – пример обновления основных полей
        existingSchedule.setDayOfWeek(scheduleDetails.getDayOfWeek());
        existingSchedule.setScheduleType(scheduleDetails.getScheduleType());
        existingSchedule.setLessonDuration(scheduleDetails.getLessonDuration());
        existingSchedule.setBreakDuration(scheduleDetails.getBreakDuration());
        existingSchedule.setFirstLessonStart(scheduleDetails.getFirstLessonStart());
        existingSchedule.setActive(scheduleDetails.isActive());
        existingSchedule.setEffectiveDate(scheduleDetails.getEffectiveDate());
        existingSchedule.setConsiderDaylightSaving(scheduleDetails.getConsiderDaylightSaving());
        // SpecialSchedules and Lessons can be updated separately if needed
        validateSchedule(existingSchedule);
        Schedule updatedSchedule = scheduleRepository.save(existingSchedule);
        logger.info("Schedule with ID {} successfully updated", id);
        return updatedSchedule;
    }

    /**
     * Deletes a schedule by its ID.
     *
     * @param id the ID of the schedule to delete.
     * @throws ResourceNotFoundException if no schedule exists with the given ID.
     */
    public void deleteSchedule(Long id) {
        logger.info("Deleting schedule with ID: {}", id);
        if (!scheduleRepository.existsById(id)) {
            logger.error("Schedule with ID {} not found for deletion", id);
            throw new ResourceNotFoundException("Schedule not found with id: " + id);
        }
        scheduleRepository.deleteById(id);
        logger.info("Schedule with ID {} successfully deleted", id);
    }

    /**
     * Validates the schedule data.
     *
     * @param schedule the schedule to validate.
     * @throws IllegalArgumentException if any required fields are missing or invalid.
     */
    private void validateSchedule(Schedule schedule) {
        if (schedule.getDayOfWeek() == null) {
            throw new IllegalArgumentException("Day of week must be specified");
        }
        if (schedule.getScheduleType() == null) {
            throw new IllegalArgumentException("Schedule type must be specified");
        }
        if (schedule.getLessonDuration() < 15 || schedule.getLessonDuration() > 90) {
            throw new IllegalArgumentException("Lesson duration must be between 15 and 90 minutes");
        }
        if (schedule.getBreakDuration() < 5 || schedule.getBreakDuration() > 30) {
            throw new IllegalArgumentException("Break duration must be between 5 and 30 minutes");
        }
        if (schedule.getFirstLessonStart() == null) {
            throw new IllegalArgumentException("First lesson start time must be specified");
        }
    }
}
