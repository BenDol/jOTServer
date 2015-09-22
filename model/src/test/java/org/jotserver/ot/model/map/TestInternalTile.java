package org.jotserver.ot.model.map;


import static org.jotserver.ot.model.item.TestItemProvider.getGroundItem;
import static org.jotserver.ot.model.item.TestItemProvider.getRegularItem;
import static org.jotserver.ot.model.item.TestItemProvider.getTopItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.creature.TestableCreature;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Position;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestInternalTile {

    private Mockery context;
    private Tile tile;
    private TestableMap map;
    private Position pos;
    private InternalTile internal;

    @Before
    public void setUp() throws Exception {
        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};

        tile = context.mock(Tile.class);
        map = new TestableMap();
        pos = new Position(100, 100, 7);
        map.setTile(pos, tile);

        internal = new InternalTile(tile, map, pos);
    }

    @Test
    public void hasTile() {
        assertSame(tile, internal.getTile());
    }

    @Test
    public void getTileInDirection() {
        Position p2 = pos.add(Direction.NORTH);
        Tile t = new Tile(map, p2);
        map.setTile(p2, t);

        assertSame(t, internal.getTile(Direction.NORTH));
    }

    @Test
    public void newTileHasNoItems() {
        assertTrue(internal.getTopItems().isEmpty());
        assertTrue(internal.getDownItems().isEmpty());
    }

    @Test
    public void newTileHasNoCreatures() {
        assertTrue(internal.getCreatures().isEmpty());
    }

    @Test
    public void newTileHasNoGround() {
        assertNull(internal.getGround());
    }

    @Test
    public void isPlacedRequiresMapObject() {
        internal = new InternalTile(tile, null, pos);
        assertFalse(internal.isPlaced());
    }

    @Test
    public void isPlacedRequiresPosition() {
        internal = new InternalTile(tile, map, null);
        assertFalse(internal.isPlaced());
    }

    @Test
    public void isPlacedRequiresMapAndPosition() {
        internal = new InternalTile(tile, map, pos);
        assertTrue(internal.isPlaced());
    }

    @Test
    public void unknownItemHasIndexMinusOne() {
        assertEquals(-1, internal.getIndexOf(getRegularItem()));
    }

    @Test
    public void indexOfNullIsMinusOne() {
        internal.addItem(getRegularItem());
        assertEquals(-1, internal.getIndexOf(null));
    }



    /*
     * General item tests
     */

    @Test(expected = IllegalArgumentException.class)
    public void cannotRemoveUnknownItem() {
        internal.removeItem(getRegularItem());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddNullItem() {
        internal.addItem(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotRemoveNullItem() {
        internal.removeItem(null);
    }


    /*
     * Ground item tests
     */

    @Test
    public void canAddGroundItem() {
        Item g = getGroundItem();
        internal.addItem(g);
        assertSame(g, internal.getGround());
        assertTrue(internal.getTopItems().isEmpty());
        assertTrue(internal.getDownItems().isEmpty());
    }

    @Test
    public void groundItemHasStackPosZero() {
        Item g = getGroundItem();
        internal.addItem(g);
        internal.addItem(getRegularItem());
        assertEquals(0, internal.getIndexOf(g));
    }

    @Test(expected = IllegalArgumentException.class)
    public void canOnlyHaveOneGroundItem() {
        internal.addItem(getGroundItem());
        internal.addItem(getGroundItem());
    }

    @Test
    public void canRemoveGroundItem() {
        Item g = getGroundItem();
        internal.addItem(g);
        internal.removeItem(g);
        assertNull(internal.getGround());
    }

    @Test
    public void addingAndRemovingGroundItemSetsParent() {
        Item g = getGroundItem();
        internal.addItem(g);
        assertSame(tile, g.getParent());
        internal.removeItem(g);
        assertNull(g.getParent());
    }


    /*
     * "Down" items tests (regular items)
     */

    @Test
    public void canAddRegularItem() {
        Item item = getRegularItem();
        internal.addItem(item);
        assertFalse(internal.getDownItems().isEmpty());
        assertTrue(internal.getTopItems().isEmpty());
        assertTrue(internal.getDownItems().contains(item));
        assertEquals(1, internal.getDownItems().size());

        assertSame(tile, item.getParent());
    }

    @Test
    public void canRemoveRegularItem() {
        Item item = getRegularItem();
        internal.addItem(item);
        internal.removeItem(item);

        assertTrue(internal.getDownItems().isEmpty());
        assertNull(item.getParent());
    }

    @Test
    public void regularItemIndexIsAboveGround() {
        Item item = getRegularItem();
        internal.addItem(item);
        assertEquals(0, internal.getIndexOf(item));
        internal.addItem(getGroundItem());
        assertEquals(1, internal.getIndexOf(item));
    }

    // TODO: INVESTIGATE!
    @Ignore
    @Test
    public void regularItemsAreAddedOnTop() {
        Item i1 = getRegularItem();
        internal.addItem(i1);
        assertEquals(0, internal.getIndexOf(i1));
        Item i2 = getRegularItem();
        internal.addItem(i2);
        assertEquals(0, internal.getIndexOf(i1));
        assertEquals(1, internal.getIndexOf(i2));
    }


    /*
     * Top item tests (always-on-bottom items)
     */

    @Test
    public void canAddTopItem() {
        Item item = getTopItem(1);
        internal.addItem(item);
        assertFalse(internal.getTopItems().isEmpty());
        assertTrue(internal.getTopItems().contains(item));
        assertEquals(0, internal.getIndexOf(item));
    }

    @Test
    public void topItemsAreAddedOnTopOfGround() {
        Item item = getTopItem(1);
        internal.addItem(item);
        assertEquals(0, internal.getIndexOf(item));
        internal.addItem(getGroundItem());
        assertEquals(1, internal.getIndexOf(item));
    }

    @Test
    public void topItemsAreAddedBelowRegularItems() {
        Item item = getTopItem(1);
        internal.addItem(item);
        assertEquals(0, internal.getIndexOf(item));
        internal.addItem(getGroundItem());
        assertEquals(1, internal.getIndexOf(item));
        internal.addItem(getRegularItem());
        internal.addItem(getRegularItem());
        assertEquals(1, internal.getIndexOf(item));
    }

    @Test
    public void topItemsWithSameOrderAreAddedInFILOOrder() {
        Item i1 = getTopItem(1);
        Item i2 = getTopItem(1);
        Item i3 = getTopItem(1);
        internal.addItem(i1);
        internal.addItem(i2);
        internal.addItem(i3);
        assertEquals(0, internal.getIndexOf(i1));
        assertEquals(1, internal.getIndexOf(i2));
        assertEquals(2, internal.getIndexOf(i3));
    }

    @Test
    public void topItemsWithDifferentOrderAreAddedSortedByAscOrder() {
        Item i1 = getTopItem(1);
        Item i2 = getTopItem(2);
        Item i3 = getTopItem(3);
        internal.addItem(i2);
        internal.addItem(i3);
        internal.addItem(i1);
        assertEquals(0, internal.getIndexOf(i1));
        assertEquals(1, internal.getIndexOf(i2));
        assertEquals(2, internal.getIndexOf(i3));
    }

    @Test
    public void canRemoveTopItem() {
        Item i1 = getTopItem(1);
        Item i2 = getTopItem(2);
        Item i3 = getTopItem(3);
        internal.addItem(i2);
        internal.addItem(i3);
        internal.addItem(i1);
        internal.removeItem(i1);

        assertEquals(-1, internal.getIndexOf(i1));
        assertFalse(internal.getTopItems().contains(i1));

        assertEquals(0, internal.getIndexOf(i2));
        assertEquals(1, internal.getIndexOf(i3));
    }


    /*
     * Creature tests
     */

    @Test
    public void canAddCreature() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.addCreature(c);
        assertFalse(internal.getCreatures().isEmpty());
        assertTrue(internal.getCreatures().contains(c));
        assertEquals(0, internal.getIndexOf(c));
    }

    @Test
    public void canRemoveCreature() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.addCreature(c);
        internal.removeCreature(c);

        assertTrue(internal.getCreatures().isEmpty());
        assertEquals(-1, internal.getIndexOf(c));
    }

    @Test
    public void addingAndRemovingCreatureUpdatesCreatureParent() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.addCreature(c);
        assertSame(tile, c.getParent());
        internal.removeCreature(c);
        assertNull(c.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddCreatureThatBelongsToOtherCylinder() {
        Cylinder cylinder = context.mock(Cylinder.class);
        Creature c = new TestableCreature(1337, "SomeCreature", cylinder);
        internal.addCreature(c);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddCreatureToTileTwice() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.addCreature(c);
        internal.addCreature(c);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotRemoveUnknownCreature() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.removeCreature(c);
    }

    @Test
    public void creaturesArePlacedAboveGroundItem() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.addCreature(c);
        assertEquals(0, internal.getIndexOf(c));
        internal.addItem(getGroundItem());
        assertEquals(1, internal.getIndexOf(c));
    }

    @Test
    public void creaturesArePlacedAboveTopItems() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.addCreature(c);
        assertEquals(0, internal.getIndexOf(c));
        internal.addItem(getTopItem(1));
        assertEquals(1, internal.getIndexOf(c));
        internal.addItem(getTopItem(1));
        assertEquals(2, internal.getIndexOf(c));
    }

    // TODO: Is this really what we want? Seems fishy :S
    @Test
    public void creaturesArePlacedBelowRegularItems() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.addCreature(c);
        assertEquals(0, internal.getIndexOf(c));
        internal.addItem(getRegularItem());
        assertEquals(0, internal.getIndexOf(c));
        internal.addItem(getRegularItem());
        assertEquals(0, internal.getIndexOf(c));
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotAddNullCreature() {
        internal.addCreature(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotRemoveNullCreature() {
        internal.removeCreature(null);
    }


    @Test
    public void moveCreatureToTileMovesCreature() {
        Tile t2 = new Tile(map, pos);
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.addCreature(c);
        internal.moveCreature(c, t2);
        assertEquals(-1, internal.getIndexOf(c));
        assertEquals(0, t2.getInternal().getIndexOf(c));
        assertSame(t2, c.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotMoveUnknownCreatureToTile() {
        Tile t2 = new Tile(map, pos);
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.moveCreature(c, t2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotMoveNullCreatureToTile() {
        Tile t2 = new Tile(map, pos);
        internal.moveCreature(null, t2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotMoveCreatureToNull() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.addCreature(c);
        internal.moveCreature(c, (Tile)null);
    }


    @Test
    public void canMoveCreatureInDirection() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.addCreature(c);
        Tile t2 = new Tile(map, pos.add(Direction.NORTH));
        map.setTile(pos.add(Direction.NORTH), t2);
        internal.moveCreature(c, Direction.NORTH);

        assertEquals(-1, internal.getIndexOf(c));
        assertEquals(0, t2.getInternal().getIndexOf(c));
        assertSame(t2, c.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void canNotMoveUnknownCreatureInDirection() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        Tile t2 = new Tile(map, pos.add(Direction.NORTH));
        map.setTile(pos.add(Direction.NORTH), t2);
        internal.moveCreature(c, Direction.NORTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotMoveNullCreatureInDirection() {
        Tile t2 = new Tile(map, pos.add(Direction.NORTH));
        map.setTile(pos.add(Direction.NORTH), t2);
        internal.moveCreature(null, Direction.NORTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotMoveCreatureInDirectionWhereDestinationDoesNotExist() {
        Creature c = new TestableCreature(1337, "SomeCreature");
        internal.addCreature(c);
        internal.moveCreature(c, Direction.NORTH);
    }

}

