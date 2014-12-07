package org.jotserver.ot.model.util;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestInterval2D {

	private static final int Y2 = 40;
	private static final int X2 = 100;
	private static final int Y1 = 20;
	private static final int X1 = 10;
	private Interval2D interval;

	@Before
	public void setUp() throws Exception {
		interval = new Interval2D(X1, Y1, X2, Y2);
	}
	
	@Test
	public void has2DBounds() {
		assertEquals(X1, interval.getStartX());
		assertEquals(Y1, interval.getStartY());
		assertEquals(X2, interval.getEndX());
		assertEquals(Y2, interval.getEndY());
	}
	
	@Test
	public void constructFromPositions() {
		Position from = new Position(X1, Y1, 0);
		Position to = new Position(X2, Y2, 0);
		interval = new Interval2D(from, to);
		
		assertEquals(X1, interval.getStartX());
		assertEquals(Y1, interval.getStartY());
		assertEquals(X2, interval.getEndX());
		assertEquals(Y2, interval.getEndY());
	}
	
	@Test
	public void constructFromPositionAndDimensions() {
		Position from = new Position(X1, Y1, 0);
		interval = new Interval2D(from, X2-X1, Y2-Y1);
		
		assertEquals(X1, interval.getStartX());
		assertEquals(Y1, interval.getStartY());
		assertEquals(X2, interval.getEndX());
		assertEquals(Y2, interval.getEndY());
	}
	
	@Test
	public void doesNotContainsPointOutsideInterval() {
		assertFalse(interval.contains(X2+1, (Y2+Y1)/2));
		assertFalse(interval.contains(X1-1, (Y2+Y1)/2));
		
		assertFalse(interval.contains((X1+X2)/2, Y1-1));
		assertFalse(interval.contains((X1+X2)/2, Y2+1));
	}
	
	@Test
	public void containsPositionInsideInterval() {
		assertTrue(interval.contains((X1+X2)/2, (Y2+Y1)/2));
		
		assertTrue(interval.contains(X2, (Y2+Y1)/2));
		assertTrue(interval.contains(X1, (Y2+Y1)/2));
		
		assertTrue(interval.contains((X1+X2)/2, Y1));
		assertTrue(interval.contains((X1+X2)/2, Y2));
	}
	
	@Test
	public void offsetIntervalIn2DSpace() {
		Interval2D interval2 = interval.offset(100, 100);
		
		assertEquals(X1+100, interval2.getStartX());
		assertEquals(Y1+100, interval2.getStartY());
		assertEquals(X2+100, interval2.getEndX());
		assertEquals(Y2+100, interval2.getEndY());
	}
	
	@Test
	public void stringRepresentation() {
		assertEquals("[x: " + X1 + "-" + X2 + ", y: " + Y1 + "-" + Y2 + "]", interval.toString());
	}
	
	@Test
	public void findClosestContainedXOutsideLeft() {
		assertEquals(X1, interval.getClosestX(0));
	}
	
	@Test
	public void findClosestContainedXOutsideRight() {
		assertEquals(X2, interval.getClosestX(X2+100));
	}
	
	@Test
	public void findClosestContainedXInside() {
		assertEquals((X1+X2)/2, interval.getClosestX((X1+X2)/2));
	}
	
	@Test
	public void findClosestContainedYOutsideAbove() {
		assertEquals(Y1, interval.getClosestY(0));
	}
	
	@Test
	public void findClosestContainedYOutsideBelow() {
		assertEquals(Y2, interval.getClosestY(Y2+100));
	}
	
	@Test
	public void findClosestContainedYInside() {
		assertEquals((Y2+Y1)/2, interval.getClosestY((Y2+Y1)/2));
	}
	
	
}
