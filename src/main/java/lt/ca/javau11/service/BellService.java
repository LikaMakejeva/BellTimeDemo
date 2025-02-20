package lt.ca.javau11.service;

import lt.ca.javau11.entity.CallSchedule;
import lt.ca.javau11.entity.CallType;
import lt.ca.javau11.validation.ValidationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

/**
 * BellService is responsible for periodically checking scheduled bell calls
 * and triggering the appropriate actions when a bell is due to ring.
 */
@Service
public class BellService {

	private static final Logger log = LoggerFactory.getLogger(BellService.class);
    private final CallScheduleService callScheduleService;
    private final ValidationService validationService;
    /**
     * Constructs a new BellService with the specified service.
     *
     * @param callScheduleService the service used to access CallSchedule data.
     */
    public BellService(CallScheduleService callScheduleService, ValidationService validationService) {
        this.callScheduleService = callScheduleService;
        this.validationService = validationService;
    }

    /**
     * Checks every minute whether it is time for a bell to ring.
     * Uses a time tolerance of ±30 seconds to avoid missing scheduled calls due to minor delays.
     */
    @Scheduled(fixedRate = 60000)
    public void checkAndRingBell() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        log.debug("Checking for bell calls at time: {}", now);

        // Using a time range of ±30 seconds for reliability
        List<CallSchedule> calls = callScheduleService.findAllByTimeRange(now.minusSeconds(30), now.plusSeconds(30));

        for (CallSchedule call : calls) {
            try {
                validationService.validateBellService(call); // 🔥 Валидация звонка перед воспроизведением
                triggerBell(call);
            } catch (IllegalArgumentException e) {
                log.warn("Skipping invalid call schedule: {}", e.getMessage());
            }
        }
    }

    /**
     * Triggers the bell by playing the corresponding sound file.
     *
     * @param call the call schedule to trigger.
     */
    private void triggerBell(CallSchedule call) {
        log.info("🔔 Ringing bell for schedule: {}", call);

        // Определяем, какой файл нужно воспроизвести
        String soundFile = getSoundFile(call.getCallType());

        if (soundFile == null) {
            log.warn("No sound file found for call type: {}", call.getCallType());
            return;
        }

        try {
            File audioFile = new File("sounds/" + soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            log.info("🔊 Playing sound: {}", soundFile);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            log.error("Error playing sound file: {}", soundFile, e);
        }
    }

    /**
     * Returns the corresponding sound file for the given call type.
     *
     * @param callType the type of call
     * @return the filename of the corresponding sound file
     */
    private String getSoundFile(CallType callType) {
        switch (callType) {
            case LESSON_START:
                return "lesson_start.wav";
            case BREAK_START:
                return "break_start.wav";
            case PRELIMINARY_CALL:
                return "preliminary_call.wav";
            default:
                return null;
        }
    }
}
