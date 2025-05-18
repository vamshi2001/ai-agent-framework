package com.api.hub.ai.constants;

/**
 * Enum representing the lifecycle status values for tasks and goals within the AI-Agent Framework.
 * <p>
 * These statuses are used by AI agents and the Environment component to track and manage the state
 * of tasks and goals during execution.
 * </p>
 * 
 * <ul>
 *   <li>{@link #NEW}: Indicates a newly created task or goal that has not yet started processing.</li>
 *   <li>{@link #IN_PROGRESS}: Indicates a task or goal that is currently being worked on or executed.</li>
 *   <li>{@link #COMPLETED}: Indicates a task or goal that has been successfully finished.</li>
 * </ul>
 * 
 * <p>Each status value is associated with a unique integer code for easy reference and persistence.</p>
 * 
 * Usage of these constants ensures consistent status checks and updates across the AI agent system.
 * 

 * @since 1.0
 */
public enum StatusValues {
    
    /** Newly created task or goal, not yet started. */
    NEW(1),
    
    /** Task or goal currently in progress. */
    IN_PROGRESS(2),
    
    /** Task or goal that has been completed successfully. */
    COMPLETED(3);

    /** Numeric code representing the status. */
    private final int code;

    /**
     * Constructs a StatusValues enum constant with the specified integer code.
     * 
     * @param code the integer code associated with the status
     */
    StatusValues(int code) {
        this.code = code;
    }

    /**
     * Returns the integer code associated with this status.
     * 
     * @return the status code
     */
    public int getCode() {
        return code;
    }
}