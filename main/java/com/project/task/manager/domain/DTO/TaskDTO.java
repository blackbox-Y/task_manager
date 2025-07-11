package com.project.task.manager.domain.DTO;

import java.util.LinkedList;

import com.project.task.manager.domain.status.PRIORITY;
import com.project.task.manager.domain.status.STATUS;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(description = "Task request")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskDTO {
	@Schema(
			description = "Task's title name", 
			example = "sign documents"
			)
	@Size(
			min = 3,
			max = 150, 
			message = "task title name length must be between 3 and 150 signs")
	@NotBlank(
			message = "task title must not be blank"
			)
	private String taskTitle;
	
	
	private String description;
	private LinkedList <String> steps;
	
	private PRIORITY priority;
	private STATUS status;
	
	private String authorName;
	private String agentName;
}
