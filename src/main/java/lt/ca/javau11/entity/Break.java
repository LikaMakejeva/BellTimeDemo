package lt.ca.javau11.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalTime;
import java.time.Duration;

/**
 * Entity representing a break period in the school schedule.
 * Contains information about the break name, start time, and duration.
 */
@Entity
@Table(name = "breaks", indexes = {
        @Index(name = "idx_break_start_time", columnList = "start_time"),
        @Index(name = "idx_break_duration", columnList = "duration")
})
public class Break {

    private static final Logger log = LoggerFactory.getLogger(Break.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the break. */
    @NotNull(message = "Break name cannot be null")
    @Size(min = 2, max = 50, message = "Break name should be between 2 and 50 characters")
    @Column(name = "name", nullable = false)
    private String name;

    /** Start time of the break. */
    @NotNull(message = "Start time cannot be null")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    /** Duration of the break. */
    @NotNull(message = "Duration cannot be null")
    @Column(name = "duration", nullable = false)
    private Duration breakDuration = Duration.ofMinutes(10);
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;



    /**
     * Default constructor.
     */
    public Break() {}

    /**
     * Constructor with name, start time, and duration.
     *
     * @param name the name of the break.
     * @param startTime the start time of the break.
     * @param duration the duration of the break.
     */
    public Break(String name, LocalTime startTime, Duration duration, Schedule schedule) {
        this.name = name;
        this.startTime = startTime;
        this.breakDuration = duration;
        this.schedule = schedule;
        
        log.info("Created Break: {} starting at {}, lasting {}, linked to Schedule ID {}", 
                 name, startTime, duration, schedule != null ? schedule.getId() : "null");
    }


    /**
     * Constructor with name and start time only.
     * Duration is set to a default of 5 minutes.
     *
     * @param name the name of the break.
     * @param startTime the start time of the break.
     */
    public Break(String name, LocalTime startTime) {
        this(name, startTime, Duration.ofMinutes(10),null); 
    }

    // ✅ **Getters**

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return breakDuration;
    }
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Computes the end time of the break dynamically.
     * This value is not stored in the database.
     *
     * @return the calculated end time.
     */
    @Transient
    public LocalTime getEndTime() {
        return startTime.plus(breakDuration);
    }

    // ✅ **Setters with validation and logging**

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.error("Attempt to set empty break name");
            throw new IllegalArgumentException("Break name cannot be null or empty");
        }
        log.debug("Setting break name to {}", name);
        this.name = name.trim();
    }

    public void setStartTime(LocalTime startTime) {
        if (startTime == null) {
            log.error("Attempt to set null start time for break");
            throw new IllegalArgumentException("Start time cannot be null");
        }
        log.debug("Setting break start time to {}", startTime);
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        if (duration == null || duration.isNegative()) {
            log.error("Attempt to set invalid duration for break");
            throw new IllegalArgumentException("Duration cannot be null or negative");
        }
        log.debug("Setting break duration to {}", duration);
        this.breakDuration = duration;
    }
    public void setSchedule(Schedule schedule) {
        if (schedule == null) {
            log.error("Attempt to assign null schedule to break");
            throw new IllegalArgumentException("Schedule cannot be null for a break");
        }
        log.debug("Assigning schedule ID {} to break '{}'", schedule.getId(), name);
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return String.format("Break{id=%d, name='%s', startTime=%s, duration=%s, endTime=%s}",
                id, name, startTime, breakDuration, getEndTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Break aBreak = (Break) o;
        return id != null && id.equals(aBreak.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
