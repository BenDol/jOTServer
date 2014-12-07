package org.jotserver.ot.model.item;


import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.InventorySlot;
import org.jotserver.ot.model.player.Skills;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Position;
import org.junit.Before;
import org.junit.Test;

public class TestItem {

	private static final String NAME = "SomeName";
	private static final int ID = 1337;
	private Mockery context;
	private ItemType type;
	private ItemType mockedType;
	private Item item;
	private Item mocked;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		type = new ItemType();
		type.serverId = ID;
		type.name = NAME;
		mockedType = context.mock(ItemType.class);
		item = new Item(type);
		mocked = new Item(mockedType);
	}
	
	@Test
	public void providesInternalItem() {
		assertNotNull(item.getInternal());
	}
	
	@Test
	public void creatureCanUse() {
		final Creature c = context.mock(Creature.class);
		final Cylinder p = context.mock(Cylinder.class);
		final Tile t = context.mock(Tile.class);
		final Position p1 = new Position(100, 100, 7);
		final Position p2 = new Position(101, 101, 7);
		item.setParent(p);
		
		context.checking(new Expectations() {{
			allowing(p).getTile(); will(returnValue(t));
			allowing(t).getPosition(); will(returnValue(p1));
			allowing(c).getPosition(); will(returnValue(p2));
		}});
		
		assertEquals(ErrorType.NONE, item.queryUse(c));
	}
	
	@Test
	public void creatureCanOnlyUseWithinOneSqm() {
		final Creature c = context.mock(Creature.class);
		final Cylinder p = context.mock(Cylinder.class);
		final Tile t = context.mock(Tile.class);
		final Position p1 = new Position(100, 100, 7);
		final Position p2 = new Position(102, 101, 7);
		item.setParent(p);
		
		context.checking(new Expectations() {{
			allowing(p).getTile(); will(returnValue(t));
			allowing(t).getPosition(); will(returnValue(p1));
			allowing(c).getPosition(); will(returnValue(p2));
		}});
		
		assertEquals(ErrorType.TOOFARAWAY, item.queryUse(c));
	}
	
	@Test
	public void identifiesNonFloorchanging() {
		type.setAttribute(ItemAttribute.FLOORCHANGEDOWN, false);
		type.setAttribute(ItemAttribute.FLOORCHANGENORTH, false);
		type.setAttribute(ItemAttribute.FLOORCHANGESOUTH, false);
		type.setAttribute(ItemAttribute.FLOORCHANGEEAST, false);
		type.setAttribute(ItemAttribute.FLOORCHANGEWEST, false);
		assertEquals(Direction.NONE, item.getFloorChangeDirection());
	}
	
	@Test
	public void identifiesSimpleFloorchanging() {
		type.setAttribute(ItemAttribute.FLOORCHANGENORTH, true);
		type.setAttribute(ItemAttribute.FLOORCHANGESOUTH, false);
		type.setAttribute(ItemAttribute.FLOORCHANGEEAST, false);
		type.setAttribute(ItemAttribute.FLOORCHANGEWEST, false);
		assertEquals(Direction.NORTH, item.getFloorChangeDirection());
		
		type.setAttribute(ItemAttribute.FLOORCHANGENORTH, false);
		type.setAttribute(ItemAttribute.FLOORCHANGESOUTH, true);
		assertEquals(Direction.SOUTH, item.getFloorChangeDirection());
		
		type.setAttribute(ItemAttribute.FLOORCHANGESOUTH, false);
		type.setAttribute(ItemAttribute.FLOORCHANGEEAST, true);
		assertEquals(Direction.EAST, item.getFloorChangeDirection());
		
		type.setAttribute(ItemAttribute.FLOORCHANGEEAST, false);
		type.setAttribute(ItemAttribute.FLOORCHANGEWEST, true);
		assertEquals(Direction.WEST, item.getFloorChangeDirection());
	}
	
	@Test
	public void identifiesComplexFloorchanging() {
		type.setAttribute(ItemAttribute.FLOORCHANGENORTH, true);
		type.setAttribute(ItemAttribute.FLOORCHANGESOUTH, false);
		type.setAttribute(ItemAttribute.FLOORCHANGEEAST, true);
		type.setAttribute(ItemAttribute.FLOORCHANGEWEST, false);
		assertEquals(Direction.NORTHEAST, item.getFloorChangeDirection());
		
		type.setAttribute(ItemAttribute.FLOORCHANGEEAST, false);
		type.setAttribute(ItemAttribute.FLOORCHANGEWEST, true);
		assertEquals(Direction.NORTHWEST, item.getFloorChangeDirection());
		
		type.setAttribute(ItemAttribute.FLOORCHANGENORTH, false);
		type.setAttribute(ItemAttribute.FLOORCHANGESOUTH, true);
		type.setAttribute(ItemAttribute.FLOORCHANGEEAST, true);
		type.setAttribute(ItemAttribute.FLOORCHANGEWEST, false);
		assertEquals(Direction.SOUTHEAST, item.getFloorChangeDirection());
		
		type.setAttribute(ItemAttribute.FLOORCHANGEEAST, false);
		type.setAttribute(ItemAttribute.FLOORCHANGEWEST, true);
		assertEquals(Direction.SOUTHWEST, item.getFloorChangeDirection());
	}
	
	@Test
	public void simpleDescription() {
		type.name = "SomeItem";
		assertEquals(type.name, item.getDescription());
	}
	
	@Test
	public void descriptionWithArticle() {
		type.name = "SomeItem";
		type.article = "SomeArticle";
		assertEquals(type.article + " " + type.name, item.getDescription());
	}
	
	@Test
	public void descriptionWithArticleAndFluid() {
		type.name = "SomeItem";
		type.article = "SomeArticle";
		type.group = ItemType.Group.FLUID;
		item.setFluidType(FluidType.NONE);
		assertEquals(type.article + " " + type.name + ". It is empty", 
				item.getDescription());
		
		item.setFluidType(FluidType.BLOOD);
		assertEquals(type.article + " " + type.name + " of " + item.getFluidType().getDescription(), 
				item.getDescription());
	}
	
	@Test
	public void descriptionWithArticleAndSplash() {
		type.name = "SomeItem";
		type.article = "SomeArticle";
		type.group = ItemType.Group.SPLASH;
		item.setFluidType(FluidType.BLOOD);
		assertEquals(type.article + " " + type.name + " of " + item.getFluidType().getDescription(), 
				item.getDescription());
	}
	
	@Test
	public void descriptionOffensiveWeapon() {
		type.name = "SomeItem";
		type.group = ItemType.Group.WEAPON;
		type.weaponType = WeaponType.SWORD;
		type.attack = 10;
		type.defense = 15;
		assertEquals(type.name + " (Atk: " + type.attack + " Def: " + type.defense + ")", 
				item.getDescription());
	}
	
	@Test
	public void descriptionAmmo() {
		type.name = "SomeItem";
		type.group = ItemType.Group.WEAPON;
		type.weaponType = WeaponType.AMMUNITION;
		type.attack = 10;
		assertEquals(type.name, 
				item.getDescription());
	}
	
	@Test
	public void descriptionDefensiveWeapon() {
		type.name = "SomeItem";
		type.group = ItemType.Group.WEAPON;
		type.weaponType = WeaponType.SWORD;
		type.attack = 0;
		type.defense = 15;
		assertEquals(type.name + " (Def: " + type.defense + ")", 
				item.getDescription());
	}
	
	@Test
	public void descriptionWithArm() {
		type.name = "SomeItem";
		type.group = ItemType.Group.ARMOR;
		type.armor = 100;
		assertEquals(type.name + " (Arm: " + type.armor + ")", 
				item.getDescription());
	}
	
	@Test
	public void fitsOnProperlyReflectsTypeSlots() {
		type.addSlot(InventorySlot.BACKPACK);
		for(InventorySlot slot : InventorySlot.values()) {
			assertFitsOnSlot(slot);
		}
	}

	private void assertFitsOnSlot(InventorySlot slot) {
		assertEquals("Item slot fitting behavoior does not match for slot " + slot.toString() + ".", 
				type.getSlots().contains(slot), item.fitsOn(slot));
	}
	
	@Test
	public void identifiesWeaponSkill() {
		type.weaponType = WeaponType.CLUB;
		assertEquals(Skills.Type.CLUB, item.getWeaponSkill());
		type.weaponType = WeaponType.AXE;
		assertEquals(Skills.Type.AXE, item.getWeaponSkill());
		type.weaponType = WeaponType.SWORD;
		assertEquals(Skills.Type.SWORD, item.getWeaponSkill());
		type.weaponType = WeaponType.DISTANCE;
		assertEquals(Skills.Type.DISTANCE, item.getWeaponSkill());
		
		type.weaponType = WeaponType.AMMUNITION;
		assertNull(item.getWeaponSkill());
		type.weaponType = WeaponType.WAND;
		assertNull(item.getWeaponSkill());
		type.weaponType = WeaponType.NONE;
		assertNull(item.getWeaponSkill());
	}
	
	@Test
	public void reflectsInternalIds() {
		type.serverId = 1337;
		type.clientId = 666;
		assertEquals(type.serverId, item.getId());
		assertEquals(type.clientId, item.getClientId());
	}
	

}
