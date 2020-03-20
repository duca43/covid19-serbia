package org.serbia.covid19.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class ExceptionHandler {
	
	@org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage()).details(request.getDescription(false)).build();
		
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public ResponseEntity<?> exceptionHandler(Exception ex, WebRequest request) {
		ErrorDetails errorDetails = ErrorDetails.builder().timestamp(new Date()).message(ex.getMessage()).details(request.getDescription(false)).build();
	
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
