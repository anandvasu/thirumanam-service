package com.thirumanam.util;

public interface ErrorMessageConstants {
	
	int CODE_INVALID_JWT_ISSUER = 100;
	String MESSAGE_INVALID__JWT_ISSUER = "Unautorized User. Please contact customer support.";
	int CODE_EXPIRED_JWT = 9000;
	String MESSAGE_EXPIRED_JWT = "Expired JWT";
	
	int CODE_UNAUTHORIZED = 9001;
	String MESSAGE_UNAUTHORIZED = "Not Authorized";
	
	String INVALID_CURRENT_PASSWORD = "Current Password is invalid.";
	String SERVER_ERROR = "Server Error. Please try again later.";
	int CODE_INVALID_USER_PASSWORD = 10000;
	String INVALID_USER_PASSWORD = "Invalid Username or Password.";
	String EMAIL_ALREADY_EXISTS = "Email already exists.";
}
