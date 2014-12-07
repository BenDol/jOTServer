package org.jotserver.ot.model.item;


import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class TestBaseItemTypeAccessor {

	private static final String NAME = "SomeItem";
	private static final int ID = 33;
	private static final int SIZE = 100;
	private BaseItemTypeAccessor items;
	private ItemType type;

	@Before
	public void setUp() throws Exception {
		items = new BaseItemTypeAccessor(SIZE);
		type = new ItemType();
		type.serverId = ID;
		type.name = NAME;
	}
	
	@Test
	public void undefinedTypeIsNull() {
		assertNull(items.getItemType(1));
	}
	
	@Test
	public void idOutsideRangeReturnsNull() {
		assertNull(items.getItemType(SIZE));
	}
	
	@Test
	public void negativeIdReturnsNull() {
		assertNull(items.getItemType(-1));
	}
	
	@Test
	public void unknownNameLookupReturnsNull() {
		assertNull(items.getItemType(NAME));
	}
	
	@Test
	public void creatingUnknownItemReturnsNull() {
		assertNull(items.createItem(ID));
	}
	
	@Test
	public void canAssignType() {
		items.setItemType(ID, type);
		assertSame(type, items.getItemType(ID));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void cannotAssignTypeOutsideRange() {
		int id = 1337;
		type.serverId = id;
		items.setItemType(id, type);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void cannotAssignTypeWithNegativeId() {
		int id = -1;
		type.serverId = id;
		items.setItemType(id, type);
	}
	
	@Test
	public void canLookupItemByName() {
		items.setItemType(ID, type);
		assertSame(type, items.getItemType(NAME));
	}
	
	@Test
	public void canCreateItemById() {
		items.setItemType(ID, type);
		Item item = items.createItem(ID);
		assertNotNull(item);
		assertSame(type, item.getType());
	}
	
	@Test
	public void canCreateItem() {
		Item item = items.createItem(type);
		assertNotNull(item);
		assertSame(type, item.getType());
	}
	
	@Test
	public void canCreateContainer() {
		type.group = ItemType.Group.CONTAINER;
		Item item = items.createItem(type);
		assertNotNull(item);
		assertTrue(item instanceof Container);
		assertSame(type, item.getType());
	}
	
	@Test
	public void canCreateStackable() {
		type.addAttribute(ItemAttribute.STACKABLE);
		Item item = items.createItem(type);
		assertNotNull(item);
		assertTrue(item instanceof Stackable);
		assertSame(type, item.getType());
	}

}
