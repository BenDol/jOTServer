package org.jotserver.ot.model.util;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestInterval {

	private static final int TO = 1337;
	private static final int FROM = 10;
	private Interval interval;

	@Before
	public void setUp() throws Exception {
		interval = new Interval(FROM, TO);
	}
	
	@Test
	public void hasStartAndEndBounds() {
		assertEquals(FROM, interval.getStart());
		assertEquals(TO, interval.getEnd());
	}
	
	@Test
	public void hasLength() {
		assertEquals(TO-FROM, interval.getLength());
	}

}
