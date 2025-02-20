package lt.ca.javau11.entity;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lt.ca.javau11.repository.SpecialScheduleRepository;

/**
 * Represents a school schedule that manages lesson timings, breaks, and special schedules.
 * Each schedule is linked to a specific day of the week and contains multiple lessons and breaks.
 * Supports both regular and special schedules with configurable durations.
 */
@Entity
@Table(name = "schedules", indexes = {
        @Index(name = "idx_schedule_effective_date", columnList = "effective_date"),
        @Index(name = "idx_schedule_day_of_week", columnList = "day_of_week"),
        @Index(name = "idx_schedule_active_day", columnList = "day_of_week, active")
})

public class Schedule {
    private static final Logger log = LoggerFactory.getLogger(Schedule.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** The name of the schedule, set by the user for custom schedules. */
    @Size(max = 100, message = "Custom name cannot exceed 100 characters")
    @Column(name = "custom_name", length = 100)
    private String customName;

    /** The day of the week this schedule applies to. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    /** The type of schedule (e.g., REGULAR, SPECIAL). */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType scheduleType = ScheduleType.REGULAR;

    /** List of lessons associated with this schedule. */
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    /** List of breaks associated with this schedule. */
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Break> breaks = new ArrayList<>();

    /** Duration of a lesson in minutes. */
    @NotNull
    @Column(nullable = false)
    private Duration lessonDuration = Duration.ofMinutes(45);

    /** Duration of breaks in minutes. */
    @Min(5) @Max(30)
    @Column(nullable = false)
    private Duration breakDuration = Duration.ofMinutes(10);

    /** Start time of the first lesson. */
    @NotNull
    @Column(nullable = false)
    private LocalTime firstLessonStart = LocalTime.of(8, 0);

    /** Indicates whether the schedule is active. */
    @Column(nullable = false)
    private boolean active = true;

    /** The effective date from which this schedule is applicable. */
    @Column
    private LocalDate effectiveDate;

    /** Indicates whether daylight saving time is considered. */
    @Column
    private Boolean considerDaylightSaving = true;

    /** Version number for optimistic locking. */
    @Version
    private Long version;

    /** Timestamp of the last update. */
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    /** Username of the last updater. */
    @Column(name = "updated_by")
    private String updatedBy;

    /** List of special schedules associated with this schedule. */
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpecialSchedule> specialSchedules = new ArrayList<>();

    /** Default constructor. */
    public Schedule() {
        log.debug("Creating new Schedule instance");
    }
    
    


	// ✅ Getters

    public Long getId() {
        return id;
    }
    
    public String getCustomName() {
        return customName;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<Break> getBreaks() {
        return breaks;
    }

    public Duration getLessonDuration() {
        return lessonDuration;
    }

    public Duration getBreakDuration() {
        return breakDuration;
    }

    public LocalTime getFirstLessonStart() {
        return firstLessonStart;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public Boolean getConsiderDaylightSaving() {
        return considerDaylightSaving;
    }

    public Long getVersion() {
        return version;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public List<SpecialSchedule> getSpecialSchedules() {
        return specialSchedules;
    }

    // ✅ Setters

    public void setId(Long id) {
        this.id = id;
    }
    public void setCustomName(String customName) {
        if (customName != null && customName.length() > 100) {
            log.error("Attempt to set too long custom name: {}", customName);
            throw new IllegalArgumentException("Custom name cannot exceed 100 characters");
        }
        log.debug("Setting custom schedule name: {}", customName);
        this.customName = customName;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons != null ? lessons : new ArrayList<>();
    }

    public void setBreaks(List<Break> breaks) {
        this.breaks = breaks != null ? breaks : new ArrayList<>();
    }

    public void setLessonDuration(Duration lessonDuration) {
        if (lessonDuration == null || lessonDuration.isNegative() || lessonDuration.isZero()) {
            log.error("Invalid lesson duration: {}", lessonDuration);
            throw new IllegalArgumentException("Lesson duration must be greater than 0 minutes");
        }
        log.debug("Setting lesson duration to {}", lessonDuration);
        this.lessonDuration = lessonDuration;
    }

    /**
     * Sets the duration of breaks in the schedule.
     *
     * @param breakDuration The duration of breaks.
     * @throws IllegalArgumentException if the duration is null or negative.
     */
    public void setBreakDuration(Duration breakDuration) {
        if (breakDuration == null || breakDuration.isNegative() || breakDuration.isZero()) {
            log.error("Attempt to set invalid break duration: {}", breakDuration);
            throw new IllegalArgumentException("Break duration must be a positive value.");
        }
        log.debug("Setting break duration to {}", breakDuration);
        this.breakDuration = breakDuration;
    }


    public void setFirstLessonStart(LocalTime firstLessonStart) {
        this.firstLessonStart = firstLessonStart;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setConsiderDaylightSaving(Boolean considerDaylightSaving) {
        this.considerDaylightSaving = considerDaylightSaving;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setSpecialSchedules(List<SpecialSchedule> specialSchedules) {
        this.specialSchedules = specialSchedules != null ? specialSchedules : new ArrayList<>();
    }

    /**
     * Retrieves the appropriate schedule for a specific date.
     * Checks for a special schedule override for the given date.
     *
     * @param date The date for which to get the schedule.
     * @param specialScheduleRepository The repository to search for special schedules.
     * @return The special schedule's base schedule if found, otherwise this schedule.
     */
    public Schedule getScheduleForDate(LocalDate date, SpecialScheduleRepository specialScheduleRepository) {
        Optional<SpecialSchedule> specialScheduleOpt = specialScheduleRepository.findBySpecialDate(date);
        
        if (specialScheduleOpt.isPresent()) {
            return specialScheduleOpt.get().getSchedule();
        } else {
            return this;
        }
    }



    /**
     * Automatically updates the last updated timestamp before saving or updating the schedule.
     */
    @PrePersist
    @PreUpdate
    protected void onSave() {
        this.lastUpdated = LocalDateTime.now();
        log.debug("Updating lastUpdated timestamp: {}", lastUpdated);
    }

    @Override
    public String toString() {
        return String.format(
                "Schedule{id=%d, dayOfWeek=%s, scheduleType=%s, lessonDuration=%d, breakDuration=%d, firstLessonStart=%s, active=%b, effectiveDate=%s, lastUpdated=%s, updatedBy=%s}",
                id, dayOfWeek, scheduleType, lessonDuration, breakDuration, firstLessonStart, active, effectiveDate, lastUpdated, updatedBy
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
