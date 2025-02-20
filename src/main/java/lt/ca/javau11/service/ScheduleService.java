package lt.ca.javau11.service;

import lt.ca.javau11.entity.*;
import lt.ca.javau11.exception.ResourceNotFoundException;
import lt.ca.javau11.repository.*;
import lt.ca.javau11.validation.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing schedules, including standard and custom schedules, lessons, breaks, calls, holidays, and special schedules.
 */
@Service
@Transactional
public class ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    private final ScheduleRepository scheduleRepository;
    private final LessonRepository lessonRepository;
    private final BreakRepository breakRepository;
    private final CallScheduleRepository callScheduleRepository;
    private final HolidayScheduleRepository holidayScheduleRepository;
    private final SpecialScheduleRepository specialScheduleRepository;
    private final ValidationService validationService;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           LessonRepository lessonRepository,
                           BreakRepository breakRepository,
                           CallScheduleRepository callScheduleRepository,
                           HolidayScheduleRepository holidayScheduleRepository,
                           SpecialScheduleRepository specialScheduleRepository,
                           ValidationService validationService) {
        this.scheduleRepository = scheduleRepository;
        this.lessonRepository = lessonRepository;
        this.breakRepository = breakRepository;
        this.callScheduleRepository = callScheduleRepository;
        this.holidayScheduleRepository = holidayScheduleRepository;
        this.specialScheduleRepository = specialScheduleRepository;
        this.validationService = validationService;
    }

    /**
     * Retrieves all schedules from the database.
     *
     * @return A list of all schedules.
     */
    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }

    /**
     * Retrieves a schedule by its unique ID.
     *
     * @param id The ID of the schedule.
     * @return The found schedule.
     * @throws ResourceNotFoundException if the schedule does not exist.
     */
    public Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with ID: " + id));
    }

    /**
     * Retrieves a schedule for a specific date, including special schedules.
     *
     * @param date The target date.
     * @return The schedule for the given date.
     * @throws ResourceNotFoundException if no schedule is found.
     */
    public Schedule getScheduleForDate(LocalDate date) {
        return scheduleRepository.findByEffectiveDate(date)
                .orElseThrow(() -> new ResourceNotFoundException("No schedule found for date: " + date));
    }

    /**
     * Retrieves schedules that fall within a given date range.
     *
     * @param start The start date (inclusive).
     * @param end The end date (inclusive).
     * @return A list of schedules within the specified range.
     */
    public List<Schedule> getSchedulesByDateRange(LocalDate start, LocalDate end) {
        return scheduleRepository.findByEffectiveDateBetween(start, end);
    }

    /**
     * Checks if an active schedule exists for a given day of the week.
     *
     * @param dayOfWeek The day to check.
     * @return True if an active schedule exists, false otherwise.
     */
    public boolean hasActiveSchedule(DayOfWeek dayOfWeek) {
        return scheduleRepository.findByDayOfWeekAndActiveTrue(dayOfWeek).isPresent();
    }

    /**
     * Saves a schedule after validation.
     *
     * @param schedule The schedule to save.
     * @return The saved schedule.
     */
    public Schedule saveSchedule(Schedule schedule) {
        validationService.validateSchedule(schedule);
        return scheduleRepository.save(schedule);
    }

    /**
     * Creates a new schedule.
     *
     * @param schedule The new schedule details.
     * @return The created schedule.
     */
    public Schedule createSchedule(Schedule schedule) {
        return saveSchedule(schedule);
    }

    /**
     * Updates an existing schedule.
     *
     * @param id The ID of the schedule to update.
     * @param scheduleDetails The new schedule details.
     * @return The updated schedule.
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
        return saveSchedule(existingSchedule);
    }

    /**
     * Deletes a schedule along with all its associated entities.
     *
     * @param id The ID of the schedule to delete.
     * @throws ResourceNotFoundException if the schedule does not exist.
     */
    public void deleteSchedule(Long id) {
        Schedule schedule = findScheduleById(id);
        lessonRepository.deleteAll(schedule.getLessons());
        breakRepository.deleteAll(schedule.getBreaks());
        callScheduleRepository.deleteAll(callScheduleRepository.findByScheduleId(id));
        scheduleRepository.deleteById(id);
        logger.info("Deleted schedule with ID: {}", id);
    }


    /** Retrieves all regular schedules. */
    public List<Schedule> getRegularSchedules() {
        return scheduleRepository.findByScheduleType(ScheduleType.REGULAR);
    }

    /** Retrieves all special schedules. */
    public List<Schedule> getSpecialSchedules() {
        return scheduleRepository.findByScheduleType(ScheduleType.SPECIAL);
    }
    /** Retrieves all lessons associated with a given schedule. */
    public List<Lesson> findLessonsBySchedule(Long scheduleId) {
        Schedule schedule = findScheduleById(scheduleId);
        return lessonRepository.findByScheduleIdOrderByOrderNumber(schedule.getId());
    }

    /** Adds a lesson to a specific schedule. */
    public void addLessonToSchedule(Long scheduleId, Lesson lesson) {
        lesson.setSchedule(findScheduleById(scheduleId));
        lessonRepository.save(lesson);
    }

    /** Removes a lesson from the schedule. */
    public void removeLesson(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }

    /**
     * Saves a corrected version of a standard schedule as a special schedule
     * This method allows an administrator to modify a base schedule and save it as a new special schedule.
     * It ensures that all related lessons, breaks, and call schedules are copied.
     *
     * @param baseScheduleId The ID of the base schedule to copy.
     * @param updatedSchedule The updated schedule details provided by the admin.
     * @param customName The custom name assigned to the new schedule.
     * @return The newly saved special schedule.
     */
    public Schedule saveCorrectedSchedule(Long baseScheduleId, Schedule updatedSchedule, String customName) {
        logger.info("Saving corrected schedule based on template {} with name '{}'", baseScheduleId, customName);

        Schedule baseSchedule = findScheduleById(baseScheduleId);
        Schedule newSchedule = new Schedule();

        // Copy essential schedule properties
        newSchedule.setDayOfWeek(baseSchedule.getDayOfWeek());
        newSchedule.setScheduleType(ScheduleType.SPECIAL);
        newSchedule.setLessonDuration(updatedSchedule.getLessonDuration());
        newSchedule.setBreakDuration(updatedSchedule.getBreakDuration());
        newSchedule.setFirstLessonStart(updatedSchedule.getFirstLessonStart());
        newSchedule.setActive(true);
        newSchedule.setEffectiveDate(updatedSchedule.getEffectiveDate());
        newSchedule.setUpdatedBy(updatedSchedule.getUpdatedBy());
        newSchedule.setCustomName(customName);

        //  Copy lessons from updated schedule
        updatedSchedule.getLessons().forEach(lesson -> {
            Lesson newLesson = new Lesson(lesson.getOrderNumber(), lesson.getSubjectEn(), newSchedule);
            newSchedule.getLessons().add(newLesson);
        });

        //  Copy breaks from updated schedule
        updatedSchedule.getBreaks().forEach(breakTime -> {
            Break newBreak = new Break(breakTime.getName(), breakTime.getStartTime(), breakTime.getDuration(), newSchedule);
            newSchedule.getBreaks().add(newBreak);
        });

        //  Copy call schedules from the base schedule
        callScheduleRepository.findByScheduleId(baseScheduleId).forEach(callSchedule -> {
            CallSchedule newCallSchedule = new CallSchedule();
            newCallSchedule.setSchedule(newSchedule);
            newCallSchedule.setCallTime(callSchedule.getCallTime());
            newCallSchedule.setCallType(callSchedule.getCallType());
            callScheduleRepository.save(newCallSchedule);
        });

        return saveSchedule(newSchedule);
    }

   

    /** Methods for managing breaks in a schedule. */
    public List<Break> findBreaksBySchedule(Long scheduleId) {
        return breakRepository.findByScheduleIdWithSchedule(scheduleId);
    }

    public void addBreakToSchedule(Long scheduleId, Break breakTime) {
        breakTime.setSchedule(findScheduleById(scheduleId));
        breakRepository.save(breakTime);
    }

    public void removeBreak(Long breakId) {
        breakRepository.deleteById(breakId);
    }

    /** Methods for managing call schedules in a schedule. */
    public List<CallSchedule> findCallSchedulesBySchedule(Long scheduleId) {
        return callScheduleRepository.findByScheduleId(scheduleId);
    }

    public void addCallScheduleToSchedule(Long scheduleId, CallSchedule callSchedule) {
        callSchedule.setSchedule(findScheduleById(scheduleId));
        callScheduleRepository.save(callSchedule);
    }

    public void removeCallSchedule(Long callScheduleId) {
        callScheduleRepository.deleteById(callScheduleId);
    }

    /** Methods for managing holiday schedules. */
    public Optional<HolidaySchedule> findHolidaySchedulesByDate(LocalDate date) {
        return holidayScheduleRepository.findByHolidayDate(date);
    }

    public void addHolidaySchedule(HolidaySchedule holidaySchedule) {
        holidayScheduleRepository.save(holidaySchedule);
    }

    public void removeHolidaySchedule(Long holidayId) {
        holidayScheduleRepository.deleteById(holidayId);
    }

    /** Methods for managing special schedules. */
    public Optional<SpecialSchedule> findSpecialSchedulesByDate(LocalDate date) {
        return specialScheduleRepository.findBySpecialDate(date);
    }

    public void addSpecialSchedule(SpecialSchedule specialSchedule) {
        specialScheduleRepository.save(specialSchedule);
    }

    public void removeSpecialSchedule(Long specialScheduleId) {
        specialScheduleRepository.deleteById(specialScheduleId);
    }
}
