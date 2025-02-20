package lt.ca.javau11.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalTime;

/**
 * Entity representing a school bell schedule.
 * Contains information about bell times and their types (lesson start, break, etc.).
 */
@Entity
@Table(name = "call_schedules", indexes = {
    @Index(name = "idx_call_lesson", columnList = "lesson_id"),
    @Index(name = "idx_call_break", columnList = "break_id"),
    @Index(name = "idx_call_time", columnList = "call_time")
})
public class CallSchedule {
    private static final Logger log = LoggerFactory.getLogger(CallSchedule.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Time when the bell rings.
     */
    @NotNull(message = "Call time cannot be null")
    @Column(name = "call_time", nullable = false)
    private LocalTime callTime;

    /**
     * Type of the bell (e.g. lesson start, break, etc.).
     */
    @NotNull(message = "Call type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CallType callType;

    /**
     * Associated lesson for the bell, if applicable.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    /**
     * Associated break period for the bell, if applicable.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "break_id")
    private Break breakPeriod;
    
    /**
     * Associated Schedule .
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false) 
    private Schedule schedule;

    /**
     * Default constructor.
     */
    public CallSchedule() {
    }

    /**
     * Constructor with main parameters.
     *
     * @param callTime the time of the bell.
     * @param callType the type of the bell.
     */
    public CallSchedule(LocalTime callTime, CallType callType) {
        this.setCallTime(callTime);
        this.setCallType(callType);
    }

    /**
     * Full constructor.
     *
     * @param callTime   the time of the bell.
     * @param callType   the type of the bell.
     * @param lesson     the associated lesson.
     * @param breakPeriod the associated break period.
     */
    public CallSchedule(LocalTime callTime, CallType callType, Lesson lesson, Break breakPeriod) {
        this.setCallTime(callTime);
        this.setCallType(callType);
        this.setLesson(lesson);
        this.setBreakPeriod(breakPeriod);
    }

    // Getters

    /**
     * Gets the ID of the call schedule.
     *
     * @return the ID of the call schedule.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the time of the bell.
     *
     * @return the time of the bell.
     */
    public LocalTime getCallTime() {
        return callTime;
    }

    /**
     * Gets the type of the bell.
     *
     * @return the type of the bell.
     */
    public CallType getCallType() {
        return callType;
    }

    /**
     * Gets the associated lesson.
     *
     * @return the associated lesson.
     */
    public Lesson getLesson() {
        return lesson;
    }

    /**
     * Gets the associated break period.
     *
     * @return the associated break period.
     */
    public Break getBreakPeriod() {
        return breakPeriod;
    }

    // Setters with validation

    /**
     * Sets the ID of the call schedule.
     *
     * @param id the ID of the call schedule.
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Sets the associated schedule for this call schedule.
     *
     * @param schedule The schedule to be associated with this call schedule.
     */
    public void setSchedule(Schedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null");
        }
        this.schedule = schedule;
    }

    /**
     * Sets the call time with validation.
     *
     * @param callTime the time of the bell.
     * @throws IllegalArgumentException if callTime is null.
     */
    public void setCallTime(LocalTime callTime) {
        if (callTime == null) {
            log.error("Attempt to set null call time");
            throw new IllegalArgumentException("Call time cannot be null");
        }
        log.debug("Setting call time to {}", callTime);
        this.callTime = callTime;
    }

    /**
     * Sets the call type with validation.
     *
     * @param callType the type of the bell.
     * @throws IllegalArgumentException if callType is null.
     */
    public void setCallType(CallType callType) {
        if (callType == null) {
            log.error("Attempt to set null call type");
            throw new IllegalArgumentException("Call type cannot be null");
        }
        log.debug("Setting call type to {}", callType);
        this.callType = callType;
    }

    /**
     * Sets the associated lesson with validation of business rules.
     *
     * @param lesson the associated lesson.
     * @throws IllegalStateException if both lesson and breakPeriod are set.
     */
    public void setLesson(Lesson lesson) {
        if (lesson != null && this.breakPeriod != null) {
            log.warn("Attempt to set lesson while break period is set");
            throw new IllegalStateException("Cannot set lesson when break period is already set");
        }
        log.debug("Setting lesson: {}", lesson != null ? "ID=" + lesson.getId() : "null");
        this.lesson = lesson;
    }

    /**
     * Sets the associated break period with validation of business rules.
     *
     * @param breakPeriod the associated break period.
     * @throws IllegalStateException if both lesson and breakPeriod are set.
     */
    public void setBreakPeriod(Break breakPeriod) {
        if (breakPeriod != null && this.lesson != null) {
            log.warn("Attempt to set break period while lesson is set");
            throw new IllegalStateException("Cannot set break period when lesson is already set");
        }
        log.debug("Setting break period: {}", breakPeriod != null ? "ID=" + breakPeriod.getId() : "null");
        this.breakPeriod = breakPeriod;
    }

    /**
     * Validates the entity before saving.
     *
     * Ensures that required fields are set correctly before persisting.
     *
     * @throws IllegalArgumentException if required fields are missing.
     */
    @PrePersist
    @PreUpdate
    protected void validateBeforeSave() {
        if (callTime == null) {
            throw new IllegalArgumentException("Call time must be set");
        }
        if (callType == null) {
            throw new IllegalArgumentException("Call type must be set");
        }
        if (lesson == null && breakPeriod == null) {
            throw new IllegalArgumentException("Either lesson or break period must be set");
        }
        log.debug("Call schedule validation passed before save");
    }

    /**
     * Provides a string representation of the call schedule.
     *
     * @return the string representation of the call schedule.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("CallSchedule{");
        builder.append("id=").append(id)
               .append(", callTime=").append(callTime)
               .append(", callType=").append(callType);
        if (lesson != null) {
            builder.append(", lessonId=").append(lesson.getId());
        }
        if (breakPeriod != null) {
            builder.append(", breakPeriodId=").append(breakPeriod.getId());
        }
        builder.append('}');
        return builder.toString();
    }

    /**
     * Compares this object with another for equality.
     *
     * @param o the object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallSchedule that = (CallSchedule) o;
        return id != null && id.equals(that.id);
    }

    /**
     * Generates a hash code for the call schedule.
     *
     * @return the hash code for the call schedule.
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

	
}
