package lt.ca.javau11.service;

import lt.ca.javau11.entity.Break;
import lt.ca.javau11.repository.BreakRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing break periods in the school schedule.
 * Provides business logic for CRUD operations on break periods.
 */
@Service
public class BreakService {

    private static final Logger log = LoggerFactory.getLogger(BreakService.class);
    private final BreakRepository breakRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param breakRepository the repository for break entities.
     */
    public BreakService(BreakRepository breakRepository) {
        this.breakRepository = breakRepository;
    }

    /**
     * Retrieves all break periods.
     *
     * @return list of all break periods.
     */
    public List<Break> findAll() {
        log.info("Fetching all break periods");
        List<Break> breaks = breakRepository.findAll();
        log.info("Found {} break periods", breaks.size());
        return breaks;
    }

    /**
     * Saves a break period.
     *
     * @param breakPeriod the break period to save.
     * @return the saved break period.
     * @throws IllegalArgumentException if breakPeriod is null.
     */
    public Break save(Break breakPeriod) {
        if (breakPeriod == null) {
            log.error("Attempt to save null break period");
            throw new IllegalArgumentException("Break cannot be null");
        }
        log.info("Saving break period: {}", breakPeriod);
        return breakRepository.save(breakPeriod);
    }

    /**
     * Retrieves a break period by its ID.
     *
     * @param id the ID of the break period.
     * @return the found break period.
     * @throws IllegalArgumentException if no break is found with the given ID.
     */
    public Break findById(Long id) {
        log.info("Searching for break period with ID: {}", id);
        return breakRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Break period with ID {} not found", id);
                    return new IllegalArgumentException("Break period not found with ID: " + id);
                });
    }

    /**
     * Deletes a break period by its ID.
     *
     * @param id the ID of the break period to delete.
     */
    @Transactional
    public void delete(Long id) {
        log.info("Deleting break period with ID: {}", id);
        if (!breakRepository.existsById(id)) {
            log.error("Break period with ID {} not found", id);
            throw new IllegalArgumentException("Break period not found with ID: " + id);
        }
        breakRepository.deleteById(id);
        log.info("Break period with ID {} successfully deleted", id);
    }

    /**
     * Updates an existing break period.
     * The break period is updated based on the provided data.
     *
     * @param id the ID of the break period to update.
     * @param breakPeriod the updated break period data.
     * @return the updated break period.
     * @throws IllegalArgumentException if the break period is not found.
     */
    public Break update(Long id, Break breakPeriod) {
        log.info("Updating break period with ID: {}", id);
        Optional<Break> existingBreakOpt = breakRepository.findById(id);
        if (existingBreakOpt.isEmpty()) {
            log.error("Break period with ID {} not found for update", id);
            throw new IllegalArgumentException("Break period not found with ID: " + id);
        }
        Break existingBreak = existingBreakOpt.get();
        // Update fields
        existingBreak.setName(breakPeriod.getName());
        existingBreak.setStartTime(breakPeriod.getStartTime());
        existingBreak.setDuration(breakPeriod.getDuration());
        log.info("Break period with ID {} updated successfully", id);
        return breakRepository.save(existingBreak);
    }

    /**
     * Checks if a break period exists by its ID.
     *
     * @param id the ID of the break period.
     * @return true if the break exists, false otherwise.
     */
    public boolean existsById(Long id) {
        boolean exists = breakRepository.existsById(id);
        log.info("Checking existence of break period with ID {}: {}", id, exists);
        return exists;
    }
}
