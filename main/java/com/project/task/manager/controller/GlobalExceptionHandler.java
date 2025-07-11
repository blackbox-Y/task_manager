package com.project.task.manager.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.project.task.manager.domain.constants.AccessDenidedException;
import com.project.task.manager.domain.constants.ResourceNotFoundException;
import com.project.task.manager.domain.constants.ValidationException;
import com.project.task.manager.domain.constants.BusinessException;
import com.project.task.manager.domain.constants.DataIntegrityViolationException;
import com.project.task.manager.domain.constants.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> createResponseEntity (
			RuntimeException ex,
			WebRequest req,
			HttpStatus status) {
		log.error("{}: {} - Path: {}", ex.getMessage() ,req.getDescription(false), ex);
		ErrorResponse response = new ErrorResponse (
				LocalDateTime.now(),
				ex.getMessage(),
				status.value()
			);
		return new ResponseEntity<>(response, status);
	}
		
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException (
			BusinessException ex,
			WebRequest req
			) {
		return createResponseEntity(ex, req, HttpStatus.BAD_REQUEST);	
	}
	
	@ExceptionHandler(AccessDenidedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException (
			AccessDenidedException ex,
			WebRequest req
			) {
		return createResponseEntity(ex, req, HttpStatus.FORBIDDEN);	
	} 
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException (
			ResourceNotFoundException ex, 
			WebRequest req
			) {
		return createResponseEntity(ex, req, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UnsupportedOperationException.class)
	public ResponseEntity<ErrorResponse> handleUnsupportedOperationException (
			UnsupportedOperationException ex,
			WebRequest req
			) {
		return createResponseEntity(ex, req, HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
			ValidationException ex, 
			WebRequest req
			){
		return createResponseEntity(ex, req, HttpStatus.BAD_REQUEST);
	} 
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
			DataIntegrityViolationException ex, 
			WebRequest req
			){
		return createResponseEntity(ex, req, HttpStatus.CONFLICT);
	} 

	@ExceptionHandler(Exception.class)
 	public ResponseEntity<ErrorResponse> handleException (
 			Exception ex,
 			WebRequest req) {
		log.error("Unhandled error: {} -- Path:{}", ex.getMessage(), req.getDescription(false), ex);
		ErrorResponse response = new ErrorResponse (
				LocalDateTime.now(),
				"Internal Server exception",
				HttpStatus.INTERNAL_SERVER_ERROR.value()
				);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}
