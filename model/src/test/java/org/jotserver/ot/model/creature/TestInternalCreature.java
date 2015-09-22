package org.jotserver.ot.model.creature;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.Light;
import org.jotserver.ot.model.Outfit;
import org.jotserver.ot.model.util.Direction;
import org.junit.Before;
import org.junit.Test;

public class TestInternalCreature {

    private static final int ID = 1337;
    private static final String NAME = "SomeName";
    private Mockery context;
    private Cylinder cylinder;
    private Creature creature;
    private InternalCreature internal;

    @Before
    public void setUp() throws Exception {
        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};

        cylinder = context.mock(Cylinder.class);
        creature = new Creature() {
            public String getDescription() { return null; }
        };

        internal = new InternalCreature(creature, ID, NAME, cylinder);
    }

    @Test
    public void hasNameAndId() {
        InternalCreature internal = new InternalCreature(creature, ID, NAME, cylinder);
        assertEquals(ID, internal.getId());
        assertEquals(NAME, internal.getName());
    }

    @Test
    public void hasCreature() {
        assertSame(creature, internal.getCreature());
    }

    @Test
    public void turnToValidDirection() {
        Direction dir;

        internal.turn(dir = Direction.NORTH);
        assertEquals(dir, internal.getDirection());

        internal.turn(dir = Direction.SOUTH);
        assertEquals(dir, internal.getDirection());

        internal.turn(dir = Direction.EAST);
        assertEquals(dir, internal.getDirection());

        internal.turn(dir = Direction.WEST);
        assertEquals(dir, internal.getDirection());
    }

    @Test(expected = IllegalArgumentException.class)
    public void noneDirectionIsNotAccepted() {
        internal.turn(Direction.NONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void diagonalDirectionIsNotAccepted() {
        internal.turn(Direction.NORTHEAST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zDirectionIsNotAccepted() {
        internal.turn(Direction.UP);
    }

    @Test
    public void hasOutfit() {
        assertNotNull(internal.getOutfit());
        Outfit o = context.mock(Outfit.class);
        internal.setOutfit(o);
        assertSame(o, internal.getOutfit());
    }

    @Test
    public void hasLight() {
        Light light = new Light(1, 1);
        internal.setLight(light);
        assertSame(light, internal.getLight());
    }

    @Test
    public void initialLightLevelIsZero() {
        assertEquals(0, internal.getLight().getLevel());
    }

    @Test
    public void hasHealth() {
        assertFalse(internal.getHealth() == 0);
        assertFalse(internal.getMaxHealth() == 0);
        internal.setHealth(1);
        internal.setMaxHealth(2);
        assertEquals(1, internal.getHealth());
        assertEquals(2, internal.getMaxHealth());
    }

    @Test
    public void hasSpeed() {
        internal.setSpeed(1337);
        assertEquals(1337, internal.getSpeed());
    }

}
