package org.jotserver.ot.model.chat;

import org.jotserver.ot.model.creature.Creature;

import java.util.Collection;

public interface JoinableChatChannel extends ChatChannel {
	public boolean join(Creature creature);

	public boolean isInvited(Creature creature);

	public boolean invite(Creature creature, Creature invited);

	public boolean uninvite(Creature creature, Creature invited);

	public boolean kick(Creature creature, Creature kicked);

	public Collection<Creature> getMembers();
}
