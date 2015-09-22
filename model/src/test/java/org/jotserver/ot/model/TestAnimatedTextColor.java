package org.jotserver.ot.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestAnimatedTextColor {

    @Test
    public void mapsToIntegerIds() {
        assertEquals(5, AnimatedTextColor.BLUE.getId());
        assertEquals(35, AnimatedTextColor.LIGHTBLUE.getId());
        assertEquals(30, AnimatedTextColor.LIGHTGREEN.getId());
        assertEquals(83, AnimatedTextColor.PURPLE.getId());
        assertEquals(129, AnimatedTextColor.LIGHTGREY.getId());
        assertEquals(144, AnimatedTextColor.DARKRED.getId());
        assertEquals(180, AnimatedTextColor.RED.getId());
        assertEquals(198, AnimatedTextColor.ORANGE.getId());
        assertEquals(210, AnimatedTextColor.YELLOW.getId());
        assertEquals(215, AnimatedTextColor.WHITE.getId());
    }
}
