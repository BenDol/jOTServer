package org.jotserver.ot.model.util;

import static org.jotserver.ot.model.util.Direction.DOWN;
import static org.jotserver.ot.model.util.Direction.EAST;
import static org.jotserver.ot.model.util.Direction.NONE;
import static org.jotserver.ot.model.util.Direction.NORTH;
import static org.jotserver.ot.model.util.Direction.NORTHEAST;
import static org.jotserver.ot.model.util.Direction.NORTHWEST;
import static org.jotserver.ot.model.util.Direction.SOUTH;
import static org.jotserver.ot.model.util.Direction.SOUTHEAST;
import static org.jotserver.ot.model.util.Direction.SOUTHWEST;
import static org.jotserver.ot.model.util.Direction.UP;
import static org.jotserver.ot.model.util.Direction.WEST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.EnumSet;

import org.junit.Test;


public class TestDirection {
	
	@Test
	public void combineDirectionWithNone() {
		assertEquals(NORTH, NORTH.combine(NONE));
		assertEquals(NORTH, NONE.combine(NORTH));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void combineDirectionWithOpposite() {
		NORTH.combine(SOUTH);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void combineDirectionWithOpposite2() {
		WEST.combine(EAST);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void combineDiagonalDirection() {
		NORTHWEST.combine(WEST);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void combineDiagonalDirection2() {
		NORTHWEST.combine(NORTH);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void combineZDirections() {
		UP.combine(NORTH);
	}
	
	
	@Test
	public void validCombinations() {
		assertEquals(NORTHWEST, NORTH.combine(WEST));
		assertEquals(NORTHWEST, WEST.combine(NORTH));
		
		assertEquals(NORTHEAST, NORTH.combine(EAST));
		assertEquals(NORTHEAST, EAST.combine(NORTH));
		
		assertEquals(SOUTHWEST, SOUTH.combine(WEST));
		assertEquals(SOUTHWEST, WEST.combine(SOUTH));
		
		assertEquals(SOUTHEAST, SOUTH.combine(EAST));
		assertEquals(SOUTHEAST, EAST.combine(SOUTH));
	}
	
	@Test
	public void validInvertations() {
		assertInvertation(NORTH, SOUTH);
		assertInvertation(EAST, WEST);
		assertInvertation(NORTHEAST, SOUTHWEST);
		assertInvertation(NORTHWEST, SOUTHEAST);
		assertInvertation(UP, DOWN);
		assertInvertation(NONE, NONE);
	}
	
	private void assertInvertation(Direction a, Direction b) {
		assertEquals(a, b.invert());
		assertEquals(b, a.invert());
	}
	
	@Test
	public void normalizeNormalDirections() {
		assertEquals(NORTH, NORTH.normalize());
		assertEquals(SOUTH, SOUTH.normalize());
		assertEquals(WEST, WEST.normalize());
		assertEquals(EAST, EAST.normalize());
	}
	
	@Test
	public void normalizeDiagonalGivesHorizontalComponents() {
		assertEquals(WEST, NORTHWEST.normalize());
		assertEquals(WEST, SOUTHWEST.normalize());
		
		assertEquals(EAST, NORTHEAST.normalize());
		assertEquals(EAST, SOUTHEAST.normalize());
	}
	
	@Test
	public void normalizeNoneAndZDirectionsGivesNone() {
		assertEquals(NONE, NONE.normalize());
		assertEquals(NONE, UP.normalize());
		assertEquals(NONE, DOWN.normalize());
	}
	
	@Test
	public void isDiagonal() {
		assertFalse(NORTH.isDiagonal());
		assertFalse(SOUTH.isDiagonal());
		assertFalse(EAST.isDiagonal());
		assertFalse(WEST.isDiagonal());
		
		assertTrue(NORTHEAST.isDiagonal());
		assertTrue(NORTHWEST.isDiagonal());
		assertTrue(SOUTHEAST.isDiagonal());
		assertTrue(SOUTHWEST.isDiagonal());
	}
	
	@Test
	public void zDirectionsAreNotDiagonal() {
		assertFalse(UP.isDiagonal());
		assertFalse(DOWN.isDiagonal());
	}
	
	@Test
	public void normalDirectionsOnlyContainsSelf() {
		assertContains(NORTH, EnumSet.of(NORTH));
		assertContains(SOUTH, EnumSet.of(SOUTH));
		assertContains(EAST, EnumSet.of(EAST));
		assertContains(WEST, EnumSet.of(WEST));
	}
	
	@Test
	public void diagonalDirectionsContainsTwoComponents() {
		assertContains(NORTHWEST, EnumSet.of(NORTH, WEST));
		assertContains(SOUTHWEST, EnumSet.of(SOUTH, WEST));
		assertContains(NORTHEAST, EnumSet.of(EAST, NORTH));
		assertContains(SOUTHEAST, EnumSet.of(SOUTH, EAST));
	}
	
	private void assertContains(Direction dir, EnumSet<Direction> components) {
		for(Direction d : components) {
			assertTrue(dir + ".contains(" + d + ") == true", dir.contains(d));
		}
		for(Direction d : EnumSet.complementOf(components)) {
			if(d.isDiagonal() || d.isZ() || d == NONE) continue;
			assertFalse(dir + ".contains(" + d + ") == false", dir.contains(d));
		}
	}
	
	@Test
	public void isZ() {
		assertTrue(UP.isZ());
		assertTrue(DOWN.isZ());
		for(Direction d : EnumSet.complementOf(EnumSet.of(UP, DOWN))) {
			assertFalse(d.isZ());
		}
		
	}
	
}
