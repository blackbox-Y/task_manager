package com.project.task.manager.service.implementation;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.task.manager.domain.constants.ErrorMessage;
import com.project.task.manager.domain.User;
import com.project.task.manager.repository.UserRepository;
import com.project.task.manager.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository repo;
	
	@Override
	@Transactional(readOnly = true)
	public User findById(Long id) {
		return repo.findById(id).orElseThrow(
				()-> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));
	}

	@Override
	@Transactional(readOnly = true)
	public User findByEmail(String email) {
		return repo.findByEmail(email).orElseThrow(
				()-> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));
	}

}
