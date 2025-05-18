package com.api.hub.ai.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the complete history of actions taken by an AI agent,
 * organized by goals and tasks.
 * <p>
 * This POJO maintains a list of {@link History} entries, where each entry
 * corresponds to a specific {@link Goal} and {@link Task} combination,
 * along with the list of {@link Action} instances that the agent executed for that task.
 * </p>
 * 
 * <p>
 * The {@code AgentHistory} class is essential for tracking the agent's progress,
 * auditing decisions, and generating metrics based on the actions performed during
 * task execution within the AI-Agent Framework.
 * </p>
 * 
 * <p><b>Usage:</b></p>
 * <ul>
 *   <li>Store and retrieve all actions related to a particular goal and task.</li>
 *   <li>Provide a detailed timeline of the agent's operational behavior.</li>
 * </ul>
 * 
 * @see History
 * @see Goal
 * @see Task
 * @see Action
 * @since 1.0
 */
@Data
public class AgentHistory {

    /**
     * List holding the action history grouped by goal and task.
     */
    List<History> history = new ArrayList<History>();

    /**
     * Represents the action history for a specific goal and task.
     * <p>
     * Contains the goal, the associated task, and a list of actions the agent took.
     * </p>
     */
    @Data
    @AllArgsConstructor
    public class History {
        /**
         * The goal associated with this history entry.
         */
        private Goal goal;

        /**
         * The task associated with this history entry.
         */
        private Task task;

        /**
         * The list of actions performed by the agent for the given goal and task.
         */
        private List<Action> actions;
    }
}