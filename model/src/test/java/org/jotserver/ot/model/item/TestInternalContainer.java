package org.jotserver.ot.model.item;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.util.ItemLocation;
import org.junit.Before;
import org.junit.Test;

public class TestInternalContainer {
	
	private Mockery context;
	private Container container;
	private ItemType type;
	private Cylinder cylinder;
	private InternalContainer internal;
	private Item mockedItem;
	private Item item;
	private Creature c;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		mockedItem = context.mock(Item.class);
		type = new ItemType();
		item = new Item(type);
		container = context.mock(Container.class);
		cylinder = context.mock(Cylinder.class);
		internal = new InternalContainer(container, cylinder);
		
		c = context.mock(Creature.class);
	}
	
	@Test
	public void hasContainer() {
		assertSame(container, internal.getContainer());
	}
	
	@Test
	public void newContainerHasNoItems() {
		assertEquals(0, internal.getItemCount());
		assertTrue(internal.getItems().isEmpty());
		assertNull(internal.getItem(0));
	}
	
	@Test
	public void canAddItem() {
		internal.addItem(item);
		assertEquals(1, internal.getItemCount());
		assertFalse(internal.getItems().isEmpty());
		assertTrue(internal.getItems().contains(item));
	}
	
	@Test
	public void updatesItemParentsWhenAdded() {
		internal.addItem(item);
		assertSame(container, item.getParent());
	}
	
	@Test
	public void addedItemsAreAddedFirst() {
		internal.addItem(item);
		assertEquals(0, internal.getSlot(item));
		Item item2 = new Item(type);
		internal.addItem(item2);
		assertEquals(0, internal.getSlot(item2));
		assertEquals(1, internal.getSlot(item));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void cannotAddNull() {
		internal.addItem(null);
	}
	
	@Test
	public void canRemoveItem() {
		internal.addItem(item);
		internal.removeItem(0);
		assertEquals(0, internal.getItemCount());
		assertTrue(internal.getItems().isEmpty());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void cannotRemoveNonexistingItem() {
		internal.removeItem(0);
	}
	
	@Test
	public void canGetItemBySlot() {
		internal.addItem(item);
		assertSame(item, internal.getItem(0));
	}
	
	@Test
	public void getItemSlotForUnknownItem() {
		assertEquals(-1, internal.getSlot(item));
	}
	
	@Test
	public void getItemBySlotWithOverflowReturnsNull() {
		internal.addItem(item);
		assertNull(internal.getItem(1));
	}
	
	@Test
	public void removingItemsRestoresItemParentToNull() {
		internal.addItem(item);
		internal.removeItem(0);
		assertNull(item.getParent());
	}
	
	@Test
	public void newContainerHasNoSpectators() {
		assertTrue(internal.getSpectators().isEmpty());
	}
	
	@Test
	public void creatureAddedAsSpectatorWhenOpened() {
		internal.open(c);
		assertTrue(internal.isSpectator(c));
		assertTrue(internal.getSpectators().contains(c));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullCreatureCannotOpen() {
		internal.open(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void creatureCanOnlyOpenOnce() {
		internal.open(c);
		internal.open(c);
	}
	
	@Test
	public void canCloseContainer() {
		internal.open(c);
		internal.close(c);
		assertFalse(internal.isSpectator(c));
		assertTrue(internal.getSpectators().isEmpty());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullCannotCloseContainer() {
		internal.close(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void unknownCreatureCannotCloseContainer() {
		internal.close(c);
	}
	
	
	@Test
	public void locationProvidesContainer() {
		internal.addItem(item);
		ItemLocation loc = internal.getLocationOf(item);
		assertSame(container, loc.getCylinder());
	}
	
	@Test
	public void locationProvidesItem() {
		internal.addItem(item);
		ItemLocation loc = internal.getLocationOf(item);
		assertSame(item, loc.get());
	}
	
	@Test
	public void locationProvidesIndex() {
		internal.addItem(item);
		ItemLocation loc = internal.getLocationOf(item);
		assertSame(internal.getSlot(item), loc.getIndex());
	}
	
	@Test
	public void locationUpdatesWhenAddingItems() {
		internal.addItem(item);
		ItemLocation loc = internal.getLocationOf(item);
		internal.addItem(new Item(type));
		assertSame(item, loc.get());
		assertEquals(1, loc.getIndex());
	}
	
	@Test
	public void locationUpdatesWhenRemovingItems() {
		internal.addItem(item);
		Item item2 = new Item(type);
		internal.addItem(item2);
		ItemLocation loc = internal.getLocationOf(item);
		internal.removeItem(0);
		assertSame(item, loc.get());
		assertEquals(0, loc.getIndex());
	}
	
	@Test
	public void locationBecomesInvalidWhenItemIsRemoved() {
		Item item2 = new Item(type);
		internal.addItem(item2);
		internal.addItem(item);
		ItemLocation loc = internal.getLocationOf(item);
		internal.removeItem(0);
		assertNull(loc.get());
		assertEquals(-1, loc.getIndex());
	}
	
	@Test
	public void locationOfUnknownItemIsNull() {
		ItemLocation loc = internal.getLocationOf(item);
		assertNull(loc);
	}
	
	@Test
	public void canGetLocationBySlot() {
		internal.addItem(item);
		ItemLocation loc = internal.getSlotLocation(0);
		assertSame(item, loc.get());
		assertEquals(0, loc.getIndex());
	}
	
}
