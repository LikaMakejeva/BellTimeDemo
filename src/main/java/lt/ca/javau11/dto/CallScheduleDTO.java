package lt.ca.javau11.dto;

import java.time.LocalTime;

/**
 * Data Transfer Object for call schedule operations.
 * Contains information about lesson calls and timings.
 *
 * @author Lika Makejeva
 * @version 1.0
 * @since 2.0
 */
public class CallScheduleDTO {
    private Long id;
    private Long lessonId;
    private LocalTime callTimeStart;
    private LocalTime preliminaryCallTime;
    private LocalTime callTimeEnd;
    private boolean isActive;

    public CallScheduleDTO() {
    }

    public CallScheduleDTO(Long id, Long lessonId, LocalTime callTimeStart,
                         LocalTime preliminaryCallTime, LocalTime callTimeEnd, boolean isActive) {
        this.id = id;
        this.lessonId = lessonId;
        this.callTimeStart = callTimeStart;
        this.preliminaryCallTime = preliminaryCallTime;
        this.callTimeEnd = callTimeEnd;
        this.isActive = isActive;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public LocalTime getCallTimeStart() {
        return callTimeStart;
    }

    public LocalTime getPreliminaryCallTime() {
        return preliminaryCallTime;
    }

    public LocalTime getCallTimeEnd() {
        return callTimeEnd;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public void setCallTimeStart(LocalTime callTimeStart) {
        this.callTimeStart = callTimeStart;
    }

    public void setPreliminaryCallTime(LocalTime preliminaryCallTime) {
        this.preliminaryCallTime = preliminaryCallTime;
    }

    public void setCallTimeEnd(LocalTime callTimeEnd) {
        this.callTimeEnd = callTimeEnd;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "CallScheduleDTO{" +
                "id=" + id +
                ", lessonId=" + lessonId +
                ", callTimeStart=" + callTimeStart +
                ", preliminaryCallTime=" + preliminaryCallTime +
                ", callTimeEnd=" + callTimeEnd +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallScheduleDTO that = (CallScheduleDTO) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}