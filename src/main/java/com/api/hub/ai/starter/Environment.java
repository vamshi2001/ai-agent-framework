package com.api.hub.ai.starter;

import com.api.hub.ai.constants.MarkerConstants;
import com.api.hub.ai.handler.impl.EnvironmentState;
import com.api.hub.ai.handler.InputOutputHandler;

import lombok.NonNull;

/**
 * The {@code Environment} interface defines the central contract for the AI-agent framework.
 * <p>
 * It serves as the main entry point through which agents interact with their operating context,
 * including:
 * <ul>
 *   <li>The current state of the environment (conversation, system, or workflow context)</li>
 *   <li>User inputs (requests or responses)</li>
 *   <li>An {@link InputOutputHandler} for communication between the agent and the user</li>
 * </ul>
 * <p>
 * Implementations of this interface are expected to coordinate agent behavior by interpreting
 * the environment state and user input, and responding accordingly.
 *
 * <p>Typical agent usage:
 * <pre>{@code
 * Environment env = new MyAgentEnvironment();
 * boolean continueInteraction = env.process(currentState, userInput, ioHandler);
 * }</pre>
 *
 * <p>This interface also extends {@link MarkerConstants} to support structured logging
 * using predefined SLF4J markers for traceability and debugging.
 */
public interface Environment extends MarkerConstants {

    /**
     * The logger instance for logging environment-level operations.
     * Logs can be tagged with markers from {@link MarkerConstants} for context-specific tracing.
     */
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Environment.class);

    /**
     * Processes the current interaction in the agent environment.
     * <p>
     * This method receives the current {@link EnvironmentState}, user input, and an input-output handler,
     * and performs the necessary logic to update the environment or respond to the user.
     *
     * @param env The current environment state, representing the conversation/session/system context.
     * @param userResponse The user input or message to be processed.
     * @param handler The input-output handler the agent can use to communicate with the user.
     * @return {@code true} if the interaction should continue (e.g., keep the session alive), {@code false} to terminate.
     */
    boolean process(EnvironmentState env, String userResponse, InputOutputHandler handler);

    /**
     * Retrieves or initializes an {@link EnvironmentState} instance based on the given environment name and identifier.
     * <p>
     * This allows the framework to support multiple environments (e.g., multi-session, multi-agent)
     * and restore or maintain state between interactions.
     *
     * @param envName The name or type of the environment (e.g., "chat", "order-processing").
     * @param id An optional unique identifier to distinguish different sessions or agents (nullable).
     * @return The environment state associated with the provided parameters.
     */
    EnvironmentState getEnvironment(@NonNull String envName, String id);
}