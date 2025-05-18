package com.api.hub.ai.pojo;

import java.util.Date;

import com.api.hub.ai.constants.StatusValues;

import lombok.Data;
import lombok.NonNull;

/**
 * Represents a Task within the AI-Agent Framework.
 * <p>
 * This POJO encapsulates the details and lifecycle of a task assigned or created within the system.
 * Each Task holds metadata including its name, the actual task payload, description, creator agent,
 * the Goal it is associated with, and its current status.
 * </p>
 * 
 * <p><b>Task Lifecycle:</b></p>
 * <ul>
 *   <li>The initial task (typically corresponding to user input) is created by the Environment.</li>
 *   <li>Subsequent tasks are created and closed by agents during the processing workflow.</li>
 *   <li>Calling {@link #close()} marks the task as completed, sets the end time, and removes it from its associated goal.</li>
 * </ul>
 * 
 * <p><b>Fields:</b></p>
 * <ul>
 *   <li><b>name:</b> Identifier or name of the task.</li>
 *   <li><b>task:</b> The payload or object representing the actual task details.</li>
 *   <li><b>description:</b> Optional textual description of the task.</li>
 *   <li><b>createdBy:</b> Name of the agent or environment that created the task.</li>
 *   <li><b>currentGoal:</b> The Goal instance that this task belongs to.</li>
 *   <li><b>taskStatus:</b> Status object tracking the task's lifecycle state.</li>
 * </ul>
 * 
 * @see Goal
 * @see Status
 * 
 * @since 1.0
 */
@Data
public class Task {

    private String name;
    private Object task;
    private String description;
    private String createdBy;
    private Goal currentGoal;
    private Status taskStatus = new Status();

    /**
     * Constructs a new Task instance.
     * 
     * @param name        The name or identifier of the task. Must not be null.
     * @param task        The task payload or data. Must not be null.
     * @param agentName   The name of the agent or environment creating this task. Must not be null.
     * @param currentGoal The Goal to which this task is assigned. Must not be null.
     */
    public Task(@NonNull String name, @NonNull Object task, @NonNull String agentName, @NonNull Goal currentGoal) {
        this.name = name;
        this.task = task;
        this.createdBy = agentName;
        this.currentGoal = currentGoal;
    }

    /**
     * Closes this task by marking its status as completed, setting the end time,
     * and removing it from the associated Goal.
     */
    public void close() {
        currentGoal.removeTask(this);
        taskStatus.setEndTime(new Date());
        taskStatus.setStatus(StatusValues.COMPLETED);
    }
}