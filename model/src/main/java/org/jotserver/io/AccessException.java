package org.jotserver.io;

public class AccessException extends Exception {

    private static final long serialVersionUID = 1L;

    public AccessException() {
        super();
    }

    public AccessException(String message) {
        super(message);
    }

    public AccessException(Throwable cause) {
        super(cause);
    }

    public AccessException(String message, Throwable cause) {
        super(message, cause);
    }

}