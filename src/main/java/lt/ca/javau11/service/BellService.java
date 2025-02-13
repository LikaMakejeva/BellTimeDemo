package lt.ca.javau11.service;

import lt.ca.javau11.entity.CallSchedule;
import lt.ca.javau11.repository.CallScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

/**
 * BellService is responsible for periodically checking scheduled bell calls
 * and triggering the appropriate actions when a bell is due to ring.
 * <p>
 * This service uses the CallScheduleRepository to find all CallSchedule entities that
 * match the current time (with minute precision). Depending on the call type (e.g., lesson start,
 * break, preliminary call), the service triggers the corresponding action (e.g., playing a melody,
 * sending a notification, or invoking an external service).
 * </p>
 */
@Service
public class BellService {

    private static final Logger log = LoggerFactory.getLogger(BellService.class);
    private final CallScheduleRepository callScheduleRepository;

    /**
     * Constructs a new BellService with the specified CallScheduleRepository.
     *
     * @param callScheduleRepository the repository used to access CallSchedule data.
     */
    public BellService(CallScheduleRepository callScheduleRepository) {
        this.callScheduleRepository = callScheduleRepository;
    }

    /**
     * Checks every minute whether it is time for a bell to ring.
     * <p>
     * This method is executed every 60 seconds using the @Scheduled annotation. It obtains the current
     * time with seconds and nanoseconds set to zero (minute precision), then queries the repository
     * for any scheduled calls that match the current time. If any scheduled calls are found,
     * the service triggers the corresponding action for each call.
     * </p>
     */
    @Scheduled(fixedRate = 60000)
    public void checkAndRingBell() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        log.debug("Checking for bell calls at time: {}", now);

        List<CallSchedule> calls = callScheduleRepository.findByCallTime(now);
        if (!calls.isEmpty()) {
            for (CallSchedule call : calls) {
                log.info("Ringing bell for schedule: {}", call);
                // TODO: Implement bell ringing logic (e.g., play sound, send notification, call external service)
            }
        }
    }
}
