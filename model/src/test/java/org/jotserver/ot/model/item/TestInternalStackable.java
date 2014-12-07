package org.jotserver.ot.model.item;


import static org.junit.Assert.*;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.Cylinder;
import org.junit.Before;
import org.junit.Test;

public class TestInternalStackable {

	private static final int COUNT = 56;
	private Mockery context;
	private InternalStackable internal;
	private Cylinder cylinder;
	private Stackable stackable;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		cylinder = context.mock(Cylinder.class);
		stackable = context.mock(Stackable.class);
		internal = new InternalStackable(stackable, COUNT, cylinder);
	}
	
	@Test
	public void hasStackable() {
		assertSame(stackable, internal.getStackable());
	}
	
	@Test
	public void hasCount() {
		assertSame(COUNT, internal.getCount());
	}
	
	@Test
	public void changeCountValid() {
		internal.setCount(COUNT+1);
		assertSame(COUNT+1, internal.getCount());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void changeCountOverflow() {
		internal.setCount(101);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void changeCountUnderflow() {
		internal.setCount(0);
	}

}
