package com.project.task.manager.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.task.manager.domain.Comment;
import com.project.task.manager.domain.Task;
import com.project.task.manager.domain.User;
import com.project.task.manager.domain.DTO.CommentDTO;
import com.project.task.manager.domain.DTO.TaskDTO;
import com.project.task.manager.repository.CommentRepository;
import com.project.task.manager.repository.TaskRepository;
import com.project.task.manager.repository.UserRepository;
import com.project.task.manager.service.CommentService;
import com.project.task.manager.service.map.CommentMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{
	
	private final CommentRepository repo;
	
	private  final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
	
	@Override
	@Transactional(readOnly = false)
	public Comment addComment (CommentDTO commentDTO, Task task, User user) {
		
		//toDo NullPointerException
		
		log.info("saving new comment: {}", task.getId());
		
		Comment comment = Comment.builder()
				.task(task)
				.commenter(user)
				.text(commentDTO.getText())
				.build();
		
		return repo.save(comment);
	}
	
	@Override
	@Transactional (readOnly = true)
	public Page<CommentDTO> showTaskComments(
			Task task, 
			int pageNumber, 
			int pageSize) {
		log.info("fetching a comment: {}", task.getId());
		
		//toDo NullPointerException
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		
		return repo.findByTask(task, pageable);
	}
	

}
