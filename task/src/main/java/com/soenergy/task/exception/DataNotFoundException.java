package com.soenergy.task.exception;

public class DataNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 8901700396195101116L;

	public DataNotFoundException(String message) {
		super(message);
	}
	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
