package org.jotserver.ot.model.player;

import org.jotserver.io.AccessException;

public class PlayerAccessException extends AccessException {

	private static final long serialVersionUID = 1L;

	public PlayerAccessException() {
		super();
	}

	public PlayerAccessException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PlayerAccessException(String arg0) {
		super(arg0);
	}

	public PlayerAccessException(Throwable arg0) {
		super(arg0);
	}

}
