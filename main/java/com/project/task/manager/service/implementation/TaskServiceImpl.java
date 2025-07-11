package com.project.task.manager.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.task.manager.domain.constants.ErrorMessage;
import com.project.task.manager.domain.Comment;
import com.project.task.manager.domain.Task;
import com.project.task.manager.domain.User;
import com.project.task.manager.domain.DTO.CommentDTO;
import com.project.task.manager.domain.DTO.TaskDTO;
import com.project.task.manager.domain.DTO.TaskWithCommentsDTO;
import com.project.task.manager.domain.status.STATUS;
import com.project.task.manager.repository.TaskRepository;
import com.project.task.manager.service.TaskService;
import com.project.task.manager.service.UserService;
import com.project.task.manager.service.map.TaskMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService{
	
	private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
	
	private final CommentServiceImpl commentService;
	private final UserService userService;
	
	private final TaskRepository taskRepo;
	
	
	
	@Override
	@PreAuthorize("authentication.principal.id == #id")
	@Transactional(readOnly = true)
	public TaskDTO findTaskDTO (Long taskId, Long userId) {
		
		log.info("attempt to get task: {}", taskId);
		
		Task task = taskRepo.findById(taskId).orElseThrow(
				()-> new EntityNotFoundException(ErrorMessage.TASK_NOT_FOUND));
		return TaskMapper.toTaskDTO(task);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Task findTask (Long taskId, Long userId) {
		
		log.info("attempt to get task: {}", taskId);
		
		return taskRepo.findById(taskId).orElseThrow(
				()-> new EntityNotFoundException(ErrorMessage.TASK_NOT_FOUND));
	}

	@Override
	@PreAuthorize("authentication.principal.id == #authorId")
	@Transactional(readOnly = true)
	public Page<TaskDTO> findAuthorTasks(Long authorId, int pageNumber, int pageSize) {
		
		log.info("attempt to get task via author: {}", authorId);
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		User user = userService.findById(authorId);
		
		Page <Task> taskPage = taskRepo.findByAuthor(pageable, user);
		Page<TaskDTO> dtoTaskPage = TaskMapper.toTaskPageDTO(taskPage);
		
		return dtoTaskPage;
	}

	@Override
	@PreAuthorize("authentication.principal.id == #authorId")
	@Transactional
	public String setStatus(STATUS status, Long taskId, Long authorId) {
		
		log.info("attempt to set task's status : {}", taskId);
		
			findTask(taskId, authorId).setStatus(status);
			return "Status updated: " + status;
	}

	@Override
	@PreAuthorize("authentication.principal.id == #authorId")
	@Transactional
	public Comment addComments(CommentDTO comDTO, Long taskId, Long authorId) {	
			
		log.info("attempt to add a comment: {}", taskId);
		
			Task task = findTask(taskId, authorId);
			User user = userService.findById(authorId);
			
			return commentService.addComment(comDTO, task, user);
	}
	

	@Override
	@PreAuthorize("authentication.principal.id == #authorId")
	@Transactional(readOnly = true)
	public TaskWithCommentsDTO taskWithComments(Long taskId, Long authorId, int pageNumber, int pageSize) {
		
		log.info("attempt to get task with coms: {}", taskId);
		
		TaskDTO task = findTaskDTO(taskId, authorId);
		Page <CommentDTO> comPage = showTaskComments(taskId, authorId, pageNumber, pageSize);
		
		TaskWithCommentsDTO taskCom = new TaskWithCommentsDTO(task, comPage); 
		
		return taskCom;
	}

	@Override
	@PreAuthorize("authentication.principal.id == #authorId")
	@Transactional (readOnly = true)
	public Page<CommentDTO> showTaskComments(
			Long taskId, 
			Long authorId, 
			int pageNumber, 
			int pageSize) {
		
		log.info("attempt to get com page: {}", taskId);
		
		Task task = findTask(taskId, authorId);
		User user = userService.findById(authorId);
		Page <CommentDTO> commentPage = commentService
				.showTaskComments(task, pageNumber, pageSize);
		return commentPage;
	}

}
