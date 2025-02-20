package lt.ca.javau11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BellTimeApplication is the main entry point of the School Bell Scheduler application.
 * <p>
 * This application manages various aspects of school bell schedules, including:
 * <ul>
 *   <li>Lessons and their durations (Lesson entity and LessonService/Controller)</li>
 *   <li>Regular and special schedules (Schedule, SpecialSchedule entities and corresponding services/controllers)</li>
 *   <li>Holiday schedules (HolidaySchedule entity and related components)</li>
 *   <li>Call schedules for bell ringing (CallSchedule entity and corresponding components)</li>
 *   <li>User management (User entity and related services/controllers)</li>
 *   <li>Break periods (Break entity and related components)</li>
 *   <li>Various enumerations (CallType, ScheduleType, DayOfWeek, etc.)</li>
 *   <li>Global exception handling for robust error management</li>
 *   <li>Automated scheduling for managing periodic tasks</li>
 * </ul>
 * The application uses:
 * <ul>
 *   <li>Spring Boot</li>
 *   <li>Spring Data JPA</li>
 *   <li>Bean Validation (Jakarta Validation)</li>
 *   <li>Scheduled Tasks with Spring Scheduling</li>
 *   <li>SLF4J for structured logging</li>
 * </ul>
 *
 * @author  
 * @version 1.1
 * @since 1.0
 */
@SpringBootApplication
@EnableScheduling  // Enables scheduling tasks for automated operations
public class BellTimeApplication {

    private static final Logger log = LoggerFactory.getLogger(BellTimeApplication.class);

    /**
     * Main method to launch the application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        log.info("Starting BellTimeApplication...");

        try {
            SpringApplication.run(BellTimeApplication.class, args);
            log.info("BellTimeApplication started successfully.");
        } catch (Exception e) {
            log.error("Application failed to start due to an exception: {}", e.getMessage(), e);
            System.exit(1);  // Terminate process with an error code if the startup fails
        }
    }
}
