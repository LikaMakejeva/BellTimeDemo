package lt.ca.javau11.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Entity representing a lesson in the school schedule.
 * Contains information about the lesson order, duration, subject, and related schedules.
 */
@Entity
@Table(name = "lessons", indexes = {
        @Index(name = "idx_lesson_schedule", columnList = "schedule_id"),
        @Index(name = "idx_lesson_order", columnList = "order_number, schedule_id", unique = true)
})
public class Lesson {
    private static final Logger log = LoggerFactory.getLogger(Lesson.class);

    /**
     * Unique identifier for the lesson.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Order number of the lesson in the schedule.
     */
    @Min(value = 1, message = "The lesson number must be greater than 0")
    @Column(name = "order_number", nullable = false)
    private int orderNumber;

    /**
     * Associated schedule for the lesson.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    /**
     * Associated call schedule for the lesson.
     */
    @OneToOne(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private CallSchedule callSchedule;

    /**
     * Duration of the lesson in minutes.
     */
    @Column(nullable = false)
    private int duration;

    /**
     * Subject name in English.
     */
    @NotNull(message = "Subject name in English is required")
    @Size(min = 1, max = 100, message = "Subject name must be between 1 and 100 characters")
    @Column(name = "subject_en", nullable = false, length = 100)
    private String subjectEn;

    /**
     * Default constructor.
     */
    public Lesson() {
    }

    /**
     * Constructor with main parameters.
     *
     * @param orderNumber Order number of the lesson.
     * @param subjectEn   Subject name in English.
     * @param schedule    Associated schedule.
     */
    public Lesson(int orderNumber, String subjectEn, Schedule schedule) {
        this.setOrderNumber(orderNumber);
        this.setSubjectEn(subjectEn);
        this.setSchedule(schedule);
    }

    // ✅ Getters

    /**
     * Gets the lesson ID.
     *
     * @return the lesson ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the order number of the lesson.
     *
     * @return the order number.
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * Gets the associated schedule.
     *
     * @return the schedule.
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Gets the associated call schedule.
     *
     * @return the call schedule.
     */
    public CallSchedule getCallSchedule() {
        return callSchedule;
    }

    /**
     * Gets the duration of the lesson.
     * If the associated schedule provides a positive lesson duration, that value is used.
     *
     * @return duration in minutes.
     */
    public int getDuration() {
        return (schedule != null && schedule.getLessonDuration() > 0) ? schedule.getLessonDuration() : duration;
    }

    /**
     * Gets the subject name in English.
     *
     * @return the subject name.
     */
    public String getSubjectEn() {
        return subjectEn;
    }
    /**
     * 🔥 **Добавленный метод**: Получение времени начала урока
     * @return start time of the lesson.
     * @throws IllegalStateException if the schedule is missing.
     */
    public LocalTime getStartTime() {
        if (schedule == null || schedule.getFirstLessonStart() == null) {
            log.warn("Schedule or first lesson start time is missing for lesson ID {}", id);
            throw new IllegalStateException("Cannot determine start time: schedule is missing");
        }
        return schedule.getFirstLessonStart().plusMinutes((long) (orderNumber - 1) * schedule.getLessonDuration());
    }
    // ✅ Setters

    public void setOrderNumber(int orderNumber) {
        if (orderNumber < 1) {
            log.error("Attempt to set invalid order number: {}", orderNumber);
            throw new IllegalArgumentException("The lesson number must be greater than 0");
        }
        log.debug("Setting order number to {}", orderNumber);
        this.orderNumber = orderNumber;
    }

    public void setSchedule(Schedule schedule) {
        if (schedule == null) {
            log.error("Attempt to set null schedule");
            throw new IllegalArgumentException("Schedule cannot be null");
        }
        log.debug("Setting schedule with ID: {}", schedule.getId());
        this.schedule = schedule;
    }

    public void setDuration(int duration) {
        if (duration < 1) {
            log.error("Attempt to set invalid duration: {}", duration);
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        log.debug("Setting duration to {} minutes", duration);
        this.duration = duration;
    }

    public void setSubjectEn(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            log.error("Attempt to set invalid subject name");
            throw new IllegalArgumentException("Subject name cannot be empty");
        }
        log.debug("Setting subject name to: {}", subject);
        this.subjectEn = subject.trim();
    }

    @PrePersist
    @PreUpdate
    protected void validateBeforeSave() {
        if (orderNumber < 1) {
            throw new IllegalArgumentException("Lesson order number must be greater than 0");
        }
        if (subjectEn == null || subjectEn.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be empty");
        }
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule must be set");
        }
        log.debug("Lesson validation passed before save");
    }

    @Override
    public String toString() {
        return String.format("Lesson{id=%d, orderNumber=%d, scheduleId=%s, duration=%d, subject='%s'}",
                id, orderNumber, Optional.ofNullable(schedule).map(Schedule::getId).orElse(null), getDuration(), subjectEn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return id != null && id.equals(lesson.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
