package com.api.hub.configuration.ai;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.api.hub.ai.handler.impl.AgentDefination;
import com.api.hub.ai.starter.Agent;
import com.api.hub.ai.starter.AiAgent;

/**
 * Spring {@link BeanPostProcessor} implementation responsible for registering AI agents
 * within the AI-Agent framework.
 * <p>
 * When a Spring bean implementing the {@link AiAgent} marker interface is initialized,
 * this class scans the bean's methods for the {@link Agent} annotation.
 * For each annotated method, it creates and configures an {@link AgentDefination} instance,
 * which encapsulates metadata and behavior information about the agent method.
 * These agent definitions are then organized into named pools for logical grouping and later retrieval.
 * </p>
 * <p>
 * This enables dynamic registration and configuration of AI agents based on annotated methods,
 * facilitating modular agent development and management within the framework.
 * </p>
 *
 * <h3>Key Responsibilities:</h3>
 * <ul>
 *   <li>Detect Spring beans implementing the {@link AiAgent} interface.</li>
 *   <li>Scan such beans for methods annotated with {@link Agent}.</li>
 *   <li>For each annotated method, create an {@link AgentDefination} containing:</li>
 *   <ul>
 *       <li>Agent name (combining bean name and method name)</li>
 *       <li>Description, goals, and method reference from the annotation</li>
 *       <li>Reference to the bean instance as the agent handler</li>
 *   </ul>
 *   <li>Group agent definitions into pools as defined by the {@code agentPools} attribute of the annotation.</li>
 *   <li>Provide a static method to retrieve agents by pool name.</li>
 * </ul>
 *
 * @see AiAgent
 * @see Agent
 * @see AgentDefination
 * @see BeanPostProcessor
 */
@Component
public class AgentRegister implements BeanPostProcessor {

    private static final Map<String, List<AgentDefination>> agentMap = new HashMap<>();

    /**
     * Post-processes beans after initialization to detect AI agents and register their agent methods.
     * <p>
     * If the given bean implements {@link AiAgent}, this method scans all its declared methods
     * for the {@link Agent} annotation. For each annotated method, it creates an {@link AgentDefination}
     * instance and configures it based on annotation attributes and method references.
     * The agent definitions are then added to one or more pools as specified.
     * </p>
     *
     * @param bean the bean instance created by Spring container
     * @param beanName the name of the bean in the Spring context
     * @return the original bean instance (unmodified)
     * @throws BeansException in case of any processing errors
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        
        if(bean instanceof AiAgent) {
            Class<?> clazz = bean.getClass();

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Agent.class)) {
                    Agent annotation = method.getAnnotation(Agent.class);

                    AgentDefination def = new AgentDefination();
                    def.setName(beanName + "-" + method.getName());
                    def.setDescription(annotation.description());
                    def.setGoalNames(Arrays.asList(annotation.goals()));
                    def.setAgentHandler(bean);
                    def.setMethodToInvoke(method);

                    String[] agentPools = annotation.agentPools();
                    for(String agentPool : agentPools) {
                        List<AgentDefination> defList = agentMap.getOrDefault(agentPool, new ArrayList<>());
                        defList.add(def);
                        agentMap.put(agentPool, defList);
                    }
                }
            }
        }

        return bean;
    }

    /**
     * Retrieves the list of {@link AgentDefination} instances registered under a given pool name.
     *
     * @param poolName the name of the agent pool
     * @return a list of agent definitions associated with the pool, or {@code null} if no agents are registered
     *         under that pool
     */
    public static List<AgentDefination> getAgents(String poolName) {
        return agentMap.get(poolName);
    }
}
