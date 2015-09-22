package org.jotserver.ot.model.map;

import static org.jotserver.ot.model.item.ItemAttribute.*;
import static org.jotserver.ot.model.item.TestItemProvider.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.EnumSet;

import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.creature.TestableCreature;
import org.jotserver.ot.model.item.FluidType;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.ItemType;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Location;
import org.jotserver.ot.model.util.Position;
import org.junit.Before;
import org.junit.Test;

public class TestTile {

    private TestableMap map;
    private Position pos;
    private Tile tile;
    private TestableMap pmap;

    @Before
    public void setUp() throws Exception {
        map = new TestableMap();
        pos = new Position(100, 100, 7);
        tile = new Tile(map, pos);
        pmap = new TestableMap(new Position(100, 100, 7), 5, 5, 9);
    }

    @Test
    public void hasInternalTile() {
        assertNotNull(tile.getInternal());
    }

    @Test
    public void hasSpecifiedPosition() {
        assertEquals(pos, tile.getPosition());
    }

    @Test
    public void hasSpecifiedMap() {
        assertEquals(map, tile.getMap());
    }


    /*
     * getThing and getTopItem tests.
     */

    @Test
    public void getThingByIndexFindsGroundItem() {
        Item g = getGroundItem();
        tile.executeAddItem(g);
        assertSame(g, tile.getThing(0));

        tile.executeAddItem(getRegularItem());
        assertSame(g, tile.getThing(0));

        tile.executeAddItem(getTopItem(1));
        assertSame(g, tile.getThing(0));
    }

    @Test
    public void getThingByIndexFindsTopItem() {
        Item i = getTopItem(1);
        tile.executeAddItem(i);
        assertSame(i, tile.getThing(0));

        tile.executeAddItem(getGroundItem());
        assertSame(i, tile.getThing(1));

        tile.executeAddItem(getRegularItem());
        assertSame(i, tile.getThing(1));

        tile.executeAddItem(getTopItem(1));
        assertSame(i, tile.getThing(1));

        tile.executeAddItem(getTopItem(2));
        assertSame(i, tile.getThing(1));

        tile.executeAddItem(getTopItem(0));
        assertSame(i, tile.getThing(2));
    }

    @Test
    public void getThingByIndexFindsRegularItem() {
        Item i = getRegularItem();
        tile.executeAddItem(i);
        assertSame(i, tile.getThing(0));

        tile.executeAddItem(getGroundItem());
        assertSame(i, tile.getThing(1));

        tile.executeAddItem(getRegularItem()); // TODO: Check the order these are added in!
        assertSame(i, tile.getThing(2));

        tile.executeAddItem(getTopItem(1));
        assertSame(i, tile.getThing(3));

        tile.executeAddItem(getTopItem(2));
        assertSame(i, tile.getThing(4));

        tile.executeAddItem(getTopItem(0));
        assertSame(i, tile.getThing(5));
    }

    @Test
    public void getThingByIndexFindsNullForUnknownIndex() {
        tile.executeAddItem(getGroundItem());
        assertNull(tile.getThing(1));
        assertNull(tile.getThing(137));
    }

    @Test
    public void getThingByIndexFindsNullForNegativeIndex() {
        tile.executeAddItem(getGroundItem());
        assertNull(tile.getThing(-1));
    }

    @Test
    public void getTopItemFindsTheTopmostItem() {
        Item i = getGroundItem();
        tile.executeAddItem(i);
        assertSame(i, tile.getTopItem());

        tile.executeAddItem(i = getTopItem(2));
        assertSame(i, tile.getTopItem());

        tile.executeAddItem(i = getTopItem(3));
        assertSame(i, tile.getTopItem());

        tile.executeAddItem(getTopItem(1));
        assertSame(i, tile.getTopItem());

        tile.executeAddItem(i = getRegularItem());
        assertSame(i, tile.getTopItem());

        tile.executeAddItem(i = getRegularItem());
        assertSame(i, tile.getTopItem());

    }

    @Test
    public void getItemsReturnsAllItems() {
        ArrayList<Item> items = new ArrayList<Item>();

        Item i;

        items.add(i = getGroundItem());
        tile.executeAddItem(i);
        assertTrue(tile.getItems().containsAll(items));
        assertEquals(items.size(), tile.getItems().size());

        items.add(i = getRegularItem());
        tile.executeAddItem(i);
        assertTrue(tile.getItems().containsAll(items));
        assertEquals(items.size(), tile.getItems().size());

        items.add(i = getRegularItem());
        tile.executeAddItem(i);
        assertTrue(tile.getItems().containsAll(items));
        assertEquals(items.size(), tile.getItems().size());

        items.add(i = getTopItem(2));
        tile.executeAddItem(i);
        assertTrue(tile.getItems().containsAll(items));
        assertEquals(items.size(), tile.getItems().size());

        items.add(i = getTopItem(1));
        tile.executeAddItem(i);
        assertTrue(tile.getItems().containsAll(items));
        assertEquals(items.size(), tile.getItems().size());

    }

    @Test
    public void tileIsAlwaysTopParent() {
        assertNull(tile.getParent());
    }

    @Test
    public void getTileReturnsSelf() {
        assertSame(tile, tile.getTile());
    }

    @Test
    public void tileFindsSurroundingTiles() {
        Position p = pos.add(Direction.EAST);
        Tile t = new Tile(map, p);
        map.setTile(p, t);
        assertSame(t, tile.getTile(Direction.EAST));
    }

    @Test
    public void doesFloorChangeConsultsItems() {
        tile.executeAddItem(getFloorChangingItem(Direction.NONE));
        assertFalse(tile.doesFloorChange());
        tile.executeAddItem(getFloorChangingItem(Direction.NORTH));
        assertTrue(tile.doesFloorChange());
    }


    /*
     * Floor change direction (getFloorChangeDirection) tests
     */

    @Test
    public void getFloorChangeDirectionReportsCorrectDirectionWithoutItems() {
        assertEquals(Direction.NONE, tile.getFloorChangeDirection());
    }

    @Test
    public void floorChangeDirectionWithSimpleDirection() {
        tile.executeAddItem(getFloorChangingItem(Direction.NORTH));
        assertEquals(Direction.NORTH, tile.getFloorChangeDirection());
    }

    @Test
    public void floorChangeDirectionWithComplexDirection() {
        tile.executeAddItem(getFloorChangingItem(Direction.NORTHWEST));
        assertEquals(Direction.NORTHWEST, tile.getFloorChangeDirection());
    }



    /*
     * Destination (getDestination) tests.
     */

    @Test
    public void destinationWithoutItemsIsSelf() {
        assertSame(tile, tile.getDestination());
    }

    @Test
    public void destinationWithNonChangingItems() {
        tile.executeAddItem(getGroundItem());
        tile.executeAddItem(getRegularItem());
        assertSame(tile, tile.getDestination());
    }

    @Test
    public void destinationWithSimpleChangingItems() {
        Position p = pos.add(Direction.UP).add(Direction.SOUTH);

        Tile t1 = pmap.getTile(pos);

        t1.executeAddItem(getFloorChangingItem(Direction.SOUTH));
        assertSame(pmap.getTile(p), t1.getDestination());
    }

    @Test
    public void destinationWithComplexChangingItems() {
        Position p = pos.add(Direction.UP).add(Direction.NORTHEAST);

        Tile t1 = pmap.getTile(pos);

        t1.executeAddItem(getFloorChangingItem(Direction.NORTHEAST));
        assertSame(pmap.getTile(p), t1.getDestination());
    }

    @Test
    public void destinationWithDownChangingItem() {
        Position p = pos.add(Direction.DOWN);

        Tile t1 = pmap.getTile(pos);
        t1.executeAddItem(getFloorChangeDownItem());
        assertSame(pmap.getTile(p), t1.getDestination());
    }

    @Test
    public void destinationWithDownAndSimpleChangingItem() {
        Position p = pos.add(Direction.DOWN);

        Tile t1 = pmap.getTile(pos);
        t1.executeAddItem(getFloorChangeDownItem());

        Tile t2 = pmap.getTile(p);
        t2.executeAddItem(getFloorChangingItem(Direction.SOUTH));
        p = p.add(Direction.NORTH);
        assertSame(pmap.getTile(p), t1.getDestination());
    }

    @Test
    public void destinationWithDownAndComplexChangingItem() {
        Position p = pos.add(Direction.DOWN);

        Tile t1 = pmap.getTile(pos);
        t1.executeAddItem(getFloorChangeDownItem());

        Tile t2 = pmap.getTile(p);
        t2.executeAddItem(getFloorChangingItem(Direction.NORTHEAST));
        p = p.add(Direction.SOUTHWEST);
        assertSame(pmap.getTile(p), t1.getDestination());
    }

    @Test
    public void missingDestinationReturnsSelf() {
        Position p = pos.add(Direction.DOWN);
        pmap.setTile(p, null);

        Tile t1 = pmap.getTile(pos);
        t1.executeAddItem(getFloorChangeDownItem());

        assertSame(t1, t1.getDestination());
    }



    /*
     * Location tests. (getLocationOf)
     */

    @Test
    public void providesLocationForThings() {
        Item g = getGroundItem();
        Item i = getRegularItem();
        Item t = getTopItem(2);
        tile.executeAddItem(g);
        tile.executeAddItem(i);
        tile.executeAddItem(t);

        Location loc = tile.getLocationOf(g);
        assertNotNull(loc); assertSame(g, loc.get()); assertEquals(0, loc.getIndex());

        loc = tile.getLocationOf(i);
        assertNotNull(loc); assertSame(i, loc.get()); assertEquals(2, loc.getIndex());

        loc = tile.getLocationOf(t);
        assertNotNull(loc); assertSame(t, loc.get()); assertEquals(1, loc.getIndex());

        assertSame(tile, loc.getCylinder());

    }

    @Test
    public void nullLocationForUnknownThings() {
        Item i = getRegularItem();
        Location loc = tile.getLocationOf(i);
        assertNull(loc);
    }

    @Test
    public void locationByStack() {
        Item g = getGroundItem();
        Item i = getRegularItem();
        Item t = getTopItem(2);
        tile.executeAddItem(g);
        tile.executeAddItem(i);
        tile.executeAddItem(t);

        Location loc = tile.getLocationOf(1);
        assertNotNull(loc); assertSame(t, loc.get()); assertEquals(1, loc.getIndex());
    }



    /*
     * Creature action methods tests
     */

    @Test
    public void queryAddCreatureWithExistingCreatures() {
        tile.executeAddItem(getGroundItem());
        tile.executeAddCreature(new TestableCreature(1, "1"));
        Creature c = new TestableCreature(2, "2");
        assertEquals(ErrorType.NOTPOSSIBLE, tile.queryAddCreature(c));
    }

    @Test
    public void queryAddCreatureRequiresGround() {
        Creature c = new TestableCreature(1, "1");
        assertEquals(ErrorType.NOTPOSSIBLE, tile.queryAddCreature(c));
        tile.executeAddItem(getGroundItem());
        assertEquals(ErrorType.NONE, tile.queryAddCreature(c));
    }

    @Test
    public void queryAddCreatureWithBlockSolidItem() {
        Creature c = new TestableCreature(1, "1");
        tile.executeAddItem(getGroundItem());
        tile.executeAddItem(getBlockSolidItem());
        assertEquals(ErrorType.NOTENOUGHROOM, tile.queryAddCreature(c));
    }

    @Test
    public void queryAddCreatureValid() {
        Creature c = new TestableCreature(1, "1");
        tile.executeAddItem(getGroundItem());
        assertEquals(ErrorType.NONE, tile.queryAddCreature(c));
    }

    @Test
    public void executeAddCreatureAddsCreature() {
        Creature creature = new TestableCreature(1, "1");
        tile.executeAddCreature(creature);
        assertTrue(tile.getCreatures().contains(creature));
    }

    @Test
    public void queryRemoveCreatureCannotRemoveUnknownCreature() {
        Creature creature = new TestableCreature(1, "1");
        assertEquals(ErrorType.NOTPOSSIBLE, tile.queryRemoveCreature(creature));
    }

    @Test
    public void queryRemoveCreatureValid() {
        Creature creature = new TestableCreature(1, "1");
        tile.executeAddCreature(creature);
        assertEquals(ErrorType.NONE, tile.queryRemoveCreature(creature));
    }

    @Test
    public void executeRemoveCreatureRemovesCreature() {
        Creature creature = new TestableCreature(1, "1");
        tile.executeAddCreature(creature);
        tile.executeRemoveCreature(creature);
        assertTrue(tile.getCreatures().isEmpty());
    }

    @Test
    public void queryMoveCreatureToNull() {
        Creature creature = new TestableCreature(1, "1");
        tile = pmap.getTile(pos);
        tile.executeAddCreature(creature);
        pmap.setTile(pos.add(Direction.NORTH), null);
        assertEquals(ErrorType.NOTPOSSIBLE, tile.queryMoveCreature(creature, Direction.NORTH));
    }

    @Test
    public void queryMoveCreatureToTileWithoutGround() {
        Creature creature = new TestableCreature(1, "1");
        tile = pmap.getTile(pos);
        tile.executeAddCreature(creature);
        assertEquals(ErrorType.NOTPOSSIBLE, tile.queryMoveCreature(creature, Direction.NORTH));
    }

    @Test
    public void queryMoveCreatureFromTileInvalid() {
        Creature creature = new TestableCreature(1, "1");
        tile = pmap.getTile(pos);
        Tile to = pmap.getTile(pos.add(Direction.NORTH));
        to.executeAddItem(getGroundItem());
        assertEquals(ErrorType.NOTPOSSIBLE, tile.queryMoveCreature(creature, Direction.NORTH));
    }

    @Test
    public void queryMoveCreatureTooFar() {
        pmap.fillWithItemsOfType(getGroundItem().getType());
        Creature creature = new TestableCreature(1, "1");
        tile = pmap.getTile(pos);
        for(int x = -2; x <= 2; x++) {
            for(int y = -2; y <= 2; y++) {
                for(int z = -2; z <= 2; z++) {
                    if(creature.getTile() != null) {
                        creature.getTile().executeRemoveCreature(creature);
                    }
                    Position p = pos.add(new Position(x, y, z));

                    if(p.equals(pos)) continue;

                    Tile toTile = pmap.getTile(p);
                    tile.executeAddCreature(creature);
                    boolean ok = pos.distanceTo(p) <= 1 && pos.getZDistanceTo(p) <= 1;
                    assertEquals(p.toString(), ok ? ErrorType.NONE : ErrorType.NOTPOSSIBLE, tile.queryMoveCreature(creature, toTile));
                }
            }
        }
    }

    @Test
    public void executeMoveCreatureMovesCreature() {
        Creature creature = new TestableCreature(1, "1");
        tile = pmap.getTile(pos);
        tile.executeAddCreature(creature);
        tile.executeMoveCreature(creature, Direction.WEST);
        Position p = pos.add(Direction.WEST);
        assertTrue(pmap.getTile(p).getCreatures().contains(creature));
        assertTrue(tile.getCreatures().isEmpty());
    }

    @Test
    public void executeMoveCreatureFloorChanging() {
        Creature creature = new TestableCreature(1, "1");
        tile = pmap.getTile(pos);
        tile.executeAddCreature(creature);
        Position p = pos.add(Direction.SOUTH);
        pmap.getTile(p).executeAddItem(getFloorChangeDownItem());
        p = p.add(Direction.DOWN);
        pmap.getTile(p).executeAddItem(getFloorChangingItem(Direction.NORTHWEST));
        p = p.add(Direction.SOUTHEAST);
        tile.executeMoveCreature(creature, Direction.SOUTH);
        assertTrue(pmap.getTile(p).getCreatures().contains(creature));
    }

    @Test
    public void executeMoveCreatureTurnsCreature() {
        Creature creature = new TestableCreature(1, "1");
        tile = pmap.getTile(pos);
        tile.executeAddCreature(creature);
        creature.turn(Direction.SOUTH);
        tile.executeMoveCreature(creature, Direction.WEST);
        assertEquals(Direction.WEST, creature.getDirection());
    }



    /*
     * Item action methods tests.
     */

    @Test
    public void queryAddBlockSolidItemWithCreature() {
        tile.executeAddCreature(new TestableCreature(1, "1"));
        Item bs = getBlockSolidItem();
        assertEquals(ErrorType.NOTPOSSIBLE, tile.queryAddItem(bs));
    }

    @Test
    public void queryAddNonPickupableOnBlockSolid() {
        ItemType t1 = new ItemType() {{
            attributes = EnumSet.of(HASHEIGHT, BLOCKSOLID);
        }};
        tile.executeAddItem(createItem(t1));
        ItemType t2 = new ItemType(); // Not pickupable
        Item i = createItem(t2);
        assertEquals(ErrorType.NOTPOSSIBLE, tile.queryAddItem(i));

        t2 = new ItemType() {{ attributes = EnumSet.of(PICKUPABLE); }};
        i = createItem(t2);
        assertEquals(ErrorType.NONE, tile.queryAddItem(i));
    }

    @Test
    public void queryAddPickupableOnBlockSolid() {
        ItemType t1 = new ItemType() {{
            attributes = EnumSet.of(PICKUPABLE, BLOCKSOLID);
        }};
        tile.executeAddItem(createItem(t1));
        ItemType t2 = new ItemType() {{ attributes = EnumSet.of(PICKUPABLE); }};
        Item i = createItem(t2);
        assertEquals(ErrorType.NOTPOSSIBLE, tile.queryAddItem(i));
    }

    @Test
    public void executeAddItemAddsItemWithoutChanging() {
        Item i = getRegularItem();
        tile.executeAddItem(i);
        assertTrue(tile.getItems().contains(i));
    }

    @Test
    public void executeAddItemAddsItemWithChanging() {
        tile = pmap.getTile(pos);
        tile.executeAddItem(getFloorChangeDownItem());
        Position p = pos.add(Direction.DOWN);
        Item i = getRegularItem();
        tile.executeAddItem(i);
        assertTrue(pmap.getTile(p).getItems().contains(i));
        assertFalse(tile.getItems().contains(i));
    }

    @Test
    public void executeAddItemRemovesExistingSplashItems() {
        Item s1 = getSplashItem(FluidType.BEER);
        tile.executeAddItem(s1);
        assertTrue(tile.getItems().contains(s1));

        Item s2 = getSplashItem(FluidType.BLOOD);
        tile.executeAddItem(s2);
        assertFalse(tile.getItems().contains(s1));
        assertTrue(tile.getItems().contains(s2));
    }

    @Test
    public void queryRemoveItemCannotRemoveUnknownItem() {
        Item i = getRegularItem();
        assertEquals(ErrorType.NOTPOSSIBLE, tile.queryRemoveItem(i));
    }

    @Test
    public void queryRemoveItemValid() {
        Item i = getRegularItem();
        tile.executeAddItem(i);
        assertEquals(ErrorType.NONE, tile.queryRemoveItem(i));
    }

    @Test
    public void executeRemoveItemRemovesItem() {
        Item i = getRegularItem();
        tile.executeAddItem(i);
        tile.executeRemoveItem(i);
        assertFalse(tile.getItems().contains(i));
        assertTrue(tile.getItems().isEmpty());
    }

}
