package com.soenergy.task.exception;

public class RepositoryException extends RuntimeException{

	private static final long serialVersionUID = 8901705396195101116L;

	public RepositoryException(String message) {
		super(message);
	}
	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
}
