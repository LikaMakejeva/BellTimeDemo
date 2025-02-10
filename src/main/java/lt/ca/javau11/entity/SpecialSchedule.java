package lt.ca.javau11.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a special schedule for specific dates in the school calendar.
 * Used for events, shortened days, or any other schedule variations.
 */
@Entity
@Table(name = "special_schedules", indexes = {
    @Index(name = "idx_special_date", columnList = "special_date", unique = true),
    @Index(name = "idx_special_schedule", columnList = "schedule_id")
})
public class SpecialSchedule {
    private static final Logger log = LoggerFactory.getLogger(SpecialSchedule.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Special date for which the schedule is applicable.
     */
    @NotNull(message = "Special date cannot be null")
    @Column(name = "special_date", nullable = false)
    private LocalDate specialDate;

    /**
     * Base schedule associated with this special schedule.
     */
    @NotNull(message = "Schedule cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    /**
     * List of lessons included in the special schedule.
     */
    @OneToMany(mappedBy = "specialSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    /**
     * Optional description for the special schedule.
     */
    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Column(name = "description", length = 255)
    private String description;

    /**
     * Default constructor.
     */
    public SpecialSchedule() {
    }

    /**
     * Constructor with required parameters.
     *
     * @param specialDate the special date for the schedule.
     * @param schedule    the base schedule for the special day.
     */
    public SpecialSchedule(LocalDate specialDate, Schedule schedule) {
        this.setSpecialDate(specialDate);
        this.setSchedule(schedule);
    }

    /**
     * Full constructor.
     *
     * @param specialDate the special date.
     * @param schedule    the base schedule.
     * @param lessons     list of lessons for the special schedule.
     * @param description description of the special schedule.
     */
    public SpecialSchedule(LocalDate specialDate, Schedule schedule, List<Lesson> lessons, String description) {
        this.setSpecialDate(specialDate);
        this.setSchedule(schedule);
        this.setLessons(lessons);
        this.setDescription(description);
    }

    // Getters

    /**
     * Gets the ID of the special schedule.
     *
     * @return the ID of the special schedule.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the special date.
     *
     * @return the special date.
     */
    public LocalDate getSpecialDate() {
        return specialDate;
    }

    /**
     * Gets the base schedule.
     *
     * @return the base schedule.
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Gets the list of lessons.
     *
     * @return the list of lessons.
     */
    public List<Lesson> getLessons() {
        return lessons;
    }

    /**
     * Gets the description.
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    // Setters with validation and logging

    /**
     * Sets the ID of the special schedule.
     *
     * @param id the ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the special date with validation.
     *
     * @param specialDate the special date to set.
     * @throws IllegalArgumentException if specialDate is null.
     */
    public void setSpecialDate(LocalDate specialDate) {
        if (specialDate == null) {
            log.error("Attempt to set null special date");
            throw new IllegalArgumentException("Special date cannot be null");
        }
        if (specialDate.isBefore(LocalDate.now())) {
            log.warn("Setting special date in the past: {}", specialDate);
        }
        log.debug("Setting special date: {}", specialDate);
        this.specialDate = specialDate;
    }

    /**
     * Sets the base schedule with validation.
     *
     * @param schedule the schedule to set.
     * @throws IllegalArgumentException if schedule is null.
     */
    public void setSchedule(Schedule schedule) {
        if (schedule == null) {
            log.error("Attempt to set null schedule");
            throw new IllegalArgumentException("Schedule cannot be null");
        }
        log.debug("Setting schedule with ID: {}", schedule.getId());
        this.schedule = schedule;
    }

    /**
     * Sets the lessons list.
     *
     * @param lessons the list of lessons.
     */
    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons != null ? lessons : new ArrayList<>();
        log.debug("Set {} lessons", this.lessons.size());
    }

    /**
     * Sets the description with validation.
     *
     * @param description the description to set.
     * @throws IllegalArgumentException if description length exceeds 255 characters.
     */
    public void setDescription(String description) {
        if (description != null && description.length() > 255) {
            log.error("Attempt to set too long description: {} characters", description.length());
            throw new IllegalArgumentException("Description must not exceed 255 characters");
        }
        this.description = description != null ? description.trim() : null;
        log.debug("Setting description: {}", this.description);
    }

    /**
     * Adds a lesson to the special schedule.
     *
     * @param lesson the lesson to add.
     */
    public void addLesson(Lesson lesson) {
        if (lesson != null) {
            lessons.add(lesson);
            log.debug("Added lesson. Total lessons: {}", lessons.size());
        }
    }

    /**
     * Removes a lesson from the special schedule.
     *
     * @param lesson the lesson to remove.
     */
    public void removeLesson(Lesson lesson) {
        if (lesson != null && lessons.remove(lesson)) {
            log.debug("Removed lesson. Remaining lessons: {}", lessons.size());
        }
    }

    /**
     * Validates the entity before persisting or updating.
     *
     * @throws IllegalArgumentException if required fields are missing.
     */
    @PrePersist
    @PreUpdate
    protected void validateBeforeSave() {
        if (specialDate == null) {
            throw new IllegalArgumentException("Special date must be set");
        }
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule must be set");
        }
        log.debug("Special schedule validation passed before save");
    }

    /**
     * Provides a string representation of the special schedule.
     *
     * @return string representation of the special schedule.
     */
    @Override
    public String toString() {
        return String.format("SpecialSchedule{id=%d, specialDate=%s, scheduleId=%s, lessons=%d, description='%s'}",
                id, specialDate, schedule != null ? schedule.getId() : "null", lessons.size(), description);
    }

    /**
     * Compares this special schedule with another for equality.
     *
     * @param o the object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecialSchedule)) return false;
        SpecialSchedule that = (SpecialSchedule) o;
        return id != null && id.equals(that.id);
    }

    /**
     * Generates a hash code for the special schedule.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
