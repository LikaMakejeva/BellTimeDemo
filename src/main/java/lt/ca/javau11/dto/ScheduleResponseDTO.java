package lt.ca.javau11.dto;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A Data Transfer Object (DTO) for providing detailed schedule information.
 * Contains comprehensive information about a schedule including lessons, breaks,
 * and statistical data.
 *
 * @author Lika Makejeva
 * @version 1.0
 * @since 2.0
 */
public class ScheduleResponseDTO {
    private Long id;
    private LocalDate effectiveDate;
    private DayOfWeek dayOfWeek;
    private LocalTime firstLessonStart;
    private Duration lessonDuration;
    private boolean isActive;
    private List<LessonDTO> lessons = new ArrayList<>();
    private List<BreakDTO> breaks = new ArrayList<>();
    private int totalLessons;
    private int totalBreaks;
    private Duration totalDuration;
    private Duration totalBreakTime;
    private LocalTime scheduleStartTime;
    private LocalTime scheduleEndTime;
    private boolean isSpecialSchedule;
    private String specialScheduleDescription;

    /**
     * Default constructor.
     */
    public ScheduleResponseDTO() {
    }

    /**
     * Constructs a ScheduleResponseDTO with essential fields.
     *
     * @param id The schedule ID
     * @param effectiveDate The effective date of the schedule
     * @param dayOfWeek The day of week
     * @param firstLessonStart The start time of first lesson
     * @param isActive Whether the schedule is active
     */
    public ScheduleResponseDTO(Long id, LocalDate effectiveDate, DayOfWeek dayOfWeek,
                             LocalTime firstLessonStart, boolean isActive) {
        this.id = id;
        this.effectiveDate = effectiveDate;
        this.dayOfWeek = dayOfWeek;
        this.firstLessonStart = firstLessonStart;
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

    public List<LessonDTO> getLessons() {
        return lessons;
    }

    public List<BreakDTO> getBreaks() {
        return breaks;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public int getTotalBreaks() {
        return totalBreaks;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public Duration getTotalBreakTime() {
        return totalBreakTime;
    }

    public LocalTime getScheduleStartTime() {
        return scheduleStartTime;
    }

    public LocalTime getScheduleEndTime() {
        return scheduleEndTime;
    }

    public boolean isSpecialSchedule() {
        return isSpecialSchedule;
    }

    public String getSpecialScheduleDescription() {
        return specialScheduleDescription;
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

    public void setLessons(List<LessonDTO> lessons) {
        this.lessons = lessons != null ? lessons : new ArrayList<>();
    }

    public void setBreaks(List<BreakDTO> breaks) {
        this.breaks = breaks != null ? breaks : new ArrayList<>();
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }

    public void setTotalBreaks(int totalBreaks) {
        this.totalBreaks = totalBreaks;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void setTotalBreakTime(Duration totalBreakTime) {
        this.totalBreakTime = totalBreakTime;
    }

    public void setScheduleStartTime(LocalTime scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }

    public void setScheduleEndTime(LocalTime scheduleEndTime) {
        this.scheduleEndTime = scheduleEndTime;
    }

    public void setSpecialSchedule(boolean specialSchedule) {
        isSpecialSchedule = specialSchedule;
    }

    public void setSpecialScheduleDescription(String specialScheduleDescription) {
        this.specialScheduleDescription = specialScheduleDescription;
    }

    /**
     * Adds a lesson to the schedule.
     *
     * @param lesson The lesson to add
     */
    public void addLesson(LessonDTO lesson) {
        if (lesson != null) {
            lessons.add(lesson);
            totalLessons = lessons.size();
        }
    }

    /**
     * Adds a break to the schedule.
     *
     * @param breakTime The break to add
     */
    public void addBreak(BreakDTO breakTime) {
        if (breakTime != null) {
            breaks.add(breakTime);
            totalBreaks = breaks.size();
        }
    }

    /**
     * Calculates total durations for lessons and breaks.
     */
    public void calculateTotals() {
        this.totalLessons = lessons.size();
        this.totalBreaks = breaks.size();
        
        // Calculate total lesson duration
        this.totalDuration = lessons.stream()
                .map(LessonDTO::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        // Calculate total break time
        this.totalBreakTime = breaks.stream()
                .map(BreakDTO::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public String toString() {
        return "ScheduleResponseDTO{" +
                "id=" + id +
                ", effectiveDate=" + effectiveDate +
                ", dayOfWeek=" + dayOfWeek +
                ", firstLessonStart=" + firstLessonStart +
                ", isActive=" + isActive +
                ", totalLessons=" + totalLessons +
                ", totalBreaks=" + totalBreaks +
                ", scheduleStartTime=" + scheduleStartTime +
                ", scheduleEndTime=" + scheduleEndTime +
                ", isSpecialSchedule=" + isSpecialSchedule +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduleResponseDTO that = (ScheduleResponseDTO) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}