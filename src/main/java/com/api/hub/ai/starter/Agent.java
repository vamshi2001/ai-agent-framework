package com.api.hub.ai.starter;

import java.lang.annotation.*;

/**
 * Annotation to mark and configure methods as AI agent tasks within the AI-Agent framework.
 * <p>
 * Methods annotated with {@code @Agent} are recognized by the framework and
 * are used to define agent behavior through metadata encapsulated in
 * {@link com.api.hub.ai.handler.impl.AgentDefination AgentDefination}.
 * </p>
 * <p>
 * This annotation enables specification of:
 * </p>
 * <ul>
 *   <li><b>variables</b>: Specifies the variable handler class or context used by the agent method, defaulting to {@code "SimpleCacheHandler"}.</li>
 *   <li><b>description</b>: Provides a human-readable description of the agent method’s purpose or behavior.</li>
 *   <li><b>goals</b>: Defines a list of goals or objectives the agent method aims to accomplish.</li>
 *   <li><b>agentPools</b>: Specifies one or more pools or groups the agent belongs to for logical categorization or execution management.</li>
 * </ul>
 * <p>
 * The annotated method is expected to accept a single parameter of type
 * {@link com.api.hub.ai.pojo.State State}, which encapsulates the current task,
 * environment state, input-output handler, and history relevant to the agent’s
 * execution context.
 * </p>
 *
 * @see com.api.hub.ai.pojo.State
 * @see com.api.hub.ai.handler.impl.AgentDefination
 * @see com.api.hub.ai.starter.AiAgent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Agent {
    
	/**
	 * Specifies the cache bean name specific to that particular agent.
	 * This should correspond to a cache bean configured in the application context,
	 * used to manage the agent’s variable state.
	 *
	 * @return the cache bean name
	 */
	String variables() default "SimpleCacheHandler";

    /**
     * A brief description of what the agent method does or its role.
     *
     * @return description of the agent method
     */
    String description() default "";

    /**
     * Goals or objectives this agent method is intended to achieve.
     *
     * @return array of goal strings
     */
    String[] goals() default {};

    /**
     * Pools or groups to which this agent belongs for categorization or scheduling.
     *
     * @return array of agent pool names
     */
    String[] agentPools() default {};
}