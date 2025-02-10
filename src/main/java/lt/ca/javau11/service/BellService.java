
package lt.ca.javau11.service;

import lt.ca.javau11.entity.CallSchedule;
import lt.ca.javau11.repository.CallScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class BellService {

    private static final Logger log = LoggerFactory.getLogger(BellService.class);
    private final CallScheduleRepository callScheduleRepository;

    public BellService(CallScheduleRepository callScheduleRepository) {
        this.callScheduleRepository = callScheduleRepository;
    }

    /**
     * Checks every minute whether it is time for a bell to ring.
     * If the current time matches a scheduled bell time (to the minute),
     * the corresponding logic is executed (e.g., playing a melody, sending a notification, etc.).
     */
    @Scheduled(fixedRate = 60000)
    public void checkAndRingBell() {
        // Get the current time with minute precision
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        log.debug("Checking for bell calls at time: {}", now);

        // Search for bell schedules that match the current time
        List<CallSchedule> calls = callScheduleRepository.findByCallTime(now);
        if (!calls.isEmpty()) {
            for (CallSchedule call : calls) {
                // The ringing logic should be implemented here – for example,
                // playing a sound melody, sending a notification, calling an external service, etc.
                log.info("Ringing bell for schedule: {}", call);
                // Example: ringBell(call);
            }
        }
    }
}