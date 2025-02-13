package lt.ca.javau11.service;

import lt.ca.javau11.dto.ScheduleEventDTO;
import lt.ca.javau11.entity.Schedule;
import lt.ca.javau11.repository.ScheduleRepository;
import lt.ca.javau11.exception.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing school schedules.
 * Provides core business logic for working with schedules.
 */
@Service
@Transactional
public class ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * Retrieves all schedules.
     *
     * @return list of all schedules.
     */
    public List<Schedule> findAllSchedules() {
        logger.info("Fetching all schedules");
        return scheduleRepository.findAll();
    }

    /**
     * Finds a schedule by its ID.
     *
     * @param id Schedule ID.
     * @return the found schedule.
     * @throws ResourceNotFoundException if not found.
     */
    public Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));
    }

    /**
     * Retrieves a schedule by its effective date.
     *
     * @param effectiveDate the effective date.
     * @return the found schedule.
     * @throws ResourceNotFoundException if not found.
     */
    public Schedule getScheduleForDate(LocalDate effectiveDate) {
        return scheduleRepository.findByEffectiveDate(effectiveDate)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found for date: " + effectiveDate));
    }

    /**
     * Retrieves all schedules within a specified date range.
     *
     * @param startDate start date (inclusive).
     * @param endDate   end date (inclusive).
     * @return list of schedules.
     */
    public List<Schedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByEffectiveDateBetween(startDate, endDate);
    }

    /**
     * Checks if an active schedule exists for a specific day of the week.
     *
     * @param dayOfWeek the day of the week.
     * @return true if an active schedule exists, false otherwise.
     */
    public boolean hasActiveSchedule(java.time.DayOfWeek dayOfWeek) {
        return scheduleRepository.findByDayOfWeekAndActiveTrue(dayOfWeek).isPresent();
    }

    /**
     * Creates a new schedule.
     *
     * @param schedule the schedule to create.
     * @return the created schedule.
     */
    public Schedule saveSchedule(Schedule schedule) {
        validateSchedule(schedule);
        return scheduleRepository.save(schedule);
    }

    /**
     * Updates an existing schedule.
     *
     * @param id Schedule ID.
     * @param scheduleDetails Schedule data to update.
     * @return the updated schedule.
     * @throws ResourceNotFoundException if schedule is not found.
     */
    public Schedule updateSchedule(Long id, Schedule scheduleDetails) {
        Schedule existingSchedule = findScheduleById(id);
        
        existingSchedule.setDayOfWeek(scheduleDetails.getDayOfWeek());
        existingSchedule.setScheduleType(scheduleDetails.getScheduleType());
        existingSchedule.setLessonDuration(scheduleDetails.getLessonDuration());
        existingSchedule.setBreakDuration(scheduleDetails.getBreakDuration());
        existingSchedule.setFirstLessonStart(scheduleDetails.getFirstLessonStart());
        existingSchedule.setActive(scheduleDetails.isActive());
        existingSchedule.setEffectiveDate(scheduleDetails.getEffectiveDate());

        validateSchedule(existingSchedule);
        return scheduleRepository.save(existingSchedule);
    }

    /**
     * Deletes a schedule by its ID.
     *
     * @param id Schedule ID.
     * @throws ResourceNotFoundException if schedule is not found.
     */
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Schedule not found with ID: " + id);
        }
        scheduleRepository.deleteById(id);
    }

    /**
     * Retrieves all schedules in a date range and converts them into ScheduleEventDTOs.
     *
     * @param startDate start date (inclusive).
     * @param endDate   end date (inclusive).
     * @return a list of ScheduleEventDTO representing lessons and breaks.
     */
    public List<ScheduleEventDTO> getEventsForDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching schedule events between {} and {}", startDate, endDate);

        return scheduleRepository.findByEffectiveDateBetween(startDate, endDate)
                .stream()
                .flatMap(schedule -> {
                    LocalDate effectiveDate = schedule.getEffectiveDate();
                    List<ScheduleEventDTO> events = new ArrayList<>();

                    // Process lessons
                    schedule.getLessons().forEach(lesson -> events.add(
                            new ScheduleEventDTO(
                                    lesson.getId(),
                                    lesson.getSubjectEn(),
                                    LocalDateTime.of(effectiveDate, lesson.getStartTime()),
                                    LocalDateTime.of(effectiveDate, lesson.getStartTime().plusMinutes(lesson.getDuration())),
                                    "#3788d8",
                                    false
                            )
                    ));

                    // Process breaks (FIX: plusMinutes now correctly uses long)
                    schedule.getBreaks().forEach(breakTime -> events.add(
                            new ScheduleEventDTO(
                                    breakTime.getId(),
                                    "Перемена",
                                    LocalDateTime.of(effectiveDate, breakTime.getStartTime()),
                                    LocalDateTime.of(effectiveDate, breakTime.getStartTime().plusMinutes(breakTime.getDuration().toMinutes())), // 🔥 FIXED!
                                    "#28a745",
                                    false
                            )
                    ));

                    return events.stream();
                })
                .collect(Collectors.toList());
    }

    /**
     * Validates the schedule data.
     *
     * @param schedule Schedule to validate.
     * @throws IllegalArgumentException if required fields are missing or invalid.
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
