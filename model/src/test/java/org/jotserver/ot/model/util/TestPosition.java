package org.jotserver.ot.model.util;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jotserver.ot.model.player.InventorySlot;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestPosition {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void hasXYZCoordinates() {
		Position pos = new Position(1, 2, 3);
		assertEquals(1, pos.getX());
		assertEquals(2, pos.getY());
		assertEquals(3, pos.getZ());
	}
	
	@Test
	public void equalsIfCoordinatesMatch() {
		Position pos = new Position(1, 2, 3);
		assertTrue(pos.equals(new Position(1, 2, 3)));
		
		assertFalse(pos.equals(new Position(1, 2, 4)));
		assertFalse(pos.equals(new Position(1, 3, 3)));
		assertFalse(pos.equals(new Position(2, 2, 3)));
	}
	
	@Test
	public void doesNotEqualOtherObject() {
		String str = new String();
		Position pos = new Position(1, 2, 3);
		assertFalse(pos.equals(str));
	}
	
	@Test
	public void hashCodeIsEqualForEqualPositions() {
		Position p1 = new Position(1, 2, 3);
		Position p2 = new Position(1, 2, 3);
		
		assertEquals(p1.hashCode(), p2.hashCode());
	}
	
	@Test
	public void stringRepresentation() {
		Position pos = new Position(1, 2, 3);
		assertEquals("(1, 2, 3)", pos.toString());
	}
	
	@Test
	public void distanceIsTheMaximumDistanceInOneDimension() {
		Position p1 = new Position(0, 0, 0);
		Position p2 = new Position(5, 1, 0);
		Position p3 = new Position(5, -5, 0);
		assertEquals(5, p1.distanceTo(p2));
		assertEquals(5, p1.distanceTo(p3));
	}
	
	@Test
	public void scaleScalesEveryAxis() {
		Position pos = new Position(1, 2, 3);
		pos = pos.scale(10);
		assertEquals(10, pos.getX());
		assertEquals(20, pos.getY());
		assertEquals(30, pos.getZ());
	}
	
	@Test
	public void canAddTwoPositions() {
		Position pos = new Position(1, 2, 3);
		pos = pos.add(new Position(3, 2, 1));
		assertEquals(new Position(4, 4, 4), pos);
	}
	
	
	@Test
	public void addNormalDirection() {
		Position pos = new Position(10, 10, 0);
		assertEquals(new Position(10, 10-1, 0), pos.add(Direction.NORTH));
		assertEquals(new Position(10, 10+1, 0), pos.add(Direction.SOUTH));
		assertEquals(new Position(10+1, 10, 0), pos.add(Direction.EAST));
		assertEquals(new Position(10-1, 10, 0), pos.add(Direction.WEST));
	}
	
	@Test
	public void addDiagonalDirection() {
		Position pos = new Position(10, 10, 0);
		assertEquals(new Position(10-1, 10-1, 0), pos.add(Direction.NORTHWEST));
		assertEquals(new Position(10+1, 10-1, 0), pos.add(Direction.NORTHEAST));
		assertEquals(new Position(10+1, 10+1, 0), pos.add(Direction.SOUTHEAST));
		assertEquals(new Position(10-1, 10+1, 0), pos.add(Direction.SOUTHWEST));
	}
	
	@Test
	public void addZDirection() {
		Position pos = new Position(10, 10, 7);
		assertEquals(new Position(10, 10, 7-1), pos.add(Direction.UP));
		assertEquals(new Position(10, 10, 7+1), pos.add(Direction.DOWN));
	}
	
	@Test
	public void addNoneDirection() {
		Position pos = new Position(10, 10, 7);
		assertEquals(pos, pos.add(Direction.NONE));
	}
	
	@Test
	public void canGetZDistance() {
		Position p1 = new Position(10, 10, 7);
		Position p2 = new Position(10, 10, 0);
		assertEquals(7, p1.getZDistanceTo(p2));
		assertEquals(7, p2.getZDistanceTo(p1));
	}
	
	@Test
	public void directionToPositionSimple() {
		Position p1 = new Position(10, 10, 7);
		Position p2;
		
		p2 = new Position(10, 10, 7);
		assertEquals(Direction.NONE, p1.directionTo(p2));
		
		p2 = new Position(10, 5, 7);
		assertEquals(Direction.NORTH, p1.directionTo(p2));
		
		p2 = new Position(10, 15, 7);
		assertEquals(Direction.SOUTH, p1.directionTo(p2));
		
		p2 = new Position(5, 10, 7);
		assertEquals(Direction.WEST, p1.directionTo(p2));
		
		p2 = new Position(15, 10, 7);
		assertEquals(Direction.EAST, p1.directionTo(p2));
	}
	
	
	@Test
	public void directionToPositionClose() {
		Position p1 = new Position(10, 10, 7);
		
		assertEquals(Direction.NORTH, p1.directionTo(new Position(10, 10-1, 7)));
		assertEquals(Direction.SOUTH, p1.directionTo(new Position(10, 10+1, 7)));
		assertEquals(Direction.WEST, p1.directionTo(new Position(10-1, 10, 7)));
		assertEquals(Direction.EAST, p1.directionTo(new Position(10+1, 10, 7)));
		
		assertEquals(Direction.NORTHWEST, p1.directionTo(new Position(10-1, 10-1, 7)));
		assertEquals(Direction.NORTHEAST, p1.directionTo(new Position(10+1, 10-1, 7)));
		assertEquals(Direction.SOUTHWEST, p1.directionTo(new Position(10-1, 10+1, 7)));
		assertEquals(Direction.SOUTHEAST, p1.directionTo(new Position(10+1, 10+1, 7)));
	}
	
	@Test
	public void directionToPositionComplex() {
		Position p1 = new Position(10, 10, 7);
		
		assertEquals(Direction.NORTH, p1.directionTo(new Position(10+1, 10-5, 7)));
		assertEquals(Direction.NORTH, p1.directionTo(new Position(10-1, 10-5, 7)));
		
		assertEquals(Direction.NORTH, p1.directionTo(new Position(10+2, 10-5, 7)));
		assertEquals(Direction.NORTH, p1.directionTo(new Position(10-2, 10-5, 7)));
		
		assertEquals(Direction.NORTHEAST, p1.directionTo(new Position(10+3, 10-5, 7)));
		assertEquals(Direction.NORTHWEST, p1.directionTo(new Position(10-3, 10-5, 7)));
		
		assertEquals(Direction.NORTHEAST, p1.directionTo(new Position(10+4, 10-5, 7)));
		assertEquals(Direction.NORTHWEST, p1.directionTo(new Position(10-4, 10-5, 7)));
		
		assertEquals(Direction.NORTHEAST, p1.directionTo(new Position(10+5, 10-5, 7)));
		assertEquals(Direction.NORTHWEST, p1.directionTo(new Position(10-5, 10-5, 7)));
	}
	
	@Test
	public void nextToPosition() {
		Position p1 = new Position(10, 10, 7);
		assertTrue(p1.isNextTo(new Position(10+1, 10, 7)));
		assertTrue(p1.isNextTo(new Position(10+1, 10+1, 7)));
		assertTrue(p1.isNextTo(new Position(10, 10-1, 7)));
		assertTrue(p1.isNextTo(new Position(10-1, 10-1, 7)));
		assertTrue(p1.isNextTo(new Position(10, 10, 7)));
		
		assertFalse(p1.isNextTo(new Position(10+2, 10, 7)));
		assertFalse(p1.isNextTo(new Position(10+1, 10+2, 7)));
		assertFalse(p1.isNextTo(new Position(10, 10-1, 6)));
		assertFalse(p1.isNextTo(new Position(10-1, 10-1, 8)));
		assertFalse(p1.isNextTo(new Position(10-8, 10+20, 7)));
	}
	
	
	/*
	 * Special locations method tests
	 */

	@Test
	public void inventoryPosition() {
		Position p = new Position(0xFFFF, InventorySlot.ARMOR.ordinal(), 0);
		assertTrue(p.isInventory());
		assertFalse(p.isContainer());
		assertEquals(InventorySlot.ARMOR, p.getInventorySlot());
	}
	
	@Test
	public void containerPosition() {
		Position p = new Position(0xFFFF, (12 | 0x40), 15);
		assertFalse(p.isInventory());
		assertTrue(p.isContainer());
		assertEquals(12, p.getContainerId());
		assertEquals(15, p.getContainerSlot());
	}
	
	
}
