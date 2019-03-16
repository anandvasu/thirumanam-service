package com.thirumanam.util;

import java.time.LocalDate;
import java.time.Period;

import com.thirumanam.model.Status;

public class Util {
	
	public static int calculateAge(String dob) {
		String[] data = dob.split("/");
		LocalDate birthday = LocalDate.of(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
		return Period.between(birthday, LocalDate.now()).getYears();
	}	
	
	public static Status populateStatus(int code, String message) {
		Status status = new Status();
		status.setCode(code);
		status.setMessage(message);
		return status;
	}
}