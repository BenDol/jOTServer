package org.jotserver.ot.model.chat;

import org.junit.Test;
import static org.jotserver.ot.model.chat.SpeakType.*;
import static org.junit.Assert.*;


public class TestSpeakType {

    @Test
    public void canLookup() {
        assertEquals(SAY, get(1));
        assertEquals(BROADCAST, get(9));
        assertEquals(PRIVATE, get(4));
    }

    @Test
    public void hasType() {
        assertEquals(Type.PUBLIC, SAY.getType());
        assertEquals(Type.PRIVATE, PRIVATE.getType());
        assertEquals(Type.BROADCAST, BROADCAST.getType());
    }

    @Test
    public void hasId() {
        assertEquals(1, SAY.getId());
        assertEquals(4, PRIVATE.getId());
        assertEquals(9, BROADCAST.getId());
    }

}
