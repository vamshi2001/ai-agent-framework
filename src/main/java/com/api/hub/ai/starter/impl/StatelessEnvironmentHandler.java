package com.api.hub.ai.starter.impl;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.api.hub.ai.cache.Cache;
import com.api.hub.ai.handler.InputOutputHandler;
import com.api.hub.ai.handler.impl.AgentDefination;
import com.api.hub.ai.handler.impl.AgentPool;
import com.api.hub.ai.handler.impl.EnvironmentState;
import com.api.hub.ai.pojo.Action;
import com.api.hub.ai.pojo.AgentHistory;
import com.api.hub.ai.pojo.AgentHistory.History;
import com.api.hub.ai.pojo.Goal;
import com.api.hub.ai.pojo.State;
import com.api.hub.ai.pojo.Task;
import com.api.hub.exception.ApiHubException;
import com.api.hub.exception.InternalServerException;

/**
 * The {@code StatelessEnvironmentHandler} is the default implementation of the AI Agent Framework's
 * {@link com.api.hub.ai.handler.impl.Environment} interface.
 * <p>
 * This class processes incoming user inputs and delegates execution to appropriate agents using a stateless, pluggable architecture.
 * The handler works in a goal-task-action loop and manages agent selection, invocation, and action history tracking.
 * </p>
 *
 * <p>
 * The framework architecture follows the pattern:
 * <strong>StatelessEnvironmentHandler → EnvironmentLoader → Environment</strong>.
 * This design allows developers to provide their own implementation of the `process` method by
 * extending {@link com.api.hub.ai.handler.impl.EnvironmentLoader} and setting the property
 * <code>env.handler.default=false</code> in the application configuration.
 * </p>
 *
 * <p>
 * This implementation is only loaded if a {@link Cache} bean is available in the context and the property
 * <code>env.handler.default=true</code> is set.
 * </p>
 *
 * @author  
 * @see com.api.hub.ai.handler.impl.EnvironmentLoader
 * @see com.api.hub.ai.handler.impl.Environment
 */

@Component
@ConditionalOnBean(Cache.class)
@ConditionalOnProperty(
	    name = "env.handler.default",
	    havingValue = "true"
	)
public class StatelessEnvironmentHandler extends EnvironmentLoader{

	/**
     * Processes the user input within the AI agent environment. This method orchestrates task creation, agent selection,
     * agent invocation, and action history tracking in a stateless manner.
     *
     * <p>
     * The method performs the following:
     * <ul>
     *     <li>Fetches the {@link AgentPool} from the application context.</li>
     *     <li>Resolves the current {@link Goal} from the environment.</li>
     *     <li>Creates a new {@link Task} from user input.</li>
     *     <li>Finds the best matching {@link AgentDefination} for the task.</li>
     *     <li>Creates a {@link State} object, invokes the agent, and tracks all performed actions.</li>
     *     <li>Repeats the loop if agent signals continuation.</li>
     * </ul>
     * </p>
     *
     * @param env The current {@link EnvironmentState}, containing goal queue, agent history, and configuration.
     * @param userResponse The user's textual input, which is wrapped into a task.
     * @param handler The {@link InputOutputHandler} for managing input/output during agent invocation.
     *
     * @return {@code true} if processing completed successfully; {@code false} otherwise.
     */
	@Override
	public boolean process(EnvironmentState env, String userResponse, InputOutputHandler handler) {
		try {
			AgentPool pool  = (AgentPool) context.getBean(env.getAgentpool());
			if(pool == null) {
				throw new InternalServerException("8002-ai-hub", "Unable to find matching Agent Pool instance for "+ env.getAgentpool(), "");
			}
			Goal currentGoal;
			currentGoal = env.getGoalQueue().peek();
			if(currentGoal==null) {
				currentGoal = env.getdeFaultGoal();
			}
			if(currentGoal==null) {
				throw new InternalServerException("8002-ai-hub", "Unable to fetch goal from Environment, there should be atleast one default Goal In env - "
						+ env.getName() + " and id - " + env.getId(), "");
			}
			Task prev_task = currentGoal.getTask();
			Task user_task = new Task("user_created_task",userResponse,"user",currentGoal);
			currentGoal.addTask(user_task, false);
			Task currentTask = null;
			if(prev_task != null) {
				currentTask = prev_task;
			}
			else {
				currentTask = user_task;
			}
			boolean continueToNext =  false;
			do {
				AgentDefination agentDef = pool.getMatchingAgent(currentTask);
				
				if(agentDef == null)
					throw new InternalServerException("8002-ai-hub", "Expected AgentDefination instance, received null In environment - "
							+ env.getName() + " and id - " + env.getId() + " for Task - " + currentTask.toString(), "");
				
				State state = new State();
				state.setTaskToPerform(currentTask);
				state.setEnv(env);
				AgentHistory agentHis = env.getAgentLevelHistory().get(agentDef.getName());
				if(agentHis == null) {
					agentHis = new AgentHistory();
				}
				state.setAgentHistory(agentHis);
				state.setHandler(handler);
				continueToNext = agentDef.invokeAgent(state);
				
				currentTask = currentGoal.getTask();
				if(currentTask == null) {
					currentGoal = env.getGoalQueue().peek();
					if(currentGoal == null) {
						break;
					}else {
						currentTask = currentGoal.getTask();
						if(currentTask == null) {
							break;
						}
					}
				}
				
				List<Action> actionsPerformed = state.getActionsPerformed();
				AgentHistory history = state.getAgentHistory();
				if(history != null) {
					History newAction = history.new History(currentGoal,currentTask,actionsPerformed);
					history.getHistory().add(newAction);
				}
				
			}while(continueToNext);
			
			
			
		}catch (ApiHubException e) {
			log.error(SYSTEM_ERROR, e.toString());
			return false;
		}catch (Exception e) {
			log.error(SYSTEM_ERROR, "Unexpected error occured while processing uer request" + env.getName()+" "+e.getMessage());
			return false;
		}
		
		return true;
	}

}
