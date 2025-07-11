package com.project.task.manager.domain.constants;

public class DataIntegrityViolationException extends RuntimeException{

	public DataIntegrityViolationException(String resourceName, Long Id) {
		super(String.format("%s with ID=%d already exists", resourceName, Id));
	}
}
