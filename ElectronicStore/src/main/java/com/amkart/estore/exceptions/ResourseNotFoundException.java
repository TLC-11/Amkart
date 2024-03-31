package com.amkart.estore.exceptions;

public class ResourseNotFoundException extends RuntimeException {
	public ResourseNotFoundException() {
		
		super("Resourse not found");
	}
	public ResourseNotFoundException(String message) {
		
		super(message);
	}

}
