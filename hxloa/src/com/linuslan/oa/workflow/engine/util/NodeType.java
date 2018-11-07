package com.linuslan.oa.workflow.engine.util;

public enum NodeType {
	START, END, TASK, STATE, JOIN, FORK;
	public static NodeType getType(String type) {
		return valueOf(type.toUpperCase());
	}
}
