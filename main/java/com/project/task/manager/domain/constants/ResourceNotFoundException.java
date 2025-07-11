package com.project.task.manager.domain.constants;

public class ResourceNotFoundException extends RuntimeException{

	public ResourceNotFoundException(String resourceName, Long Id) {
		super(String.format("%s with ID=%d cannot be found", resourceName, Id));
	}

}
