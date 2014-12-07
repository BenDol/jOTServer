package org.jotserver.ot.model.chat;

import static org.junit.Assert.*;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.creature.Creature;
import org.junit.Before;
import org.junit.Test;


public class TestPrivateMessageChannel {
	
	private Mockery context;

	@Before
	public void setUp() {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
	}
	
	@Test
	public void hasOwner() {
		Creature c = context.mock(Creature.class);
		PrivateMessageChannel ch = new PrivateMessageChannel(c);
		assertSame(c, ch.getOwner());
	}
	
	@Test
	public void ownerIsTheSoleMember() {
		Creature c = context.mock(Creature.class);
		PrivateMessageChannel ch = new PrivateMessageChannel(c);
		assertEquals(1, ch.getMembers().size());
		assertSame(c, ch.getMembers().iterator().next());
		assertTrue(ch.getMembers().contains(c));
	}
	
}
