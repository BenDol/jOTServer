package org.jotserver.ot.model.account;

import org.jotserver.ot.model.player.Premium;


public class Account {

    private long number;
    private String password;
    private Premium premium;

    public Account() {
        number = 0;
        password = null;
        premium = new Premium();
    }

    public Account(long number) {
        this();
        this.number = number;
    }

    public Account(long number, String password) {
        this(number);
        this.password = password;
    }

    public long getNumber() {
        return number;
    }
    public void setNumber(long number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Premium getPremium() {
        return premium;
    }

}
