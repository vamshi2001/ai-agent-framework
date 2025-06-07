package com.api.hub.ai.handler.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.api.hub.ai.cache.Cache;
import com.api.hub.ai.pojo.State;
import com.api.hub.exception.ApiHubException;
import com.api.hub.exception.InputException;
import com.api.hub.exception.InternalServerException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * {@code AgentDefination} represents the definition and metadata of an AI agent
 * within the AI-Agent Framework. It encapsulates all information required to dynamically
 * invoke an agent's method using Java reflection.
 *
 * <p>This class enables flexible execution of agent logic by decoupling the agent's
 * behavior from the main control flow. The framework can invoke any agent by calling
 * {@link #invokeAgent(State)} with the appropriate execution state.</p>
 *
 * <p><b>Key Components:</b></p>
 * <ul>
 *   <li><b>name</b>: Unique identifier or name of the agent.</li>
 *   <li><b>methodToInvoke</b>: The method that will be executed when this agent is invoked.</li>
 *   <li><b>agentHandler</b>: The actual object instance that contains the method logic.</li>
 *   <li><b>description</b>: Human-readable description of the agent's purpose.</li>
 *   <li><b>variables</b>: A scoped cache of variables specific to this agent.</li>
 *   <li><b>goalNames</b>: List of goal names this agent is associated with.</li>
 * </ul>
 *
 * <p><b>Usage:</b> The environment will create instances of this class
 * and call {@link #invokeAgent(State)} to execute agent logic with the provided state.</p>
 *
 * <p>This design supports dynamic agent registration and execution, allowing the framework
 * to be easily extended without hardcoded logic for each agent.</p>
 *

 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class AgentDefination {

    /** Unique name of the agent */
    private String name;

    /** The method to be executed when this agent is invoked */
    private Method methodToInvoke;

    /** The object instance that contains the logic to execute */
    private Object agentHandler;

    /** Human-readable description of the agent */
    private String description;

    /** Variable cache local to this agent’s execution context */
    private Cache<String, Object> variables;

    /** List of goal names that this agent can act upon */
    private List<String> goalNames;

    /**
     * Invokes the defined agent method using Java reflection.
     * <p>The method must accept a single {@link State} parameter and return a boolean
     * indicating success or failure.</p>
     *
     * @param state The complete execution state provided by the environment.
     * @return {@code true} if the agent completed successfully, {@code false} otherwise.
     * @throws ApiHubException If an error occurs during method invocation.
     */
    public boolean invokeAgent(State state) throws ApiHubException {
        try {
            methodToInvoke.setAccessible(true);
            return (boolean) methodToInvoke.invoke(agentHandler, new Object[] { state , variables});
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new InputException("1001-ai-hub", "unable to invoke agent " + e.getMessage(), "");
        } catch (Exception e) {
            throw new InternalServerException("8009-ai-hub", "unexpected error occurred while invoking agent " + e.getMessage(), "");
        }
    }
}