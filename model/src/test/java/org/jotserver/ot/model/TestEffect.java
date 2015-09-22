package org.jotserver.ot.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestEffect {

    @Test
    public void hasIntegerType() {
        assertEquals(3, Effect.PUFF.getType());
        assertEquals(11, Effect.TELEPORT.getType());
        assertEquals(17, Effect.POISON.getType());
        assertEquals(4, Effect.BLOCKHIT.getType());
    }
}
