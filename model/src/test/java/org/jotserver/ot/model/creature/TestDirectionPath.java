package org.jotserver.ot.model.creature;


import static org.junit.Assert.*;

import java.util.Arrays;

import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Position;
import org.junit.Before;
import org.junit.Test;

public class TestDirectionPath {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void createEmptyDirectionPath() {
        Position pos = new Position();
        DirectionPath path = new DirectionPath(pos);
        assertTrue(path.isEmpty());
        assertNull(path.getNextStep());
    }

    @Test
    public void hasPosition() {
        Position pos = new Position(123, 234, 7);
        DirectionPath path = new DirectionPath(pos);
        assertEquals(pos, path.getCurrentPosition());
    }

    @Test
    public void canAddDirections() {
        Position pos = new Position(123, 234, 7);
        DirectionPath path = new DirectionPath(pos);
        path.addStep(Direction.NORTH);
        assertEquals(Direction.NORTH, path.getNextStep());
    }

    @Test
    public void movingUpdatesPosition() {
        Position pos = new Position(123, 234, 7);
        DirectionPath path = new DirectionPath(pos);
        path.addStep(Direction.NORTH);
        pos = pos.add(path.getNextStep());
        assertEquals(pos, path.getCurrentPosition());
    }

    @Test
    public void canAddPath() {
        Position pos = new Position(123, 234, 7);
        DirectionPath path = new DirectionPath(pos,
                Arrays.asList(
                        Direction.NORTH, Direction.EAST, Direction.SOUTH
                ));

        assertEquals(Direction.NORTH, path.getNextStep());
        assertEquals(Direction.EAST, path.getNextStep());
        assertEquals(Direction.SOUTH, path.getNextStep());

    }

    @Test
    public void addInvertedOrder() {
        Position pos = new Position(123, 234, 7);
        DirectionPath path = new DirectionPath(pos);
        path.addStepFirst(Direction.NORTH);
        path.addStepFirst(Direction.SOUTH);
        assertEquals(Direction.SOUTH, path.getNextStep());
        assertEquals(Direction.NORTH, path.getNextStep());
    }

}
