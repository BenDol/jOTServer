package org.jotserver.ot.model.item;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestFluidType {
	
	@Test
	public void canLookupByIntegerType() {
		assertEquals(FluidType.BLOOD, FluidType.get(2));
		assertEquals(FluidType.LEMONADE, FluidType.get(5));
		assertEquals(FluidType.LIFEFLUID, FluidType.get(10));
		assertEquals(FluidType.LAVA, FluidType.get(26));
	}
	
	@Test
	public void hasId() {
		assertEquals(2, FluidType.BLOOD.getId());
		assertEquals(5, FluidType.LEMONADE.getId());
		assertEquals(10, FluidType.LIFEFLUID.getId());
	}
	
	@Test
	public void hasColor() {
		assertEquals(FluidType.Color.RED, FluidType.BLOOD.getColor());
		assertEquals(FluidType.Color.YELLOW, FluidType.LEMONADE.getColor());
		assertEquals(FluidType.Color.BROWN, FluidType.BEER.getColor());
	}
	
	@Test
	public void hasDescription() {
		assertEquals("blood", FluidType.BLOOD.getDescription());
		assertEquals("life fluid", FluidType.LIFEFLUID.getDescription());
		assertEquals("beer", FluidType.BEER.getDescription());
	}
	
	@Test
	public void colorHasServerId() {
		assertEquals(2, FluidType.Color.RED.getId());
		assertEquals(5, FluidType.Color.YELLOW.getId());
		assertEquals(7, FluidType.Color.PURPLE.getId());
	}
	
	@Test
	public void colorHasClientId() {
		assertEquals(5, FluidType.Color.RED.getClientId());
		assertEquals(8, FluidType.Color.YELLOW.getClientId());
		assertEquals(2, FluidType.Color.PURPLE.getClientId());
	}
	
}
