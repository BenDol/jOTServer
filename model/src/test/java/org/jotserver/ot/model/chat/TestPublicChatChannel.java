package org.jotserver.ot.model.chat;


import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.junit.Before;
import org.junit.Test;

public class TestPublicChatChannel {

	private static final String NAME = "MyChannel";
	private static final int ID = 1337;
	private PublicChatChannel ch;
	private Mockery context;
	private Creature creature;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		ch = new PublicChatChannel(ID, NAME);
		creature = context.mock(Creature.class);
	}
	
	@Test
	public void hasIdAndName() {
		assertEquals(ID, ch.getId());
		assertEquals(NAME, ch.getName());
	}
	
	@Test
	public void newChannelHasNoMembers() {
		assertTrue(ch.getMembers().isEmpty());
	}
	
	@Test
	public void canJoinChannel() {
		ch.join(creature);
		assertEquals(1, ch.getMembers().size());
		assertTrue(ch.getMembers().contains(creature));
		assertSame(creature, ch.getMembers().iterator().next());
	}
	
	@Test
	public void cannotKickOtherMembers() {
		Player p = context.mock(Player.class);
		ch.join(creature);
		ch.join(p);
		assertEquals(2, ch.getMembers().size());
		assertFalse(ch.kick(p, creature));
		assertEquals(2, ch.getMembers().size());
	}
	
	@Test
	public void canKickYourself() {
		ch.join(creature);
		assertTrue(ch.kick(creature, creature));
		assertTrue(ch.getMembers().isEmpty());
	}
	
}
