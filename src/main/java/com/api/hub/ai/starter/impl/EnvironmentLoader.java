package com.api.hub.ai.starter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import com.api.hub.ai.cache.Cache;
import com.api.hub.ai.handler.impl.EnvironmentState;
import com.api.hub.ai.pojo.AgentHistory;
import com.api.hub.ai.pojo.Goal;
import com.api.hub.ai.starter.Environment;
import com.api.hub.exception.ApiHubException;
import com.api.hub.exception.ConfigurationException;

import lombok.NonNull;

/**
 * {@code EnvironmentLoader} is the primary abstract implementation of the {@link Environment} interface
 * within the AI-agent framework. It is responsible for:
 * <ul>
 *     <li>Retrieving or creating new {@link EnvironmentState} instances based on environment name and ID</li>
 *     <li>Loading and validating required configuration from Spring's {@link org.springframework.core.env.Environment}</li>
 *     <li>Injecting and managing various {@link Cache} components for goals, history, variables, etc.</li>
 *     <li>Constructing a fully-initialized {@link EnvironmentState} with all operational components</li>
 * </ul>
 *
 * <p>This class enables agent-based applications to be configured dynamically based on property files
 * (e.g., `application.properties` or YAML) for flexible and reusable environment definitions.
 *
 * <p><b>Note:</b> As an abstract class, this must be extended to implement the {@link #process(EnvironmentState, String, com.api.hub.ai.handler.InputOutputHandler)} method.
 */
public abstract class EnvironmentLoader implements Environment {

    /**
     * Spring application context used to dynamically retrieve beans and cache handlers by name.
     */
    @Autowired
    protected ApplicationContext context;

    /**
     * Spring environment used to access external configuration properties.
     */
    @Autowired
    protected org.springframework.core.env.Environment env;

    /**
     * Cache used to store and retrieve active {@link EnvironmentState} instances by unique identifier.
     */
    @Autowired
    @Qualifier("EnvironmentStateCache")
    protected Cache<String, EnvironmentState> envStateCache;

    /**
     * Retrieves an existing {@link EnvironmentState} based on the given environment name and ID.
     * <p>
     * If the ID is missing or the state is not found in the cache, a new state is created.
     *
     * @param envName the environment name (e.g., "chat", "healthcare")
     * @param id an optional unique identifier for the environment state
     * @return an initialized and cached {@link EnvironmentState}
     */
    @Override
    public EnvironmentState getEnvironment(@NonNull String envName, String id) {
        EnvironmentState state = null;
        if (id == null || id.isBlank()) {
            state = envStateCache.get(id);
        }
        if (state == null) {
            state = createEnvironment(envName);
        }
        return state;
    }

    /**
     * Creates a new {@link EnvironmentState} by:
     * <ul>
     *   <li>Loading goals from configuration</li>
     *   <li>Resolving cache handlers for agent history, environment history, and variables</li>
     *   <li>Instantiating the state with an executor and communication handler</li>
     * </ul>
     *
     * @param envName the name of the environment
     * @return a fully initialized {@link EnvironmentState}, or {@code null} if creation fails
     */
    private EnvironmentState createEnvironment(@NonNull String envName) {
        try {
            String name = envName;
            List<Goal> goals = new ArrayList<>();
            String propToSearch = "ai." + envName + ".goal.list";
            String goalsList = env.getProperty(propToSearch);
            if (goalsList == null)
                throw new ConfigurationException("6003-ai-hub", "Required goals for environment " + envName + ", missing value for " + propToSearch, "");

            String[] array = goalsList.split(",");
            for (String goalName : array) {
                Goal goal = new Goal();
                goal.setGoal(goalName);

                propToSearch = "ai." + envName + "." + goalName + ".isDefault";
                String defaultGoal = env.getProperty(propToSearch);
                boolean isDefaultGoal = false;
                if (defaultGoal != null) {
                    try {
                        isDefaultGoal = Boolean.parseBoolean(defaultGoal);
                    } catch (Exception e) {
                        throw new ConfigurationException("6003-ai-hub", "Expected boolean value for " + propToSearch + ", invalid value provided", "");
                    }
                }
                goal.setDefaultGoal(isDefaultGoal);
                goals.add(goal);
            }

            // Retrieve agent-level history cache
            propToSearch = "ai." + envName + ".cache.agentHistory";
            String cacheClassName = env.getProperty(propToSearch);
            if (cacheClassName == null)
                throw new ConfigurationException("6003-ai-hub", "Missing agent history cache for " + envName + ", property: " + propToSearch, "");
            @SuppressWarnings("unchecked")
            Cache<String, AgentHistory> agentLevelHistory = (Cache<String, AgentHistory>) context.getBean(cacheClassName);

            // Retrieve environment-level history cache
            propToSearch = "ai." + envName + ".cache.envLevelHistory";
            cacheClassName = env.getProperty(propToSearch);
            if (cacheClassName == null)
                throw new ConfigurationException("6003-ai-hub", "Missing environment history cache for " + envName + ", property: " + propToSearch, "");
            @SuppressWarnings("unchecked")
            Cache<String, String> envLevelHistory = (Cache<String, String>) context.getBean(cacheClassName);

            // Retrieve environment variables cache
            propToSearch = "ai." + envName + ".cache.envVariables";
            cacheClassName = env.getProperty(propToSearch);
            if (cacheClassName == null)
                throw new ConfigurationException("6003-ai-hub", "Missing environment variables cache for " + envName + ", property: " + propToSearch, "");
            @SuppressWarnings("unchecked")
            Cache<String, Object> variables = (Cache<String, Object>) context.getBean(cacheClassName);

            // Load agent pool name
            propToSearch = "ai." + envName + ".agentPool";
            String agentPoolName = env.getProperty(propToSearch);
            if (agentPoolName == null)
                throw new ConfigurationException("6003-ai-hub", "Missing agent pool name for " + envName + ", property: " + propToSearch, "");

            // Load communication handler name
            propToSearch = "ai." + envName + ".inOutHandler";
            String inOutHandlerName = env.getProperty(propToSearch);
            if (inOutHandlerName == null)
                throw new ConfigurationException("6003-ai-hub", "Missing input-output handler for " + envName + ", property: " + propToSearch, "");

            // Construct the EnvironmentState
            ExecutorService executor = Executors.newCachedThreadPool();
            Environment environment = this;
            EnvironmentState state = new EnvironmentState(
                    name, goals, agentLevelHistory, envLevelHistory, variables,
                    agentPoolName, environment, executor, inOutHandlerName, context);

            envStateCache.save(state.getId(), state);
            return state;
        } catch (ApiHubException e) {
            log.error(ERROR, e.toString());
        } catch (Exception e) {
            log.error(ERROR, "Unable to create environment. Please check configurations for Environment - " + envName + ". " + e.getMessage());
        }

        return null;
    }
}