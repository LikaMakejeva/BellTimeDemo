package lt.ca.javau11.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object for special schedule operations.
 * Contains information about special schedules and their associated breaks.
 *
 * @author Lika Makejeva
 * @version 1.0
 * @since 2.0
 */
public class SpecialScheduleDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate effectiveDate;
    private boolean isActive;
    private List<Long> breakIds = new ArrayList<>();

    public SpecialScheduleDTO() {
    }

    public SpecialScheduleDTO(Long id, String name, String description,
                            LocalDate effectiveDate, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.effectiveDate = effectiveDate;
        this.isActive = isActive;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<Long> getBreakIds() {
        return breakIds;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setBreakIds(List<Long> breakIds) {
        this.breakIds = breakIds != null ? breakIds : new ArrayList<>();
    }

	@Override
	public String toString() {
		return "SpecialScheduleDTO [id=" + id + ", name=" + name + ", description=" + description + ", effectiveDate="
				+ effectiveDate + ", isActive=" + isActive + ", breakIds=" + breakIds + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(breakIds, description, effectiveDate, id, isActive, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpecialScheduleDTO other = (SpecialScheduleDTO) obj;
		return Objects.equals(breakIds, other.breakIds) && Objects.equals(description, other.description)
				&& Objects.equals(effectiveDate, other.effectiveDate) && Objects.equals(id, other.id)
				&& isActive == other.isActive && Objects.equals(name, other.name);
	}

}