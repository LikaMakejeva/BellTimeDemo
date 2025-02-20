package lt.ca.javau11.dto;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Data Transfer Object for lesson operations.
 * Contains basic lesson information for API operations.
 *
 * @author Lika Makejeva
 * @version 1.0
 * @since 2.0
 */
public class LessonDTO {
    private Long id;
    private String subjectEn;
    private int orderNumber;
    private LocalTime startTime;
    private Duration duration;
    private Long scheduleId;
    private Long callScheduleId;

    public LessonDTO() {
    }

    public LessonDTO(Long id, String subjectEn, int orderNumber, 
                    LocalTime startTime, Duration duration, 
                    Long scheduleId, Long callScheduleId) {
        this.id = id;
        this.subjectEn = subjectEn;
        this.orderNumber = orderNumber;
        this.startTime = startTime;
        this.duration = duration;
        this.scheduleId = scheduleId;
        this.callScheduleId = callScheduleId;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getSubjectEn() {
        return subjectEn;
    }

    public int getOrderNumber() {
        return orderNumber;
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

    public Long getCallScheduleId() {
        return callScheduleId;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setSubjectEn(String subjectEn) {
        this.subjectEn = subjectEn;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
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

    public void setCallScheduleId(Long callScheduleId) {
        this.callScheduleId = callScheduleId;
    }

    @Override
    public String toString() {
        return "LessonDTO{" +
                "id=" + id +
                ", subjectEn='" + subjectEn + '\'' +
                ", orderNumber=" + orderNumber +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", scheduleId=" + scheduleId +
                ", callScheduleId=" + callScheduleId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonDTO lessonDTO = (LessonDTO) o;
        return id != null ? id.equals(lessonDTO.id) : lessonDTO.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

