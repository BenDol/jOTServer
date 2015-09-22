package org.jotserver.ot.model.account;

import org.jotserver.io.AccessException;

public class AccountAccessException extends AccessException {

    private static final long serialVersionUID = 1L;

    public AccountAccessException() {
        super();
    }

    public AccountAccessException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public AccountAccessException(String arg0) {
        super(arg0);
    }

    public AccountAccessException(Throwable arg0) {
        super(arg0);
    }
}
