package org.jotserver.ot.model.creature;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestPath {
	
	@Test
	public void emptyPathIsEmpty() {
		Path p = Path.EMPTY;
		assertTrue(p.isEmpty());
		assertNull(p.getNextStep());
		assertNull(p.getCurrentPosition());
	}
	
}
