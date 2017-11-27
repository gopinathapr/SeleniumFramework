package com.ascendlearning.automation.ui.exceptions;

public class DriverException extends Exception {
	
	private static final long serialVersionUID = 1159518894670458957L;
	private static String message = "Driver Verification exception: ";

	public DriverException() {
		super();
	}
	
	public DriverException(String msg) {
		super(message+"  "+msg);
	}
	
	public DriverException(String msg, Throwable exp) {
		super(message+"  "+msg, exp);
	}

}
