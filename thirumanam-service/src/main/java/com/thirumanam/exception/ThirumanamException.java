package com.thirumanam.exception;

public class ThirumanamException extends Throwable{
	
	private String code;
	private String message;
	
	public ThirumanamException(String code, String message) {
		this.code = code;
		this.message = message;
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
}