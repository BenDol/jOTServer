package org.jotserver.ot.model.map;


import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.jotserver.ot.model.item.TestItemProvider.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.creature.Path;
import org.jotserver.ot.model.creature.TestableCreature;
import org.jotserver.ot.model.player.Camera;
import org.jotserver.ot.model.util.Interval3D;
import org.jotserver.ot.model.util.Position;
import org.junit.Before;
import org.junit.Test;

public class TestMap {

    private Position pos;
    private TestableMap map;

    @Before
    public void setUp() throws Exception {
        pos = new Position(100, 100, 7);
        map = new TestableMap(pos, 15);
    }

    @Test
    public void noSpectatorsOnEmptyMap() {
        Position r = new Position(5, 5, 0);
        Spectators<Creature>  s = map.getSpectators(Creature.class, pos.add(r.invert()), pos.add(r));
        Iterator<Creature> it = s.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    public void spectatorsWithCreatures() {
        ArrayList<Creature> creatures = new ArrayList<Creature>();
        Creature c;
        int i = 1;

        creatures.add(c = new TestableCreature(i++, String.valueOf(i)));
        map.getTile(pos.add(new Position(0, 0, 0))).executeAddCreature(c);

        creatures.add(c = new TestableCreature(i++, String.valueOf(i)));
        map.getTile(pos.add(new Position(2, 2, 0))).executeAddCreature(c);

        creatures.add(c = new TestableCreature(i++, String.valueOf(i)));
        map.getTile(pos.add(new Position(-1, -1, 0))).executeAddCreature(c);

        Position r = new Position(5, 5, 0);
        Spectators<Creature>  s = map.getSpectators(Creature.class, pos.add(r.invert()), pos.add(r));

        Iterator<Creature> it = s.iterator();

        assertTrue(creatures.contains(it.next()));
        assertTrue(creatures.contains(it.next()));
        assertTrue(creatures.contains(it.next()));
        assertFalse(it.hasNext());
    }

    @Test
    public void spectatorsWithCreaturesOnEdges() {
        ArrayList<Creature> creatures = new ArrayList<Creature>();
        Creature c;
        int i = 1;

        creatures.add(c = new TestableCreature(i++, String.valueOf(i)));
        map.getTile(pos.add(new Position(5, 5, 0))).executeAddCreature(c);

        creatures.add(c = new TestableCreature(i++, String.valueOf(i)));
        map.getTile(pos.add(new Position(-5, -5, 0))).executeAddCreature(c);

        c = new TestableCreature(i++, String.valueOf(i));
        map.getTile(pos.add(new Position(0, 6, 0))).executeAddCreature(c);

        Position r = new Position(5, 5, 0);
        Spectators<Creature>  s = map.getSpectators(Creature.class, pos.add(r.invert()), pos.add(r));

        Iterator<Creature> it = s.iterator();

        assertTrue(creatures.contains(it.next()));
        assertTrue(creatures.contains(it.next()));
        assertFalse(it.hasNext());
    }

    @Test
    public void cameraSpectatorsWithCreaturesOnEdges() {
        ArrayList<Creature> creatures = new ArrayList<Creature>();
        Creature c;
        int i = 1;

        creatures.add(c = new TestableCreature(i++, String.valueOf(i)));
        map.getTile(pos.add(new Position(11, 11, 0))).executeAddCreature(c);

        creatures.add(c = new TestableCreature(i++, String.valueOf(i)));
        map.getTile(pos.add(new Position(-11, -11, 0))).executeAddCreature(c);

        c = new TestableCreature(i++, String.valueOf(i));
        map.getTile(pos.add(new Position(0, 12, 0))).executeAddCreature(c);

        Spectators<Creature>  s = map.getSpectators(Creature.class, pos);

        Iterator<Creature> it = s.iterator();

        assertTrue(creatures.contains(it.next()));
        assertTrue(creatures.contains(it.next()));
        assertFalse(it.hasNext());
    }


    @Test
    public void spectatorRangeIsMaxViewport() {
        Interval3D iv = map.getSpectatorRange(pos);
        assertEquals(pos.getX() - Camera.viewportX, iv.getStartX());
        assertEquals(pos.getX() + Camera.viewportX, iv.getEndX());
        assertEquals(pos.getY() - Camera.viewportY, iv.getStartY());
        assertEquals(pos.getY() + Camera.viewportY, iv.getEndY());
    }

    @Test
    public void spectatorZRangeBelowGround() {
        Position pos = new Position(100, 100, 9);
        Interval3D iv = map.getSpectatorRange(pos);
        assertEquals(pos.getZ() - 2, iv.getStartZ());
        assertEquals(pos.getZ() + 2, iv.getEndZ());
    }

    @Test
    public void spectatorZRangeFarBelowGround() {
        Position pos = new Position(100, 100, 14);
        Interval3D iv = map.getSpectatorRange(pos);
        assertEquals(pos.getZ() - 2, iv.getStartZ());
        assertEquals(Map.MAX_Z - 1, iv.getEndZ());

        pos = new Position(100, 100, 15);
        iv = map.getSpectatorRange(pos);
        assertEquals(pos.getZ() - 2, iv.getStartZ());
        assertEquals(Map.MAX_Z - 1, iv.getEndZ());
    }

    @Test
    public void spectatorZRangeAboveGround() {
        Position pos = new Position(100, 100, 7);
        Interval3D iv = map.getSpectatorRange(pos);
        assertEquals(0, iv.getStartZ());
        assertEquals(pos.getZ() + 2, iv.getEndZ());

        pos = new Position(100, 100, 6);
        iv = map.getSpectatorRange(pos);
        assertEquals(0, iv.getStartZ());
        assertEquals(pos.getZ() + 2, iv.getEndZ());
    }

    @Test
    public void spectatorZRangeFarAboveGround() {
        Position pos = new Position(100, 100, 5);
        Interval3D iv = map.getSpectatorRange(pos);
        assertEquals(0, iv.getStartZ());
        assertEquals(7, iv.getEndZ());

        pos = new Position(100, 100, 4);
        iv = map.getSpectatorRange(pos);
        assertEquals(0, iv.getStartZ());
        assertEquals(7, iv.getEndZ());

        pos = new Position(100, 100, 0);
        iv = map.getSpectatorRange(pos);
        assertEquals(0, iv.getStartZ());
        assertEquals(7, iv.getEndZ());
    }

    @Test
    public void simplePathfinding() {
        map.fillWithItemsOfType(getGroundItem().getType());

        Creature creature = new TestableCreature(1, "1");
        Position destPos = pos.add(new Position(5, 5, 0));

        map.getTile(pos).executeAddCreature(creature);

        Path path = map.getPath(pos, destPos);
        assertNotNull(path);
        while(!path.isEmpty()) {
            creature.getTile().executeMoveCreature(creature, path.getNextStep());
        }
        assertEquals(destPos, creature.getPosition());
    }

    @Test
    public void cannotFindPathBetweenFloors() {
        map.fillWithItemsOfType(getGroundItem().getType());

        Creature creature = new TestableCreature(1, "1");
        Position destPos = pos.add(new Position(0, 0, 1));

        map.getTile(pos).executeAddCreature(creature);

        Path path = map.getPath(pos, destPos);
        assertNull(path);
    }

    @Test
    public void canFindPathToTargetWithRadius() {
        map.fillWithItemsOfType(getGroundItem().getType());

        Creature creature = new TestableCreature(1, "1");
        Position destPos = pos.add(new Position(5, 5, 0));

        map.getTile(pos).executeAddCreature(creature);

        Path path = map.getPath(pos, destPos, 2);
        assertNotNull(path);
        while(!path.isEmpty()) {
            creature.getTile().executeMoveCreature(creature, path.getNextStep());
        }
        assertEquals(2, creature.getPosition().distanceTo(destPos));
    }

}
