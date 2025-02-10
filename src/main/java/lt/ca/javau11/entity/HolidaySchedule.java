package lt.ca.javau11.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;

/**
 * Entity representing a holiday or vacation in the school schedule.
 * Contains information about the holiday date, description, and whether it is a working day.
 */
@Entity
@Table(name = "holiday_schedules", indexes = {
    @Index(name = "idx_holiday_date", columnList = "holiday_date", unique = true)
})
public class HolidaySchedule {
    private static final Logger log = LoggerFactory.getLogger(HolidaySchedule.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The date of the holiday.
     */
    @NotNull(message = "Holiday date cannot be null")
    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    /**
     * The description of the holiday.
     */
    @NotNull(message = "Description cannot be null")
    @Size(min = 2, max = 255, message = "Description must be between 2 and 255 characters")
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Indicates whether the holiday is a working day.
     */
    @Column(name = "is_working_day", nullable = false)
    private boolean workingDay = false;

    /**
     * Default constructor.
     */
    public HolidaySchedule() {
    }

    /**
     * Constructor with holiday date and description.
     *
     * @param holidayDate the date of the holiday.
     * @param description the description of the holiday.
     */
    public HolidaySchedule(LocalDate holidayDate, String description) {
        this.setHolidayDate(holidayDate);
        this.setDescription(description);
    }

    // Getters

    /**
     * Gets the unique ID of the holiday schedule.
     *
     * @return the ID of the holiday schedule.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the holiday date.
     *
     * @return the holiday date.
     */
    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    /**
     * Gets the holiday description.
     *
     * @return the description of the holiday.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if the holiday is a working day.
     *
     * @return true if the holiday is a working day, false otherwise.
     */
    public boolean isWorkingDay() {
        return workingDay;
    }

    // Setters with validation and logging

    /**
     * Sets the ID of the holiday schedule.
     *
     * @param id the ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the holiday date with validation.
     *
     * @param holidayDate the date to set.
     * @throws IllegalArgumentException if holidayDate is null.
     */
    public void setHolidayDate(LocalDate holidayDate) {
        if (holidayDate == null) {
            log.error("Attempt to set null holiday date");
            throw new IllegalArgumentException("Holiday date cannot be null");
        }
        // Optionally warn if the date is in the past.
        if (holidayDate.isBefore(LocalDate.now())) {
            log.warn("Setting holiday date in the past: {}", holidayDate);
        }
        log.debug("Setting holiday date to {}", holidayDate);
        this.holidayDate = holidayDate;
    }

    /**
     * Sets the holiday description with validation.
     *
     * @param description the description to set.
     * @throws IllegalArgumentException if description is null, empty, or exceeds 255 characters.
     */
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            log.error("Attempt to set empty description");
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (description.length() > 255) {
            log.error("Attempt to set too long description: {} characters", description.length());
            throw new IllegalArgumentException("Description cannot be longer than 255 characters");
        }
        String trimmed = description.trim();
        log.debug("Setting description to '{}'", trimmed);
        this.description = trimmed;
    }

    /**
     * Sets whether the holiday is a working day.
     *
     * @param workingDay true if it is a working day, false otherwise.
     */
    public void setWorkingDay(boolean workingDay) {
        this.workingDay = workingDay;
    }

    /**
     * Validates the entity before persisting or updating.
     * Ensures that required fields are set.
     *
     * @throws IllegalArgumentException if required fields are missing.
     */
    @PrePersist
    @PreUpdate
    protected void validateBeforeSave() {
        if (holidayDate == null) {
            throw new IllegalArgumentException("Holiday date must be set");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description must be set");
        }
        log.debug("Holiday schedule validation passed before save");
    }

    /**
     * Returns a string representation of the holiday schedule.
     *
     * @return string representation of the holiday schedule.
     */
    @Override
    public String toString() {
        return String.format("HolidaySchedule{id=%d, holidayDate=%s, description='%s', workingDay=%s}",
                id, holidayDate, description, workingDay);
    }

    /**
     * Compares this holiday schedule with another for equality.
     *
     * @param o the object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HolidaySchedule that = (HolidaySchedule) o;
        return id != null && id.equals(that.id);
    }

    /**
     * Generates a hash code for the holiday schedule.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
