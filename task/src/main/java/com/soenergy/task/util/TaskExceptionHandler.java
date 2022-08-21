package com.soenergy.task.util;


import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.soenergy.task.beans.ExceptionResponse;
import com.soenergy.task.exception.DataNotFoundException;
import com.soenergy.task.exception.RepositoryException;

@RestControllerAdvice
public class TaskExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger logger=LoggerFactory.getLogger(TaskExceptionHandler.class);
	
	//Handles DataNotFoundException
	@ExceptionHandler(DataNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleDataNotFoundException(
					DataNotFoundException ex,  WebRequest request){
		
		String description=String.format("Data not found error - ", 
											request.getDescription(false));
		ExceptionResponse exceptionResponse=new ExceptionResponse(LocalDateTime.now(), 
												HttpStatus.NOT_FOUND.toString(),
															ex.getMessage(), description);
		logger.info("DataNotFoundException - {}",exceptionResponse);
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND );
	}

	//Handles RepositoryException
	@ExceptionHandler(RepositoryException.class)
	public final ResponseEntity<ExceptionResponse> handleRepositoryException(
						RepositoryException ex,  WebRequest request){
			
			String description=String.format("Repository Error - ", 
												request.getDescription(false));
			ExceptionResponse exceptionResponse=new ExceptionResponse(LocalDateTime.now(), 
													HttpStatus.INTERNAL_SERVER_ERROR.toString(),
																ex.getMessage(), description);
			logger.info("RepositoryException - {}",exceptionResponse);
			
			return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR );
	}
	//Handles MethodArgumentTypeMismatchException
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public final ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
					MethodArgumentTypeMismatchException ex,  WebRequest request){
				
				String description=String.format("Id should be specified as integer value - ", 
													request.getDescription(false));
				ExceptionResponse exceptionResponse=new ExceptionResponse(LocalDateTime.now(), 
														HttpStatus.BAD_REQUEST.toString(),
																	ex.getMessage(), description);
				logger.info("MethodArgumentTypeMismatchException - {}",exceptionResponse);
				
				return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST );
		}
	
	//handles MissingServletRequestParameterException
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
				MissingServletRequestParameterException ex, HttpHeaders headers, 
						HttpStatus status, WebRequest request) {
			ExceptionResponse exceptionResponse=
					new ExceptionResponse(LocalDateTime.now(), 
							status.toString(),
							ex.getMessage(), request.getDescription(false));
			logger.info("MissingServletRequestParameterException - {}",exceptionResponse);
		    return new ResponseEntity<>(exceptionResponse, status);
	}
	//handles NoHandlerFoundException
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, 
						HttpStatus status, WebRequest request) {
			ExceptionResponse exceptionResponse=
					new ExceptionResponse(LocalDateTime.now(), 
							status.toString(),
							ex.getMessage(), request.getDescription(false));
			logger.info("NoHandlerFoundException - {}",exceptionResponse);
		    return new ResponseEntity<>(exceptionResponse, status);
	}
	//handles MethodArgementNotValidException
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
				MethodArgumentNotValidException ex, HttpHeaders headers, 
						HttpStatus status, WebRequest request) {
			ExceptionResponse exceptionResponse=
					new ExceptionResponse(LocalDateTime.now(), 
							status.toString(),
							ex.getMessage(), request.getDescription(false));
			logger.info("MethodArgumentNotValidException - {}",exceptionResponse);
		    return new ResponseEntity<>(exceptionResponse, status);
	}
	//handles all exceptions
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllEExceptions(
					Exception ex,  WebRequest request) {
		ExceptionResponse exceptionResponse=new ExceptionResponse(LocalDateTime.now(), 
												HttpStatus.INTERNAL_SERVER_ERROR.toString(),
												ex.getMessage(), request.getDescription(false));
		logger.info("Internal Server Error - {}",exceptionResponse);
	    return new ResponseEntity<Object>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	//handles HttpRequestMethodNotSupportedException
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, HttpHeaders headers, 
						HttpStatus status, WebRequest request) {
			ExceptionResponse exceptionResponse=
					new ExceptionResponse(LocalDateTime.now(), 
							status.toString(),
							ex.getMessage(), request.getDescription(false));
			logger.info("HttpRequestMethodNotSupportedException - {}",exceptionResponse);
		    return new ResponseEntity<>(exceptionResponse, status);
	}

}
