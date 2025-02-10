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
@Table(name = "breaks")
public class Break {

    private static final Logger log = LoggerFactory.getLogger(Break.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the break.
     */
    @NotNull(message = "Break name cannot be null")
    @Size(min = 2, max = 50, message = "Break name should be between 2 and 50 characters")
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Start time of the break.
     */
    @NotNull(message = "Start time cannot be null")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    /**
     * Duration of the break.
     */
    @NotNull(message = "Duration cannot be null")
    @Column(name = "duration", nullable = false)
    private Duration duration;

    /**
     * Default constructor.
     */
    public Break() {
    }

    /**
     * Constructor with name, start time, and duration.
     *
     * @param name the name of the break.
     * @param startTime the start time of the break.
     * @param duration the duration of the break.
     */
    public Break(String name, LocalTime startTime, Duration duration) {
        this.setName(name);
        this.setStartTime(startTime);
        this.setDuration(duration);
    }

    /**
     * Constructor with name and start time only.
     * Duration is set to a default of 5 minutes.
     *
     * @param name the name of the break.
     * @param startTime the start time of the break.
     */
    public Break(String name, LocalTime startTime) {
        this(name, startTime, Duration.ofMinutes(5));
    }

    // Getters

    /**
     * Returns the ID of the break.
     *
     * @return the break ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the name of the break.
     *
     * @return the break name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the start time of the break.
     *
     * @return the start time.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the duration of the break.
     *
     * @return the duration.
     */
    public Duration getDuration() {
        return duration;
    }

    // Setters with validation and logging

    /**
     * Sets the ID of the break.
     *
     * @param id the break ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the name of the break.
     *
     * @param name the break name.
     * @throws IllegalArgumentException if the name is null or empty.
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.error("Attempt to set empty break name");
            throw new IllegalArgumentException("Break name cannot be null or empty");
        }
        log.debug("Setting break name to {}", name);
        this.name = name.trim();
    }

    /**
     * Sets the start time of the break.
     *
     * @param startTime the start time.
     * @throws IllegalArgumentException if startTime is null.
     */
    public void setStartTime(LocalTime startTime) {
        if (startTime == null) {
            log.error("Attempt to set null start time for break");
            throw new IllegalArgumentException("Start time cannot be null");
        }
        log.debug("Setting break start time to {}", startTime);
        this.startTime = startTime;
    }

    /**
     * Sets the duration of the break.
     *
     * @param duration the duration.
     * @throws IllegalArgumentException if duration is null or negative.
     */
    public void setDuration(Duration duration) {
        if (duration == null || duration.isNegative()) {
            log.error("Attempt to set invalid duration for break");
            throw new IllegalArgumentException("Duration cannot be null or negative");
        }
        log.debug("Setting break duration to {}", duration);
        this.duration = duration;
    }

    /**
     * Returns a string representation of the break.
     *
     * @return a string representing the break.
     */
    @Override
    public String toString() {
        return String.format("Break{id=%d, name='%s', startTime=%s, duration=%s}",
                id, name, startTime, duration);
    }

    /**
     * Compares this break with another for equality.
     *
     * @param o the object to compare with.
     * @return true if the breaks are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Break aBreak = (Break) o;
        return id != null && id.equals(aBreak.id);
    }

    /**
     * Generates a hash code for the break.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
