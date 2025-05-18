package com.api.hub.ai.pojo;

import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;

import com.api.hub.ai.cache.Cache;
import com.api.hub.ai.constants.StatusValues;

import lombok.Data;

/**
 * Represents a Goal within the AI-Agent Framework.
 * <p>
 * This POJO models a Goal that the AI agent or environment works towards achieving.
 * Each Goal maintains a collection of Tasks, a status indicating its lifecycle state,
 * a flag to mark if it is a default goal, and a cache of variables relevant to the goal's context.
 * </p>
 * 
 * <p><b>Responsibilities and Behavior:</b></p>
 * <ul>
 *   <li>Holds a queue of {@link Task} instances representing work to be done towards this goal.</li>
 *   <li>Supports retrieval of the current active task via {@link #getTask()} (peek of the task queue).</li>
 *   <li>Allows adding tasks either at the front or end of the task queue via {@link #addTask(Task, boolean)}.</li>
 *   <li>Handles task removal with {@link #removeTask(Task)}, which also calls {@link Task#close()} on the task.</li>
 *   <li>Manages the goal's status and can be marked completed via {@link #close()}.</li>
 *   <li>Maintains a {@link Cache} instance to store goal-specific variables, facilitating stateful processing.</li>
 * </ul>
 * 
 * <p><b>Field Summary:</b></p>
 * <ul>
 *   <li><b>goal:</b> A string identifier or description for the goal.</li>
 *   <li><b>defaultGoal:</b> Flag indicating if this is the default goal for the agent or environment.</li>
 *   <li><b>tasks:</b> A deque holding the tasks associated with this goal, ordered for processing.</li>
 *   <li><b>status:</b> Tracks the current lifecycle status of the goal.</li>
 *   <li><b>variables:</b> A cache for storing key-value pairs relevant to this goal's execution context.</li>
 * </ul>
 * 
 * @see Task
 * @see Status
 * @see Cache
 * 
 * @since 1.0
 */
@Data
public class Goal {
    
    private String goal;
    private boolean defaultGoal;
    private Deque<Task> tasks = new LinkedList<Task>();
    private Status status = new Status();
    
    private Cache<String, Object> variables;
    
    /**
     * Retrieves the current active task for this goal without removing it.
     * 
     * @return The task at the front of the task queue, or null if no tasks exist.
     */
    public Task getTask() {
        return tasks.peek();
    }
    
    /**
     * Removes the specified task from this goal's task queue.
     * This also closes the task by updating its status.
     * 
     * @param task The task to be removed.
     * @return true if the task was successfully removed; false otherwise.
     */
    public boolean removeTask(Task task) {
        task.close();
        return tasks.remove(task);
    }
    
    /**
     * Marks this goal as completed by setting the end time and status accordingly.
     */
    public void close() {
        status.setEndTime(new Date());
        status.setStatus(StatusValues.COMPLETED);
    }
    
    /**
     * Adds a task to this goal's task queue.
     * 
     * @param task      The task to add.
     * @param addBefore If true, adds the task at the front (head) of the queue;
     *                  otherwise, adds it at the end (tail).
     */
    public void addTask(Task task, boolean addBefore) {
        if (addBefore) {
            tasks.addFirst(task);
        } else {
            tasks.addLast(task);
        }
    }
}
