package org.jotserver.ot.model;

import org.jotserver.io.AccessException;

public class MOTDAccessException extends AccessException {

	private static final long serialVersionUID = 1L;

	public MOTDAccessException() {
		super();
	}

	public MOTDAccessException(String message) {
		super(message);
	}

	public MOTDAccessException(Throwable cause) {
		super(cause);
	}

	public MOTDAccessException(String message, Throwable cause) {
		super(message, cause);
	}

}
