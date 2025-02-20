package lt.ca.javau11.dto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A Data Transfer Object (DTO) for providing detailed lesson information.
 * Contains extended information about a lesson including schedule and call details.
 *
 * @author Lika Makejeva
 * @version 1.0
 * @since 2.0
 */
public class LessonResponseDTO {
    private Long id;
    private String subjectEn;
    private int orderNumber;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration duration;
    private Long scheduleId;
    private LocalDate scheduleDate;
    private LocalTime callTimeStart;
    private LocalTime preliminaryCallTime;
    private LocalTime callTimeEnd;
    private boolean isActive;
    private String nextBreakDuration;
    private String previousBreakDuration;

    /**
     * Default constructor.
     */
    public LessonResponseDTO() {
    }

    /**
     * Constructs a LessonResponseDTO with all fields.
     *
     * @param id The lesson ID
     * @param subjectEn The subject name in English
     * @param orderNumber The order number in schedule
     * @param startTime The start time
     * @param endTime The end time
     * @param duration The lesson duration
     * @param scheduleId The associated schedule ID
     * @param scheduleDate The date of the schedule
     * @param callTimeStart The start call time
     * @param preliminaryCallTime The preliminary call time
     * @param callTimeEnd The end call time
     * @param isActive Whether the lesson is active
     * @param nextBreakDuration Duration of the next break
     * @param previousBreakDuration Duration of the previous break
     */
    public LessonResponseDTO(Long id, String subjectEn, int orderNumber,
                           LocalTime startTime, LocalTime endTime, Duration duration,
                           Long scheduleId, LocalDate scheduleDate,
                           LocalTime callTimeStart, LocalTime preliminaryCallTime,
                           LocalTime callTimeEnd, boolean isActive,
                           String nextBreakDuration, String previousBreakDuration) {
        this.id = id;
        this.subjectEn = subjectEn;
        this.orderNumber = orderNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.scheduleId = scheduleId;
        this.scheduleDate = scheduleDate;
        this.callTimeStart = callTimeStart;
        this.preliminaryCallTime = preliminaryCallTime;
        this.callTimeEnd = callTimeEnd;
        this.isActive = isActive;
        this.nextBreakDuration = nextBreakDuration;
        this.previousBreakDuration = previousBreakDuration;
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

    public LocalTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
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

    public String getNextBreakDuration() {
        return nextBreakDuration;
    }

    public String getPreviousBreakDuration() {
        return previousBreakDuration;
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

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
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

    public void setNextBreakDuration(String nextBreakDuration) {
        this.nextBreakDuration = nextBreakDuration;
    }

    public void setPreviousBreakDuration(String previousBreakDuration) {
        this.previousBreakDuration = previousBreakDuration;
    }

    @Override
    public String toString() {
        return "LessonResponseDTO{" +
                "id=" + id +
                ", subjectEn='" + subjectEn + '\'' +
                ", orderNumber=" + orderNumber +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", scheduleId=" + scheduleId +
                ", scheduleDate=" + scheduleDate +
                ", callTimeStart=" + callTimeStart +
                ", preliminaryCallTime=" + preliminaryCallTime +
                ", callTimeEnd=" + callTimeEnd +
                ", isActive=" + isActive +
                ", nextBreakDuration='" + nextBreakDuration + '\'' +
                ", previousBreakDuration='" + previousBreakDuration + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonResponseDTO that = (LessonResponseDTO) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}