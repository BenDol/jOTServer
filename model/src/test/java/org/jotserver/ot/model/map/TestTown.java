package org.jotserver.ot.model.map;


import static org.junit.Assert.*;

import org.jotserver.ot.model.util.Position;
import org.junit.Before;
import org.junit.Test;

public class TestTown {

    private static final int ID = 1337;
    private static final String NAME = "SomeTown";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void hasNameAndId() {
        Town t = new Town(ID, NAME);
        assertEquals(ID, t.getId());
        assertEquals(NAME, t.getName());
    }

    @Test
    public void hasPosition() {
        Town t = new Town();
        Position p = new Position(12, 34, 7);
        t.setPosition(p);
        assertEquals(p, t.getPosition());
    }

    @Test
    public void changeName() {
        Town t = new Town();
        t.setName(NAME);
        assertEquals(NAME, t.getName());
    }

}
