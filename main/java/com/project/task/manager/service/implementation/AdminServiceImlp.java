package com.project.task.manager.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.task.manager.domain.constants.ErrorMessage;
import com.project.task.manager.domain.Task;
import com.project.task.manager.domain.User;
import com.project.task.manager.domain.DTO.CommentDTO;
import com.project.task.manager.domain.DTO.TaskDTO;
import com.project.task.manager.domain.DTO.TaskWithCommentsDTO;
import com.project.task.manager.domain.status.PRIORITY;
import com.project.task.manager.domain.status.STATUS;
import com.project.task.manager.repository.TaskRepository;
import com.project.task.manager.service.AdminService;
import com.project.task.manager.service.UserService;
import com.project.task.manager.service.map.TaskMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminServiceImlp implements AdminService{
	
	private  final Logger log = LoggerFactory.getLogger(AdminServiceImlp.class);
	
	private final TaskRepository taskRepo;
	private final CommentServiceImpl comServ;
	private final UserService userService;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public Task addTask(Task task) {
		log.info("attempt to save a task: {}", task);
		taskRepo.save(task);
		return task;
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public Task findTask(Long taskId) {
		log.info("attempt to find a task by ID: {}", taskId);
		return taskRepo.findById(taskId).orElseThrow(
				()-> new EntityNotFoundException(ErrorMessage.TASK_NOT_FOUND));
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional(readOnly = true)
	public Task findTask(String taskTitle) {
		log.info("attempt to find a task by title: {}", taskTitle);
		return taskRepo.findByTitle(taskTitle).orElseThrow(
				()-> new EntityNotFoundException(ErrorMessage.TASK_NOT_FOUND));
	}
	
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional(readOnly = true)
	public Page<TaskDTO> findAllTasks(int pageNumber, int pageSize) {
		log.info("attempt to create a page of all title");
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<TaskDTO> dtoTaskPage = TaskMapper
				.toTaskPageDTO(taskRepo.findAll(pageable));
		
		return dtoTaskPage;
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional (readOnly = true)
	public Page<TaskDTO> findAgentTasks(Long id, int pageNumber, int pageSize) {
		
		User user = userService.findById(id);
		
		log.info("attempt to get a page of tasks by agent: {}", user);
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<TaskDTO> dtoTaskPage = TaskMapper
				.toTaskPageDTO(taskRepo.findByAgent(pageable, user));
		
		return dtoTaskPage;
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional (readOnly = true)
	public Page<TaskDTO> findAuthorTasks(Long id, int pageNumber, int pageSize) {
		User user = userService.findById(id);
		
		log.info("attempt to get a page of tasks by author: {}", user);
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<TaskDTO> dtoTaskPage = TaskMapper
				.toTaskPageDTO(taskRepo.findByAuthor(pageable, user));
		
		return dtoTaskPage;
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public String setStatus(STATUS status, Long Id) {
		
		log.info("attempt to set task status: {}", status);
		
		findTask(Id).setStatus(status);
		return status.toString()+ " was set";
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public String setPriority(PRIORITY priority, Long Id) {
		
		log.info("attempt to set task priority: {}", priority);
		
		findTask(Id).setPriority(priority);
		return priority.toString()+ " was set";
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public String setAgent(Long userId, Long taskId) {
		
		User user = userService.findById(userId);
		
		log.info("attempt to set agent: {}", user);
		
		findTask(taskId).setAgent(user);
		return user.getUsername()+ "is an agent";
	}
	
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public String addComments(CommentDTO comDTO, Long taskId, Long userId) {
		
		log.info("attempt to add a comment: {}", comDTO);
		
		Task task = findTask(taskId);
		
		User user = userService.findById(userId);

		
		comServ.addComment(comDTO, task, user);
		return comDTO.getText();
	}
	
	@Override
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteTask(Long Id) {
		
		log.info("attempt to delete task: {}", Id);
		
		Task task = findTask(Id);
		taskRepo.deleteById(Id);
		return task.getTitle() + " task is deletet";
	}

	@Override
	@Transactional (readOnly = true)
	public TaskWithCommentsDTO taskWithComments(Long Id, Long userId, int pageNumber, int pageSize) {
		
		log.info("attempt to get task with comments: {}", Id);
		
		Task task = findTask(Id);
		TaskDTO taskDTO = TaskMapper.toTaskDTO(task);
		Page <CommentDTO> comPage = showTaskComments(Id, userId, pageNumber, pageSize);
		
		TaskWithCommentsDTO taskCom = new TaskWithCommentsDTO(taskDTO, comPage); 
		
		return taskCom;
	}


	}
