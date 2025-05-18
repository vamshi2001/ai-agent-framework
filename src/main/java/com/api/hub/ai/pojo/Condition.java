package com.api.hub.ai.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Condition {

	private List<String> taskGoalName;
	private List<String> taskName;
	private List<Object[]> taskDescription;
	private List<String> taskCreatedBy;
	private List<String> taskObjectType;
	private List<Object[]> taskObjectMethodValue;
	
}
