package lt.ca.javau11.entity;

/**
 * Enumeration representing types of school bells.
 * Used to differentiate between different bell signals in the schedule.
 */
public enum CallType {
    /**
     * Signal for lesson start
     */
    LESSON_START,    

    /**
     * Signal for break time
     */
    BREAK_START,
   

    /**
     * Preliminary signal before lesson start (warning bell)
     */
    PRELIMINARY_CALL; 
}
