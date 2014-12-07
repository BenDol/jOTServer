package org.jotserver.ot.model.player;


import static org.jotserver.ot.model.item.ItemAttribute.PICKUPABLE;
import static org.jotserver.ot.model.item.TestItemProvider.*;
import static org.junit.Assert.*;

import java.util.EnumSet;
import java.util.Iterator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.creature.TestableCreature;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.ItemType;
import org.jotserver.ot.model.map.Spectators;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.ItemLocation;
import org.junit.Before;
import org.junit.Test;

public class TestInventory {

	private Inventory inventory;
	private Mockery context;
	private Player player;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		player = context.mock(Player.class);
		inventory = new Inventory(player);
	}
	
	@Test
	public void hasPlayer() {
		assertSame(player, inventory.getPlayer());
	}
	
	@Test
	public void hasInternal() {
		assertNotNull(inventory.getInternal());
	}
	
	@Test
	public void contentsSpectatorsIsPlayer() {
		Spectators<Creature> s = inventory.getContentsSpectators(Creature.class);
		Iterator<Creature> it = s.iterator();
		assertTrue(it.hasNext());
		assertSame(player, it.next());
		assertFalse(it.hasNext());
	}
	
	@Test
	public void contentsSpectatorsOfNonPlayerTypeIsEmpty() {
		Spectators<TestableCreature> s = inventory.getContentsSpectators(TestableCreature.class);
		Iterator<TestableCreature> it = s.iterator();
		assertFalse(it.hasNext());
	}
	
	@Test
	public void isOnlyVisibleToTheOwner() {
		assertTrue(inventory.isVisibleTo(player));
		assertFalse(inventory.isVisibleTo(new TestableCreature(1, "1")));
	}
	
	@Test
	public void parentIsPlayerParent() {
		final Cylinder parent = context.mock(Cylinder.class);
		context.checking(new Expectations() {{
			allowing(player).getParent(); will(returnValue(parent));
		}});
		assertSame(parent, inventory.getParent());
	}
	
	@Test
	public void tileIsPlayerTile() {
		final Tile tile = context.mock(Tile.class);
		context.checking(new Expectations() {{
			allowing(player).getTile(); will(returnValue(tile));
		}});
		assertSame(tile, inventory.getTile());
	}
	
	@Test
	public void placementIsPlayerPlacement() {
		context.checking(new Expectations() {{
			allowing(player).isPlaced(); will(returnValue(true));
		}});
		assertTrue(inventory.isPlaced());
	}
	
	@Test
	public void locationOfItemFindsItem() {
		Item item = getRegularItem();
		inventory.executeAddItem(InventorySlot.HEAD, item);
		ItemLocation loc = inventory.getLocationOf(item);
		assertNotNull(loc);
		assertSame(item, loc.get());
		assertEquals(InventorySlot.HEAD.ordinal(), loc.getIndex());
		assertSame(inventory, loc.getCylinder());
	}
	
	@Test
	public void locationOfNonItemThingIsNull() {
		TestableCreature creature = new TestableCreature(1, "1");
		ItemLocation loc = inventory.getLocationOf(creature);
		assertNull(loc);
	}
	
	@Test
	public void locationOfUnknownItemIsNull() {
		Item item = getRegularItem();
		ItemLocation loc = inventory.getLocationOf(item);
		assertNull(loc);
	}
	
	
	
	/*
	 * Action method tests.
	 */
	
	@Test
	public void queryAddItemRequiresPickupableItem() {
		Item item = createItem(new ItemType()); // Not pickupable
		assertEquals(ErrorType.CANNOTPICKUP, inventory.queryAddItem(InventorySlot.LEFT, item));
	}
	
	@Test
	public void queryAddItemRequiresFit() {
		Item item = createItem(new ItemType() {{
			attributes = EnumSet.of(PICKUPABLE);
			slots = EnumSet.complementOf(EnumSet.of(InventorySlot.HEAD));
		}});
		assertEquals(ErrorType.DOESNOTFIT, inventory.queryAddItem(InventorySlot.HEAD, item));
	}
	
	@Test
	public void queryAddItemRequiresEmptySlot() {
		inventory.executeAddItem(InventorySlot.LEFT, getRegularItem());
		Item item = createItem(new ItemType() {{
			attributes = EnumSet.of(PICKUPABLE);
		}});
		assertEquals(ErrorType.NOTENOUGHROOM, inventory.queryAddItem(InventorySlot.LEFT, item));
	}
	
	@Test
	public void queryAddItemValid() {
		Item item = createItem(new ItemType() {{
			attributes = EnumSet.of(PICKUPABLE);
		}});
		assertEquals(ErrorType.NONE, inventory.queryAddItem(InventorySlot.LEFT, item));
	}
	
	@Test
	public void executeAddItemAddsItem() {
		Item item = getRegularItem();
		inventory.executeAddItem(InventorySlot.ARMOR, item);
		assertEquals(item, inventory.getItem(InventorySlot.ARMOR));
	}
	
	@Test
	public void queryRemoveItemRequiresKnownItem() {
		Item item = getRegularItem();
		assertEquals(ErrorType.NOTPOSSIBLE, inventory.queryRemoveItem(item));
	}
	
	@Test
	public void queryRemoveItemValid() {
		Item item = getRegularItem();
		inventory.executeAddItem(InventorySlot.ARMOR, item);
		assertEquals(ErrorType.NONE, inventory.queryRemoveItem(item));
	}
	
	@Test
	public void queryRemoveItemBySlotRequiresItem() {
		assertEquals(ErrorType.NOTPOSSIBLE, inventory.queryRemoveItem(InventorySlot.AMMO));
	}
	
	@Test
	public void executeRemoveItemRemovesItem() {
		Item item = getRegularItem();
		inventory.executeAddItem(InventorySlot.ARMOR, item);
		inventory.executeRemoveItem(item);
		assertTrue(inventory.isEmpty(InventorySlot.ARMOR));
	}
	
	@Test
	public void hasItemFindsItem() {
		Item item = getRegularItem();
		assertFalse(inventory.hasItem(item));
		inventory.executeAddItem(InventorySlot.ARMOR, item);
		assertTrue(inventory.hasItem(item));
	}
	
	@Test
	public void providesSlotLocation() {
		Item item = getRegularItem();
		inventory.executeAddItem(InventorySlot.HEAD, item);
		ItemLocation loc = inventory.getSlotLocation(InventorySlot.HEAD);
		assertNotNull(loc);
		assertSame(item, loc.get());
	}

}
