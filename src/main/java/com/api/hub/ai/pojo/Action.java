package com.api.hub.ai.pojo;

import lombok.Data;

/**
 * Represents an action performed by an AI agent on a specific task within the AI-Agent Framework.
 * <p>
 * Each AI agent is required to create and populate an instance of this class to capture
 * details about the action taken. This information is stored in the agent's state and
 * is critical for tracking progress, auditing, and metrics collection.
 * </p>
 * 
 * <p><b>Fields:</b></p>
 * <ul>
 *   <li>{@code description} - A human-readable summary of the action performed by the agent.</li>
 *   <li>{@code task} - The {@link Task} object representing the task on which this action was taken.</li>
 *   <li>{@code executer} - Identifier or name of the agent or system component that executed the action.</li>
 * </ul>
 * 
 * This POJO supports transparent reporting and monitoring of agent activities and helps
 * maintain a comprehensive history of actions for evaluation and debugging purposes.
 * 
 * @see Task
 * @since 1.0
 */
@Data
public class Action {

    /**
     * Description of the action taken by the AI agent.
     */
    private String description;

    /**
     * The task associated with this action.
     */
    private Task task;

    /**
     * Identifier of the entity (agent or component) that executed the action.
     */
    private String executer;
}
