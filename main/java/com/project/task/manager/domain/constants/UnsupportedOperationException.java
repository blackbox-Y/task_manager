package com.project.task.manager.domain.constants;

public class UnsupportedOperationException extends RuntimeException{

	public UnsupportedOperationException(String operationName) {
		super(String.format("Operation %s is not supported", operationName));
	}

}
