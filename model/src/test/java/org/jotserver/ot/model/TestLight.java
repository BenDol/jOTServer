package org.jotserver.ot.model;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestLight {
	
	@Test
	public void hasLevel() {
		Light light = new Light(1, 2);
		assertEquals(1, light.getLevel());
	}
	
	@Test
	public void hasColor() {
		Light light = new Light(1, 2);
		assertEquals(2, light.getColor());
	}
	
}
