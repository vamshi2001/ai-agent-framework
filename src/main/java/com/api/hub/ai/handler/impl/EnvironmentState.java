package com.api.hub.ai.handler.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.context.ApplicationContext;

import com.api.hub.ai.cache.Cache;
import com.api.hub.ai.handler.InputOutputHandler;
import com.api.hub.ai.pojo.AgentHistory;
import com.api.hub.ai.pojo.Goal;
import com.api.hub.ai.starter.Environment;
import com.api.hub.logging.LoggingData;
import com.api.hub.logging.LoggingDataHolder;

import lombok.Data;
import lombok.NonNull;

/**
 * Represents the runtime state of an AI agent's execution environment.
 * <p>
 * This class holds and manages various components critical to the AI-agent framework's 
 * goal-oriented processing, such as active goals, agent-specific and environment-level histories,
 * shared variables, and async execution context.
 * </p>
 * 
 * <p>
 * Each instance of {@code EnvironmentState} encapsulates the full state required for a single 
 * chat or interaction session with the user. It should be instantiated fresh for every new
 * conversation or chat session.
 * </p>
 *
 * <p><strong>Session-based Design Note:</strong></p>
 * <ul>
 *   <li>Each new user interaction (chat thread/session) should create a new {@code EnvironmentState} instance.</li>
 *   <li>The {@code id} field in this class serves as the unique session or chat ID, similar to how
 *       traditional chat systems track conversations per user.</li>
 *   <li>This allows agents to maintain isolated memory and goal contexts per session.</li>
 * </ul>
 * 
 * <p>
 * It also handles the orchestration of input-output processing through the associated
 * {@link InputOutputHandler}, allowing asynchronous handling of messages via the provided {@link ExecutorService}.
 * </p>
 * 
 * <p>
 * Each environment instance is self-contained, enabling scalable, concurrent, and multi-agent processing
 * tailored to the specific goals and histories of an individual conversation or agent session.
 * </p>
 * 
AI Framework
 */
@Data
public class EnvironmentState implements AutoCloseable{

	public EnvironmentState(@NonNull String name, @NonNull List<Goal> goals,
			@NonNull Cache<String, AgentHistory> agentLevelHistory, @NonNull Cache<String, String> envLevelHistory,
			@NonNull Cache<String, Object> variables, @NonNull String agentpool, @NonNull Environment env, 
			ExecutorService executer, String inOutHandlerName, ApplicationContext context) {
		super();
		this.name = name;
		this.goals = goals;
		this.
		agentLevelHistory = agentLevelHistory;
		this.envLevelHistory = envLevelHistory;
		this.variables = variables;
		this.agentpool = agentpool;
		this.env = env;
		this.executer = executer;
		this.inOutHandlerName = inOutHandlerName;
		this.context = context;
	}
    /**
     * Unique identifier for this environment state.
     */
    private String id = UUID.randomUUID().toString();

    /**
     * Name of the environment instance. Used for agent pool and handler resolution.
     */
    @NonNull
    private String name;

    /**
     * List of all predefined goals in this environment.
     */
    @NonNull
    private List<Goal> goals;

    /**
     * Queue of currently active or pending goals to be executed by agents.
     */
    private final Queue<Goal> goalQueue = new LinkedList<>();

    /**
     * Cached default goal used when no other goal is active.
     */
    private Goal defaultGoal;

    /**
     * Agent-level interaction history. Used to store memory per agent for each goal.
     */
    @NonNull
    private Cache<String, AgentHistory> agentLevelHistory;

    /**
     * Environment-level goal history. Used to persist goal progression and transitions.
     */
    @NonNull
    private Cache<String, String> envLevelHistory;

    /**
     * Shared key-value store to allow communication between agents via variables.
     */
    @NonNull
    private Cache<String, Object> variables;

    /**
     * Name of the agent pool used to retrieve matching agent definitions for each goal.
     */
    @NonNull
    private String agentpool;

    /**
     * The execution environment responsible for processing messages and managing goal transitions.
     */
    @NonNull
    private Environment env;

    /**
     * Thread executor used for asynchronous processing of messages.
     */
    @NonNull
    ExecutorService executer;

    /**
     * Spring application context used to load input-output handlers dynamically by name.
     */
    ApplicationContext context;

    /**
     * The bean name of the {@link InputOutputHandler} used to process inputs and produce outputs.
     */
    String inOutHandlerName;

    /**
     * Retrieves the current goal from the queue without removing it.
     * 
     * @return the current goal at the front of the queue or {@code null} if queue is empty
     */
    public Goal getGoal() {
        return goalQueue.peek();
    }

    /**
     * Removes the current goal from the queue and closes it.
     * 
     * @return the removed goal or {@code null} if queue was empty
     */
    public Goal removeGoal() {
        Goal goal = goalQueue.poll();
        if (goal != null) {
            goal.close();
        }
        return goal;
    }
    
    public volatile boolean closed = false;
    
    @Override
    public void close() {
    	goals.clear();
    	goalQueue.clear();
    	agentLevelHistory.close();
    	envLevelHistory.close();
		variables.close();
		agentpool = null;
		env = null;
		executer.shutdownNow();
    }
    

    /**
     * Retrieves the default goal defined in this environment.
     * 
     * @return the default goal if defined, otherwise {@code null}
     */
    public Goal getdeFaultGoal() {
        if (defaultGoal != null) {
            return defaultGoal;
        }
        for (Goal goal : goals) {
            if (goal.isDefaultGoal()) {
                defaultGoal = goal;
                return goal;
            }
        }
        return null;
    }

    /**
     * Asynchronously processes a message by using the configured {@link InputOutputHandler}.
     * The handler is initialized with this environment state and submitted to the executor.
     * 
     * @param msg the input message to be processed
     * @return a future representing the asynchronous result of the input-output handler
     */
    public Future<InputOutputHandler> process(String msg) {
        LoggingData data = LoggingDataHolder.get();
        return CompletableFuture.supplyAsync(() -> {
            InputOutputHandler inOutHandler;
            try {
                LoggingDataHolder.set(data);
                inOutHandler = (InputOutputHandler) context.getBean(inOutHandlerName);
                inOutHandler.setInput(msg);
                inOutHandler.setState(this);
                inOutHandler.status(env.process(this, msg, inOutHandler));
            } finally {
                LoggingDataHolder.clear();
            }

            return inOutHandler;
        }, executer);
    }
}
