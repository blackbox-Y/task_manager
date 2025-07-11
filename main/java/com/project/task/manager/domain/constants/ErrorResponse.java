package com.project.task.manager.domain.constants;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor 
public class ErrorResponse {
	private LocalDateTime timeStamp;
	
	private String message;
	
	private int status;
}
