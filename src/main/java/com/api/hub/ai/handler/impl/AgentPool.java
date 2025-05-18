package com.api.hub.ai.handler.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.api.hub.ai.pojo.Task;
import com.api.hub.configuration.ai.AgentRegister;
import com.api.hub.exception.ApiHubException;
import com.api.hub.exception.InputException;

/**
 * Abstract class representing a pool of AI agents registered under a specific category or type.
 * <p>
 * This class is part of the AI-Agent Framework, responsible for managing the registration, mapping,
 * and invocation readiness of AI agents. It initializes agent definitions at application startup
 * and maps them to specific goals they are capable of handling.
 * </p>
 *
 * <p>
 * Subclasses must implement the {@link #getMatchingAgent(Task)} method to provide logic for
 * selecting the most appropriate agent for a given task.
 * </p>
 *
 * <p><b>Usage:</b> Extend this class and define custom agent-matching logic based on your use case.</p>
 *
 * @author 
 */
public abstract class AgentPool {

    /**
     * Name identifying this pool of agents. Must be unique and not null.
     */
    protected String name;

    /**
     * List of supported goal names this agent pool is designed to handle.
     */
    protected List<String> goals = new ArrayList<>();

    /**
     * Map of agent names to their corresponding {@link AgentDefination} objects.
     */
    protected Map<String, AgentDefination> agentsMap = new HashMap<>();

    /**
     * Map of goal names to list of agent names that are capable of handling them.
     */
    protected Map<String, List<String>> agentGoalMap = new HashMap<>();

    /**
     * Registers a new agent into the pool.
     *
     * @param agent The agent definition to be registered.
     */
    public void registerAgent(AgentDefination agent) {
        agentsMap.put(agent.getName(), agent);
    }

    /**
     * Refreshes the mapping between goals and agent names based on each agent's declared capabilities.
     * Agents that support all goals ("*") will be mapped to all available goals in the pool.
     */
    public void refresh() {
        for (Map.Entry<String, AgentDefination> entry : agentsMap.entrySet()) {
            String agentName = entry.getValue().getName();
            List<String> goalNames = entry.getValue().getGoalNames();

            if (goalNames.size() == 1 && goalNames.get(0).equals("*")) {
                goals.forEach(g -> {
                    agentGoalMap.getOrDefault(g, new ArrayList<>()).add(agentName);
                });
            } else {
                goalNames.forEach(g -> {
                    agentGoalMap.getOrDefault(g, new ArrayList<>()).add(agentName);
                });
            }
        }
    }

    /**
     * Initializes the agent pool after the application context is fully loaded.
     * <p>
     * This method loads all agents registered under this pool's name from the {@link AgentRegister}
     * and registers them into the pool.
     * </p>
     *
     * @throws ApiHubException if the name is not set or if no agents are registered under the given name.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void configureAgents() throws ApiHubException {
        if (name == null || name.isBlank()) {
            throw new InputException("1001-ai-hub", "name is null", "");
        }

        List<AgentDefination> defList = AgentRegister.getAgents(name);
        if (defList == null || defList.isEmpty()) {
            throw new InputException("1001-ai-hub", "invalid name " + name, "");
        }

        for (AgentDefination def : defList) {
            registerAgent(def);
        }
    }

    /**
     * Abstract method to retrieve the agent definition that matches the given task.
     * <p>
     * Subclasses must implement logic to determine which registered agent is best suited
     * for handling the specified task.
     * </p>
     *
     * @param task The task for which a matching agent needs to be found.
     * @return The matching {@link AgentDefination}, or {@code null} if no match is found.
     */
    public abstract AgentDefination getMatchingAgent(Task task);
}