package com.project.task.manager.service;

import org.springframework.data.domain.Page;

import com.project.task.manager.domain.Comment;
import com.project.task.manager.domain.Task;
import com.project.task.manager.domain.User;
import com.project.task.manager.domain.DTO.CommentDTO;

public interface CommentService {
	Comment addComment (
			CommentDTO commentDTO,
			Task task,
			User user);
	
	Page <CommentDTO> showTaskComments (
			Task task,
			int pageNumber, 
			int pageSize);
}
