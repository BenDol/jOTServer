package org.jotserver.ot.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestTextMessageType {

    @Test
    public void hasIntegerType() {
        assertEquals(1, TextMessageType.CONSOLE_YELLOW.getType());
        assertEquals(4, TextMessageType.CONSOLE_LIGHT_BLUE.getType());
        assertEquals(17, TextMessageType.CONSOLE_ORANGE.getType());
        assertEquals(23, TextMessageType.STATUS_SMALL.getType());
        assertEquals(22, TextMessageType.DESCRIPTION.getType());
        assertEquals(21, TextMessageType.DEFAULT.getType());
    }
}
