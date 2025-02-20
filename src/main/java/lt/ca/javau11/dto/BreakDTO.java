package lt.ca.javau11.dto;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Data Transfer Object for break operations.
 * Contains basic break information for API operations.
 *
 * @author Lika Makejeva
 * @version 1.0
 * @since 2.0
 */
public class BreakDTO {
    private Long id;
    private String name;
    private LocalTime startTime;
    private Duration duration;
    private Long scheduleId;

    public BreakDTO() {
    }

    public BreakDTO(Long id, String name, LocalTime startTime, 
                   Duration duration, Long scheduleId) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.duration = duration;
        this.scheduleId = scheduleId;
    }

    // Getters
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
        return duration;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public String toString() {
        return "BreakDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", scheduleId=" + scheduleId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BreakDTO breakDTO = (BreakDTO) o;
        return id != null ? id.equals(breakDTO.id) : breakDTO.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}