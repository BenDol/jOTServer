package org.jotserver.ot.model.chat;


import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.creature.TestableCreature;
import org.junit.Before;
import org.junit.Test;

public class TestPrivateChatChannel {

	private static final int ID = 0x22;
	private static final String NAME = "SomeName";
	private static final long CID = 1337;
	private PrivateChatChannel channel;
	private TestableCreature owner;

	@Before
	public void setUp() throws Exception {
		owner = new TestableCreature(CID, NAME);
		channel = new PrivateChatChannel(ID, owner);
	}
	
	@Test
	public void hasOwner() {
		assertEquals(owner, channel.getOwner());
	}
	
	@Test
	public void hasId() {
		assertEquals(ID, channel.getId());
	}
	
	@Test
	public void constructsCorrectName() {
		assertEquals(NAME + "'s Channel", channel.getName());
	}
	
	@Test
	public void newChannelHasNoMembers() {
		assertTrue(channel.getMembers().isEmpty());
	}
	
	@Test
	public void uninvitedCreatureIsNotInvited() {
		Creature c = new TestableCreature(1, "1");
		assertFalse(channel.isInvited(c));
	}
	
	@Test
	public void ownerIsInvited() {
		assertTrue(channel.isInvited(owner));
	}
	
	@Test
	public void canInviteCreature() {
		
	}

}
