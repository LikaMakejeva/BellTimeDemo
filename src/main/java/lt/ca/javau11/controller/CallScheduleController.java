package lt.ca.javau11.controller;

import lt.ca.javau11.entity.CallSchedule;
import lt.ca.javau11.entity.CallType;
import lt.ca.javau11.service.CallScheduleService;
import lt.ca.javau11.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.ObjectError;

@RestController
@RequestMapping("/api/call-schedules")
@Validated  // Ensure validation of input request body
public class CallScheduleController {

    private static final Logger log = LoggerFactory.getLogger(CallScheduleController.class);
    private final CallScheduleService callScheduleService;

    // Constructor for dependency injection
    public CallScheduleController(CallScheduleService callScheduleService) {
        this.callScheduleService = callScheduleService;
    }

    /**
     * Get all call schedules.
     *
     * @return list of all call schedules.
     */
    @GetMapping
    public List<CallSchedule> getAllCallSchedules() {
        log.info("Fetching all call schedules");
        return callScheduleService.findAll();
    }

    /**
     * Create a new call schedule.
     *
     * @param callSchedule the new call schedule to be created.
     * @return the saved call schedule.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CallSchedule createCallSchedule(@Valid @RequestBody CallSchedule callSchedule) {
        log.info("Creating new call schedule: {}", callSchedule);
        return callScheduleService.save(callSchedule);
    }

    /**
     * Get a call schedule by ID.
     *
     * @param id the ID of the call schedule.
     * @return the found call schedule.
     * @throws ResourceNotFoundException if the call schedule is not found.
     */
    @GetMapping("/{id}")
    public CallSchedule getCallScheduleById(@PathVariable Long id) {
        log.info("Fetching call schedule with ID: {}", id);
        // The service method already throws an exception if not found.
        return callScheduleService.findById(id);
    }

    /**
     * Delete a call schedule by ID.
     *
     * @param id the ID of the call schedule to be deleted.
     * @throws ResourceNotFoundException if the call schedule is not found.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCallSchedule(@PathVariable Long id) {
        log.info("Deleting call schedule with ID: {}", id);
        if (!callScheduleService.existsById(id)) {
            log.warn("Call schedule with ID: {} not found", id);
            throw new ResourceNotFoundException("Call schedule not found with ID: " + id);
        }
        callScheduleService.delete(id);
        log.info("Call schedule with ID: {} successfully deleted", id);
    }

    /**
     * Get call schedules by call type.
     *
     * @param callType the type of the call schedules to retrieve.
     * @return list of call schedules matching the given type.
     */
    @GetMapping("/type/{callType}")
    public List<CallSchedule> getCallSchedulesByType(@PathVariable CallType callType) {
        log.info("Fetching call schedules by type: {}", callType);
        return callScheduleService.findByCallType(callType);
    }

    /**
     * Handle validation errors (constraint violations).
     *
     * @param ex the exception containing validation errors.
     * @return a response entity with error messages.
     */
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationExceptions(jakarta.validation.ConstraintViolationException ex) {
        // Collect error messages from constraint violations.
        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.toList());
        log.warn("Validation error: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle method argument validation errors.
     *
     * @param ex the exception containing argument validation errors.
     * @return a response entity with error messages.
     */
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        log.warn("Method argument validation error: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
