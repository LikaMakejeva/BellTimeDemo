package lt.ca.javau11.service;

import lt.ca.javau11.dto.ScheduleEventDTO;
import lt.ca.javau11.entity.Break;
import lt.ca.javau11.entity.Lesson;
import lt.ca.javau11.entity.Schedule;
import lt.ca.javau11.exception.ResourceNotFoundException;
import lt.ca.javau11.repository.BreakRepository;
import lt.ca.javau11.repository.LessonRepository;
import lt.ca.javau11.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing schedule events (lessons and breaks).
 * 
 * This service provides methods to create, update, delete, and retrieve
 * schedule events for a calendar, converting between entity models and DTOs.
 * 
 *
 *
 * @author Lika Makejeva
 * @version 1.2
 * @since 1.0
 */
@Service
@Transactional
public class ScheduleEventService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleEventService.class);

    private final ScheduleRepository scheduleRepository;
    private final LessonRepository lessonRepository;
    private final BreakRepository breakRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param scheduleRepository Repository for schedules.
     * @param lessonRepository   Repository for lessons.
     * @param breakRepository    Repository for breaks.
     */
    public ScheduleEventService(ScheduleRepository scheduleRepository, LessonRepository lessonRepository, BreakRepository breakRepository) {
        this.scheduleRepository = scheduleRepository;
        this.lessonRepository = lessonRepository;
        this.breakRepository = breakRepository;
    }

    /**
     * Retrieves all schedule events within a given date range.
     *
     * @param startDate Start date (inclusive).
     * @param endDate   End date (inclusive).
     * @return List of `ScheduleEventDTO` representing lessons and breaks.
     */
    public List<ScheduleEventDTO> getEventsForDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching schedule events between {} and {}", startDate, endDate);

        return scheduleRepository.findByEffectiveDateBetween(startDate, endDate)
                .stream()
                .flatMap(schedule -> convertScheduleToEvents(schedule).stream())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all schedule events for a specific date.
     *
     * @param date The date for which events should be retrieved.
     * @return List of `ScheduleEventDTO` for the given date.
     */
    public List<ScheduleEventDTO> getEventsForDate(LocalDate date) {
        logger.info("Fetching schedule events for date: {}", date);
        return getEventsForDateRange(date, date);
    }
    /**
     * Creates a new schedule event (lesson or break).
     *
     * @param eventDTO The event data to be created.
     * @return The created event as a DTO.
     * @throws ResourceNotFoundException if no schedule exists for the event's date.
     */
    public ScheduleEventDTO createEvent(ScheduleEventDTO eventDTO) {
        logger.info("Creating new schedule event: {}", eventDTO);

        Schedule schedule = scheduleRepository.findByEffectiveDate(eventDTO.getStart().toLocalDate())
            .orElseThrow(() -> new ResourceNotFoundException("Schedule not found for date: " + eventDTO.getStart().toLocalDate()));

        if (eventDTO.getTitle().equalsIgnoreCase("Break")) {
            Break newBreak = new Break(
                "Default Break",
                eventDTO.getStart().toLocalTime(),
                Duration.between(eventDTO.getStart(), eventDTO.getEnd()),
                schedule 
            );
            breakRepository.save(newBreak);
        } else {
            Lesson newLesson = new Lesson(schedule.getLessons().size() + 1, eventDTO.getTitle(), schedule);
            lessonRepository.save(newLesson);
        }

        return eventDTO;
    }
    /**
     * Updates an existing schedule event.
     *
     * @param id       The ID of the event to update.
     * @param eventDTO The new event details.
     * @return The updated event DTO.
     * @throws ResourceNotFoundException if the event with the given ID is not found.
     */
    public ScheduleEventDTO updateEvent(Long id, ScheduleEventDTO eventDTO) {
        logger.info("Updating schedule event with ID: {}", id);

        Lesson lesson = lessonRepository.findById(id).orElse(null);
        Break breakTime = breakRepository.findById(id).orElse(null);

        if (lesson != null) {
            lesson.setSubjectEn(eventDTO.getTitle());
            lessonRepository.save(lesson);
        } else if (breakTime != null) {
            breakTime.setStartTime(eventDTO.getStart().toLocalTime());
            
            // 🔥 **Вместо setEndTime() обновляем длительность!**
            Duration newDuration = Duration.between(eventDTO.getStart(), eventDTO.getEnd());
            breakTime.setDuration(newDuration); // ✅ Обновляем длительность
            breakRepository.save(breakTime);
        } else {
            throw new ResourceNotFoundException("Schedule event not found with ID: " + id);
        }

        return eventDTO;
    }

    /**
     * Deletes a schedule event (lesson or break) by ID.
     *
     * @param id The ID of the event to delete.
     * @throws ResourceNotFoundException if the event with the given ID is not found.
     */
    public void deleteEvent(Long id) {
        logger.info("Deleting schedule event with ID: {}", id);

        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
            logger.info("Deleted lesson with ID: {}", id);
        } else if (breakRepository.existsById(id)) {
            breakRepository.deleteById(id);
            logger.info("Deleted break with ID: {}", id);
        } else {
            throw new ResourceNotFoundException("Schedule event not found with ID: " + id);
        }
    }

    /**
     * Saves a schedule event (creates if new, updates if existing).
     *
     * @param eventDTO The event data to be saved.
     * @return The saved event DTO.
     */
    public ScheduleEventDTO saveEvent(ScheduleEventDTO eventDTO) {
        logger.info("Saving schedule event: {}", eventDTO);

        if (eventDTO.getId() == null) {
            return createEvent(eventDTO);
        } else {
            return updateEvent(eventDTO.getId(), eventDTO);
        }
    }

    /**
     * Checks if there are active events on a specific day of the week.
     *
     * @param dayOfWeek The day of the week to check.
     * @return True if active events exist, false otherwise.
     */
    public boolean hasActiveEvent(DayOfWeek dayOfWeek) {
        logger.info("Checking for active events on {}", dayOfWeek);

        return scheduleRepository.findByDayOfWeekAndActiveTrue(dayOfWeek).isPresent();
    }

    /**
     * Converts a `Schedule` entity into a list of `ScheduleEventDTO` objects.
     *
     * @param schedule The schedule entity to convert.
     * @return A list of `ScheduleEventDTO` objects representing the lessons and breaks in the schedule.
     */
    private List<ScheduleEventDTO> convertScheduleToEvents(Schedule schedule) {
        LocalDate effectiveDate = schedule.getEffectiveDate();

        List<ScheduleEventDTO> lessons = schedule.getLessons()
                .stream()
                .map(lesson -> new ScheduleEventDTO(
                        lesson.getId(),
                        lesson.getSubjectEn(),
                        LocalDateTime.of(effectiveDate, lesson.getStartTime()),
                      
                        LocalDateTime.of(effectiveDate, lesson.getStartTime().plusMinutes(lesson.getDuration().toMinutes())),
                        "#3788d8",
                        false
                ))
                .collect(Collectors.toList());

        List<ScheduleEventDTO> breaks = schedule.getBreaks()
                .stream()
                .map(breakTime -> new ScheduleEventDTO(
                        breakTime.getId(),
                        "Break",
                        LocalDateTime.of(effectiveDate, breakTime.getStartTime()),
                        LocalDateTime.of(effectiveDate, breakTime.getEndTime()),
                        "#28a745",
                        false
                ))
                .collect(Collectors.toList());

        lessons.addAll(breaks);
        return lessons;
    }
}
