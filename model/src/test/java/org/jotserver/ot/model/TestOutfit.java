package org.jotserver.ot.model;

import static org.junit.Assert.*;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;


public class TestOutfit {
	
	private Mockery context;
	private OutfitType mockedType;

	@Before
	public void setUp() {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		mockedType = context.mock(OutfitType.class);
	}
	
	@Test
	public void hasDefaultValues() {
		Outfit outfit = new Outfit(mockedType);
		assertEquals(0, outfit.getAddons());
		assertEquals(40, outfit.getBody());
		assertEquals(40, outfit.getFeet());
		assertEquals(40, outfit.getHead());
		assertEquals(40, outfit.getLegs());
		assertSame(mockedType, outfit.getType());
	}
	
	@Test
	public void hasSpecifiedValues() {
		Outfit outfit = new Outfit(mockedType, 1, 2, 3, 4, 5);
		assertEquals(1, outfit.getHead());
		assertEquals(2, outfit.getBody());
		assertEquals(3, outfit.getLegs());
		assertEquals(4, outfit.getFeet());
		assertEquals(5, outfit.getAddons());
		assertSame(mockedType, outfit.getType());
	}
}
