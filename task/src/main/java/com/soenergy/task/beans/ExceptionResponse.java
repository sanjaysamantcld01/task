package com.soenergy.task.beans;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ExceptionResponse {
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone ="Europe/London" )
	private LocalDateTime errrorTimestamp;
	private String code;
	private String message;
	private String details;
	public ExceptionResponse(LocalDateTime errrorTimestamp, String code, String message, String details) {
		super();
		this.errrorTimestamp = errrorTimestamp;
		this.code = code;
		this.message = message;
		this.details = details;
	}
	public ExceptionResponse() {
		super();
	}
	public LocalDateTime getErrrorTimestamp() {
		return errrorTimestamp;
	}
	public void setErrrorTimestamp(LocalDateTime errrorTimestamp) {
		this.errrorTimestamp = errrorTimestamp;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	@Override
	public String toString() {
		return "ExceptionResponse [errrorTimestamp=" + errrorTimestamp + ", code=" + code + ", message=" + message
				+ ", details=" + details + "]";
	}
	
	
	

}
