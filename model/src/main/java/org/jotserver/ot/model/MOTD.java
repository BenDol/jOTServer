package org.jotserver.ot.model;

public class MOTD {
	
	private int number;
	private String message;
	
	public MOTD() {
		number = 0;
		message = null;
	}
	
	public MOTD(int number, String message) {
		this();
		this.number = number;
		this.message = message;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
