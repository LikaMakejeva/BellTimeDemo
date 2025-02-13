package lt.ca.javau11.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a calendar event.
 * Used for transferring lesson and break schedule data to the frontend calendar.
 * 
 * This DTO is designed to be lightweight and separate from the database entity.
 * It includes only the necessary fields for calendar display.
 * 
 * @author Lika Makejeva
 * @version 1.0
 * @since 1.0
 */
public class ScheduleEventDTO {
    
    private Long id;
    private String title;       // Event title (lesson or break)
    private LocalDateTime start; // Start time of the event
    private LocalDateTime end;   // End time of the event
    private String color;       // Display color in the calendar
    private boolean allDay;     // Whether the event lasts the whole day

    /**
     * Default constructor.
     */
    public ScheduleEventDTO() {
    }

    /**
     * Constructor with parameters.
     * 
     * @param id    Event ID
     * @param title Event title (e.g., "Math", "Break")
     * @param start Event start time
     * @param end   Event end time
     * @param color Event color (e.g., blue for lessons, green for breaks)
     * @param allDay Whether the event lasts the whole day
     */
    public ScheduleEventDTO(Long id, String title, LocalDateTime start, LocalDateTime end, String color, boolean allDay) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.color = color;
        this.allDay = allDay;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    @Override
    public String toString() {
        return "ScheduleEventDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", color='" + color + '\'' +
                ", allDay=" + allDay +
                '}';
    }
}
