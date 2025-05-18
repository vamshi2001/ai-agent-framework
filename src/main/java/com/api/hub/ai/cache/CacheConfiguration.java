package com.api.hub.ai.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.api.hub.ai.cache.impl.SimpleCacheHandler;
import com.api.hub.ai.handler.impl.EnvironmentState;

/**
 * Configuration class for cache initialization in the
 * Enterprise AI Agent Execution & Orchestration Platform.
 *
 * <p>
 * This configuration dynamically registers cache beans based on property-driven conditions.
 * Specifically, it initializes a default in-memory cache implementation for
 * {@link EnvironmentState} using {@link SimpleCacheHandler} when the
 * property {@code cache.env.simplecache=true} is set.
 * </p>
 *
 * <p>
 * Caching is critical in the platform to maintain low-latency access to
 * environment states and session-specific data during LLM/Non-LLM agent
 * orchestration workflows.
 * </p>
 *
 * <p>
 * Each chat session in the platform creates a new {@link EnvironmentState}
 * instance, uniquely identified by a chat/session ID, which is cached to
 * optimize agent context switching and state management.
 * </p>
 *
 * <p>
 * Scheduled tasks enabled via {@code @EnableScheduling} are utilized internally
 * for refreshing and syncing cache content with potential external systems (if
 * implemented via {@link CacheOperations}).
 * </p>
 *
 * @see Cache
 * @see CacheOperations
 * @see EnvironmentState
 * @see SimpleCacheHandler
 */
@Configuration
@EnableScheduling
public class CacheConfiguration {

    /**
     * Registers a default in-memory {@code Cache} bean for storing {@link EnvironmentState}
     * instances, typically used for managing the lifecycle and context of each
     * agent chat session.
     *
     * <p>
     * This bean is only activated when the property {@code cache.env.simplecache}
     * is explicitly set to {@code true}, allowing developers to switch between
     * in-memory and more advanced cache providers (e.g., Redis, NoSQL) without code changes.
     * </p>
     *
     * @return a {@code SimpleCacheHandler} instance for managing {@code EnvironmentState}
     */
    @Bean("EnvironmentStateCache")
    @ConditionalOnProperty(name = "cache.env.simplecache", havingValue = "true")
    public Cache<String, EnvironmentState> getDefaultEnvCache() {
        return new SimpleCacheHandler<String, EnvironmentState>();
    }
}
