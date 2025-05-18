package com.api.hub.ai.pojo;

import java.util.List;

import com.api.hub.ai.handler.impl.EnvironmentState;
import com.api.hub.ai.handler.InputOutputHandler;

import lombok.Data;

/**
 * Represents the runtime state of an AI agent within the AI-Agent Framework.
 * <p>
 * This POJO encapsulates all essential context and data that an agent requires to perform its operations.
 * It contains the current task, agent action history, environment snapshot, and input/output handler.
 * </p>
 * 
 * <p><b>Lifecycle:</b></p>
 * <p>
 * Instances of this class are created and initialized by the <code>com.api.hub.ai.starter.Environment</code> class (specifically,
 * its process function) before invoking the agent. The agent method receives this instance as input
 * to execute the required logic with all relevant state information readily available.
 * </p>
 * 
 * <p><b>Fields:</b></p>
 * <ul>
 *   <li><b>taskToPerform:</b> The task the agent needs to execute.</li>
 *   <li><b>agentHistory:</b> History of goals, tasks, and actions the agent has performed.</li>
 *   <li><b>actionsPerformed:</b> List of actions taken by the agent during the current execution.</li>
 *   <li><b>env:</b> Snapshot of the environment state accessible to the agent.</li>
 *   <li><b>handler:</b> Input/output handler for communication and data exchange.</li>
 * </ul>
 * 
 * <p><b>Example Usage by Environment:</b></p>
 * <pre>{@code
 * State state = new State();
 * state.setTaskToPerform(task);
 * state.setAgentHistory(history);
 * state.setActionsPerformed(actions);
 * state.setEnv(environmentState);
 * state.setHandler(ioHandler);
 * 
 * (boolean) methodToInvoke.invoke(agentHandler, new Object[] { state}) {@link com.api.hub.ai.handler.impl.AgentDefination}
 * }</pre>
 * 
 * @see Task
 * @see AgentHistory
 * @see Action
 * @see EnvironmentState
 * @see InputOutputHandler
 * @since 1.0
 */
@Data
public class State {

    private Task taskToPerform;
    private AgentHistory agentHistory;
    private List<Action> actionsPerformed;
    private EnvironmentState env;
    private InputOutputHandler handler;
}