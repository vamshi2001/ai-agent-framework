package com.api.hub.ai.starter;

/**
 * Marker interface to designate a class as an AI Agent within the AI-Agent framework.
 * <p>
 * Classes implementing this interface represent autonomous AI agents that can be
 * discovered and configured by the {@code AgentRegister} class during application
 * startup.
 * </p>
 * <p>
 * Implementing this interface enables the framework to automatically process
 * agent classes annotated with {@link com.api.hub.ai.starter.Agent @Agent} annotation,
 * which is used by {@code AgentRegister} to build and manage {@link com.api.hub.ai.handler.impl.AgentDefination AgentDefination}
 * metadata for runtime execution.
 * </p>
 * <p>
 * This interface does not define any methods and serves purely as a marker
 * for identification and configuration purposes within the framework.
 * </p>
 *
 * @see com.api.hub.ai.starter.Agent
 * @see com.api.hub.configuration.ai.AgentRegister
 * @see com.api.hub.ai.handler.impl.AgentDefination
 */
public interface AiAgent {
    // Marker interface - no methods to implement
}
