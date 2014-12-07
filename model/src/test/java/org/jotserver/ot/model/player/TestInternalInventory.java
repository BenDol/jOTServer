package org.jotserver.ot.model.player;


import static org.jotserver.ot.model.item.TestItemProvider.getRegularItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.util.ItemLocation;
import org.junit.Before;
import org.junit.Test;

public class TestInternalInventory {

	private InternalInventory internal;
	private Mockery context;
	private Inventory inventory;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		inventory = context.mock(Inventory.class);
		internal = new InternalInventory(inventory);
	}
	
	@Test
	public void hasInventory() {
		assertSame(inventory, internal.getInventory());
	}
	
	@Test
	public void canPutItemInSlot() {
		Item item = getRegularItem();
		internal.setItem(InventorySlot.RIGHT, item);
		assertEquals(item, internal.getItem(InventorySlot.RIGHT));
	}
	
	@Test
	public void canReplaceItemInSlot() {
		Item item = getRegularItem();
		Item item2 = getRegularItem();
		internal.setItem(InventorySlot.LEGS, item);
		internal.setItem(InventorySlot.LEGS, item2);
		assertEquals(item2, internal.getItem(InventorySlot.LEGS));
	}
	
	@Test
	public void updatesParentOfAddedItems() {
		Item item = getRegularItem();
		internal.setItem(InventorySlot.LEFT, item);
		assertEquals(inventory, item.getParent());
	}
	
	@Test
	public void updatesParentOfReplacedItems() {
		Item item = getRegularItem();
		Item item2 = getRegularItem();
		internal.setItem(InventorySlot.HEAD, item);
		internal.setItem(InventorySlot.HEAD, item2);
		assertNull(item.getParent());
	}
	
	@Test
	public void canRemoveItem() {
		Item item = getRegularItem();
		internal.setItem(InventorySlot.LEFT, item);
		internal.removeItem(item);
		assertNull(internal.getItem(InventorySlot.LEFT));
		assertNull(item.getParent());
	}
	
	@Test
	public void emptySlotIsEmpty() {
		assertTrue(internal.isEmpty(InventorySlot.NECKLACE));
	}
	
	@Test
	public void occupiedSlotIsNotEmpty() {
		Item item = getRegularItem();
		internal.setItem(InventorySlot.NECKLACE, item);
		assertFalse(internal.isEmpty(InventorySlot.NECKLACE));
	}
	
	@Test
	public void canRemoveItemBySlot() {
		Item item = getRegularItem();
		internal.setItem(InventorySlot.NECKLACE, item);
		internal.removeItem(InventorySlot.NECKLACE);
		assertTrue(internal.isEmpty(InventorySlot.NECKLACE));
		assertNull(internal.getItem(InventorySlot.NECKLACE));
		assertNull(item.getParent());
	}
	
	@Test
	public void canTestIfContainsItem() {
		Item item = getRegularItem();
		assertFalse(internal.hasItem(item));
		internal.setItem(InventorySlot.FEET, item);
		assertTrue(internal.hasItem(item));
	}
	
	@Test
	public void findsSlotOfItem() {
		Item item = getRegularItem();
		internal.setItem(InventorySlot.BACKPACK, item);
		assertEquals(InventorySlot.BACKPACK, internal.getSlot(item));
	}
	
	@Test
	public void unknownItemHasNullSlot() {
		Item item = getRegularItem();
		assertNull(internal.getSlot(item));
	}
	
	
	@Test
	public void slotLocationFindsItem() {
		Item item = getRegularItem();
		internal.setItem(InventorySlot.LEFT, item);
		ItemLocation loc = internal.getSlotLocation(InventorySlot.LEFT);
		assertSame(item, loc.get());
	}
	
	@Test
	public void slotLocationIsLockedToSlot() {
		ItemLocation loc = internal.getSlotLocation(InventorySlot.LEFT);
		Item item = getRegularItem();
		assertNull(loc.get());
		internal.setItem(InventorySlot.LEFT, item);
		assertSame(item, loc.get());
		internal.removeItem(InventorySlot.LEFT);
		assertNull(loc.get());
	}
	
	@Test
	public void slotLocationHasCorrectCylinder() {
		ItemLocation loc = internal.getSlotLocation(InventorySlot.LEFT);
		assertSame(inventory, loc.getCylinder());
	}
	
	@Test
	public void slotLocationHasCorrectIndex() {
		ItemLocation loc = internal.getSlotLocation(InventorySlot.LEFT);
		assertEquals(InventorySlot.LEFT.ordinal(), loc.getIndex());
	}

}
