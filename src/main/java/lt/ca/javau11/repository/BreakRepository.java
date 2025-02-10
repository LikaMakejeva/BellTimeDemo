package lt.ca.javau11.repository;

import lt.ca.javau11.entity.Break;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link Break} entities.
 * Provides basic CRUD operations and custom query methods.
 */
public interface BreakRepository extends JpaRepository<Break, Long> {

    /**
     * Finds a {@link Break} by its name.
     *
     * @param name the name of the break.
     * @return an Optional containing the Break if found, or empty if not found.
     */
    Optional<Break> findByName(String name);

    /**
     * Finds a {@link Break} by its start time.
     *
     * @param startTime the start time of the break.
     * @return an Optional containing the Break if found, or empty if not found.
     */
    Optional<Break> findByStartTime(LocalTime startTime);
}
