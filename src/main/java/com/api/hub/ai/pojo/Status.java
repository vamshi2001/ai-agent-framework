package com.api.hub.ai.pojo;

import java.util.Date;

import com.api.hub.ai.constants.StatusValues;

import lombok.Data;

/**
 * Represents the status of a Goal or Task within the AI-Agent Framework.
 * <p>
 * This POJO tracks lifecycle timestamps and status indicators for Goals and Tasks.
 * It includes standard status values as well as a dynamic status field for agents to
 * maintain custom or extended status tracking according to their specific logic.
 * </p>
 * 
 * <p><b>Fields:</b></p>
 * <ul>
 *   <li><b>startTime:</b> Timestamp when the goal or task was started (default is creation time).</li>
 *   <li><b>endTime:</b> Timestamp when the goal or task was completed or ended.</li>
 *   <li><b>status:</b> Standard status from {@link StatusValues}, such as NEW, IN_PROGRESS, or COMPLETED.</li>
 *   <li><b>dynamicStatus:</b> Custom integer status value for agents to track internal or extended states.</li>
 * </ul>
 * 
 * <p><b>Usage:</b></p>
 * <p>
 * Both {@link Goal} and {@link Task} POJOs embed this class to represent and manage their execution status.
 * The <code>dynamicStatus</code> field allows agents to implement additional status logic beyond the predefined enum.
 * </p>
 * 
 * @see StatusValues
 * @see Goal
 * @see Task
 * @since 1.0
 */
@Data
public class Status {

    private Date startTime = new Date();
    private Date endTime;
    private StatusValues status = StatusValues.NEW;
    private int dynamicStatus = 0;
}
