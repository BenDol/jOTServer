package org.jotserver.ot.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestMOTD {

    private static final int NUMBER = 1337;
    private static final String MESSAGE = "This is a message";

    @Test
    public void hasMessage() {
        MOTD motd = new MOTD(NUMBER, MESSAGE);
        assertEquals(MESSAGE, motd.getMessage());
        motd.setMessage(MESSAGE + MESSAGE);
        assertEquals(MESSAGE + MESSAGE, motd.getMessage());
    }

    @Test
    public void hasNumber() {
        MOTD motd = new MOTD(NUMBER, MESSAGE);
        assertEquals(NUMBER, motd.getNumber());
        motd.setNumber(NUMBER + NUMBER);
        assertEquals(NUMBER + NUMBER, motd.getNumber());
    }

}
