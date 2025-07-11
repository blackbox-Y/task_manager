package com.project.task.manager.service.map;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.project.task.manager.domain.Comment;
import com.project.task.manager.domain.DTO.CommentDTO;

public class CommentMapper {
	public static CommentDTO toComDTO (Comment comment) {
		return new CommentDTO().builder()
				.userEmail(comment.getCommenter().getUsername()
//						.getEmail() видимо из-за использования userDetails пришлось поменять на getUsername()
						)
				.text(comment.getText())
				.taskTitle(comment.getTask().getTitle())
				.build();
	}
	
	public static Page <CommentDTO> toComPageDTO (Page <Comment> comPage) {
		
	     List<CommentDTO> comDTOs = comPage.getContent()
	                .stream()
	                .map(CommentMapper::toComDTO)
	                .collect(Collectors.toList());
	        return new PageImpl<>(comDTOs,
	        		PageRequest.of(
	                		comPage.getNumber(), 
	                		comPage.getSize(), 
	                		comPage.getSort()),
	                		comPage.getTotalElements()
	        );
	}
}
