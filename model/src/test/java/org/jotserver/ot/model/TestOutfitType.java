package org.jotserver.ot.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestOutfitType {

    private static final int LOOK = 1337;
    private static final String NAME = "SomeType";

    @Test
    public void hasSuppliedValues() {
        OutfitType type = new OutfitType(NAME, OutfitType.Type.MONSTER, LOOK, false);
        assertEquals(NAME, type.getName());
        assertEquals(OutfitType.Type.MONSTER, type.getType());
        assertEquals(LOOK, type.getLook());
        assertFalse(type.isEnabled());
    }

}
