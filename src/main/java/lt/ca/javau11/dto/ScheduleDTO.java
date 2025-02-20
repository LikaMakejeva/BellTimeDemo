package lt.ca.javau11.dto;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for schedule operations.
 * Contains basic schedule information for API operations.
 *
 * @author Lika Makejeva
 * @version 1.0
 * @since 2.0
 */
public class ScheduleDTO {
    private Long id;
    private LocalDate effectiveDate;
    private DayOfWeek dayOfWeek;
    private LocalTime firstLessonStart;
    private Duration lessonDuration;
    private boolean isActive;
    private List<Long> lessonIds = new ArrayList<>();
    private List<Long> breakIds = new ArrayList<>();

    public ScheduleDTO() {
    }

    public ScheduleDTO(Long id, LocalDate effectiveDate, DayOfWeek dayOfWeek,
                      LocalTime firstLessonStart, Duration lessonDuration, boolean isActive) {
        this.id = id;
        this.effectiveDate = effectiveDate;
        this.dayOfWeek = dayOfWeek;
        this.firstLessonStart = firstLessonStart;
        this.lessonDuration = lessonDuration;
        this.isActive = isActive;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getFirstLessonStart() {
        return firstLessonStart;
    }

    public Duration getLessonDuration() {
        return lessonDuration;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<Long> getLessonIds() {
        return lessonIds;
    }

    public List<Long> getBreakIds() {
        return breakIds;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setFirstLessonStart(LocalTime firstLessonStart) {
        this.firstLessonStart = firstLessonStart;
    }

    public void setLessonDuration(Duration lessonDuration) {
        this.lessonDuration = lessonDuration;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setLessonIds(List<Long> lessonIds) {
        this.lessonIds = lessonIds != null ? lessonIds : new ArrayList<>();
    }

    public void setBreakIds(List<Long> breakIds) {
        this.breakIds = breakIds != null ? breakIds : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ScheduleDTO{" +
                "id=" + id +
                ", effectiveDate=" + effectiveDate +
                ", dayOfWeek=" + dayOfWeek +
                ", firstLessonStart=" + firstLessonStart +
                ", lessonDuration=" + lessonDuration +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleDTO that = (ScheduleDTO) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}