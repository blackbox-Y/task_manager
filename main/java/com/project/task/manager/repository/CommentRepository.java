package com.project.task.manager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.task.manager.domain.Comment;
import com.project.task.manager.domain.Task;
import com.project.task.manager.domain.DTO.CommentDTO;

public interface CommentRepository extends JpaRepository <Comment, Long>{
	
	
	@Query("SELECT new com.project.task.manager.domain.DTO.CommentDTO"
			+ "(c.ID, c.taskTitle, c.userEmail, c.text)"
			+ "FROM comment WHERE c.task =:task")
	Page <CommentDTO> findByTask (@Param("Task") Task task, Pageable pageable);
}
