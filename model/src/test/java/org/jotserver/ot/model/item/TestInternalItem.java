package org.jotserver.ot.model.item;


import static org.junit.Assert.*;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.Cylinder;
import org.junit.Before;
import org.junit.Test;

public class TestInternalItem {

	private Mockery context;
	private Item item;
	private Cylinder cylinder;
	private InternalItem internal;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		cylinder = context.mock(Cylinder.class);
		item = context.mock(Item.class);
		internal = new InternalItem(item, cylinder);
	}
	
	@Test
	public void hasItem() {
		assertSame(item, internal.getItem());
	}
	
	@Test
	public void defaultFluidTypeIsNone() {
		assertEquals(FluidType.NONE, internal.getFluidType());
	}
	
	@Test
	public void hasFluidType() {
		internal.setFluidType(FluidType.BLOOD);
		assertEquals(FluidType.BLOOD, internal.getFluidType());
	}

}
