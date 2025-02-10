package lt.ca.javau11.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lt.ca.javau11.repository.SpecialScheduleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a school schedule that manages the timing and organization of school lessons and breaks.
 * <p>
 * This class serves as the main entity for handling school bell schedules, containing information about
 * lesson durations, break times, and special schedule variations. It supports both regular daily schedules
 * and special occasion schedules.
 * <p>
 * The schedule can be either regular or special, with configurable lesson and break durations.
 * Each schedule is associated with a specific day of the week and can have multiple lessons.
 *
 * @author YourName
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "schedules")
public class Schedule {
    private static final Logger log = LoggerFactory.getLogger(Schedule.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The day of the week this schedule applies to.
     */
    @NotNull(message = "Day of week cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    /**
     * The type of the schedule (e.g., REGULAR, SPECIAL).
     */
    @NotNull(message = "Schedule type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType scheduleType = ScheduleType.REGULAR;

    /**
     * List of lessons associated with this schedule.
     */
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    /**
     * Duration of a lesson in minutes.
     */
    @Min(value = 15, message = "The lesson duration must be at least 15 minutes")
    @Max(value = 90, message = "The lesson duration must not exceed 90 minutes")
    @Column(nullable = false)
    private int lessonDuration = 45;

    /**
     * Duration of breaks in minutes.
     */
    @Min(value = 5, message = "The break duration must be at least 5 minutes")
    @Max(value = 30, message = "The break duration must not exceed 30 minutes")
    @Column(nullable = false)
    private int breakDuration = 10;

    /**
     * Start time of the first lesson.
     */
    @NotNull(message = "First lesson start time cannot be null")
    @Column(nullable = false)
    private LocalTime firstLessonStart = LocalTime.of(8, 0);

    /**
     * Indicates whether the schedule is active.
     */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * The effective date from which this schedule is applicable.
     */
    @Column
    private LocalDate effectiveDate;

    /**
     * Indicates whether daylight saving time is considered.
     */
    @Column
    private Boolean considerDaylightSaving = true;

    /**
     * Version number for optimistic locking.
     */
    @Version
    private Long version;

    /**
     * Timestamp of the last update.
     */
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    /**
     * Username of the last updater.
     */
    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * List of special schedules associated with this schedule.
     */
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpecialSchedule> specialSchedules = new ArrayList<>();

    /**
     * Default constructor.
     */
    public Schedule() {
        log.debug("Creating new Schedule instance");
    }

    // Getters

    /**
     * Gets the unique identifier of the schedule.
     *
     * @return the schedule's ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the day of week this schedule applies to.
     *
     * @return the day of week.
     */
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Gets the type of the schedule.
     *
     * @return the schedule type.
     */
    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    /**
     * Gets the list of lessons associated with this schedule.
     *
     * @return list of lessons.
     */
    public List<Lesson> getLessons() {
        return lessons;
    }

    /**
     * Gets the duration of lessons.
     *
     * @return the lesson duration in minutes.
     */
    public int getLessonDuration() {
        return lessonDuration;
    }

    /**
     * Gets the duration of breaks.
     *
     * @return the break duration in minutes.
     */
    public int getBreakDuration() {
        return breakDuration;
    }

    /**
     * Gets the start time of the first lesson.
     *
     * @return the first lesson start time.
     */
    public LocalTime getFirstLessonStart() {
        return firstLessonStart;
    }

    /**
     * Checks if the schedule is active.
     *
     * @return true if active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Gets the effective date of the schedule.
     *
     * @return the effective date.
     */
    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Gets the daylight saving consideration setting.
     *
     * @return true if DST is considered, false otherwise.
     */
    public Boolean getConsiderDaylightSaving() {
        return considerDaylightSaving;
    }

    /**
     * Gets the version number for optimistic locking.
     *
     * @return the version number.
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Gets the timestamp of the last update.
     *
     * @return the last updated timestamp.
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Gets the username of the last updater.
     *
     * @return the username.
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Gets the list of special schedules associated with this schedule.
     *
     * @return list of special schedules.
     */
    public List<SpecialSchedule> getSpecialSchedules() {
        return specialSchedules;
    }

    // Setters with validation and logging

    /**
     * Sets the schedule's ID.
     *
     * @param id the ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the day of week for the schedule.
     *
     * @param dayOfWeek the day of week to set.
     * @throws IllegalArgumentException if dayOfWeek is null.
     */
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) {
            log.error("Attempt to set null day of week");
            throw new IllegalArgumentException("Day of week cannot be null");
        }
        log.debug("Setting day of week to {}", dayOfWeek);
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Sets the schedule type.
     *
     * @param scheduleType the schedule type to set.
     * @throws IllegalArgumentException if scheduleType is null.
     */
    public void setScheduleType(ScheduleType scheduleType) {
        if (scheduleType == null) {
            log.error("Attempt to set null schedule type");
            throw new IllegalArgumentException("Schedule type cannot be null");
        }
        log.debug("Setting schedule type to {}", scheduleType);
        this.scheduleType = scheduleType;
    }

    /**
     * Sets the list of lessons for the schedule.
     *
     * @param lessons the list of lessons to set.
     */
    public void setLessons(List<Lesson> lessons) {
        this.lessons = (lessons != null) ? lessons : new ArrayList<>();
        log.debug("Set lessons list, size: {}", this.lessons.size());
    }

    /**
     * Sets the lesson duration in minutes.
     *
     * @param lessonDuration the lesson duration to set.
     * @throws IllegalArgumentException if duration is not between 15 and 90 minutes.
     */
    public void setLessonDuration(int lessonDuration) {
        if (lessonDuration < 15 || lessonDuration > 90) {
            log.error("Attempt to set invalid lesson duration: {}", lessonDuration);
            throw new IllegalArgumentException("Lesson duration must be between 15 and 90 minutes");
        }
        log.debug("Setting lesson duration to {} minutes", lessonDuration);
        this.lessonDuration = lessonDuration;
    }

    /**
     * Sets the break duration in minutes.
     *
     * @param breakDuration the break duration to set.
     * @throws IllegalArgumentException if duration is not between 5 and 30 minutes.
     */
    public void setBreakDuration(int breakDuration) {
        if (breakDuration < 5 || breakDuration > 30) {
            log.error("Attempt to set invalid break duration: {}", breakDuration);
            throw new IllegalArgumentException("Break duration must be between 5 and 30 minutes");
        }
        log.debug("Setting break duration to {} minutes", breakDuration);
        this.breakDuration = breakDuration;
    }

    /**
     * Sets the start time of the first lesson.
     *
     * @param firstLessonStart the start time to set.
     * @throws IllegalArgumentException if firstLessonStart is null.
     */
    public void setFirstLessonStart(LocalTime firstLessonStart) {
        if (firstLessonStart == null) {
            log.error("Attempt to set null first lesson start time");
            throw new IllegalArgumentException("First lesson start time cannot be null");
        }
        log.debug("Setting first lesson start time to {}", firstLessonStart);
        this.firstLessonStart = firstLessonStart;
    }

    /**
     * Sets whether the schedule is active.
     *
     * @param active true to mark the schedule as active, false otherwise.
     */
    public void setActive(boolean active) {
        log.debug("Setting active status to {}", active);
        this.active = active;
    }

    /**
     * Sets the effective date for the schedule.
     *
     * @param effectiveDate the effective date to set.
     */
    public void setEffectiveDate(LocalDate effectiveDate) {
        log.debug("Setting effective date to {}", effectiveDate);
        this.effectiveDate = effectiveDate;
    }

    /**
     * Sets the daylight saving time consideration flag.
     *
     * @param considerDaylightSaving true if DST is considered, false otherwise.
     */
    public void setConsiderDaylightSaving(Boolean considerDaylightSaving) {
        log.debug("Setting daylight saving consideration to {}", considerDaylightSaving);
        this.considerDaylightSaving = considerDaylightSaving;
    }

    /**
     * Sets the version number for optimistic locking.
     *
     * @param version the version to set.
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Sets the timestamp of the last update.
     *
     * @param lastUpdated the timestamp to set.
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        log.debug("Setting last updated timestamp to {}", lastUpdated);
        this.lastUpdated = lastUpdated;
    }

    /**
     * Sets the username of the last updater.
     *
     * @param updatedBy the username to set.
     */
    public void setUpdatedBy(String updatedBy) {
        log.debug("Setting updatedBy to {}", updatedBy);
        this.updatedBy = updatedBy;
    }

    /**
     * Sets the list of special schedules.
     *
     * @param specialSchedules the list of special schedules to set.
     */
    public void setSpecialSchedules(List<SpecialSchedule> specialSchedules) {
        this.specialSchedules = (specialSchedules != null) ? specialSchedules : new ArrayList<>();
        log.debug("Set {} special schedules", this.specialSchedules.size());
    }

    /**
     * Retrieves the appropriate schedule for a specific date.
     * Checks for a special schedule override for the given date.
     *
     * @param date the date for which to get the schedule.
     * @param specialScheduleRepository the repository to search for special schedules.
     * @return the special schedule's base schedule if found, otherwise this schedule.
     */
    public Schedule getScheduleForDate(LocalDate date, SpecialScheduleRepository specialScheduleRepository) {
        Optional<SpecialSchedule> specialScheduleOpt = specialScheduleRepository.findBySpecialDate(date);
        if (specialScheduleOpt.isPresent()) {
            log.debug("Found special schedule for date: {}", date);
            return specialScheduleOpt.get().getSchedule();
        }
        log.debug("No special schedule found for date: {}, using regular schedule", date);
        return this;
    }

    /**
     * Updates the last updated timestamp before saving or updating the schedule.
     */
    @PrePersist
    @PreUpdate
    protected void onSave() {
        lastUpdated = LocalDateTime.now();
        log.debug("Updating lastUpdated timestamp: {}", lastUpdated);
    }

    /**
     * Returns a string representation of the schedule.
     *
     * @return a string representing the schedule.
     */
    @Override
    public String toString() {
        return String.format(
            "Schedule{id=%d, dayOfWeek=%s, scheduleType=%s, lessonDuration=%d, breakDuration=%d, " +
            "firstLessonStart=%s, active=%b, effectiveDate=%s, lastUpdated=%s}",
            id, dayOfWeek, scheduleType, lessonDuration, breakDuration,
            firstLessonStart, active, effectiveDate, lastUpdated
        );
    }

    /**
     * Compares this schedule with another for equality.
     *
     * @param o the object to compare with.
     * @return true if the schedules are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return id != null && id.equals(schedule.id);
    }

    /**
     * Generates a hash code for the schedule.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
