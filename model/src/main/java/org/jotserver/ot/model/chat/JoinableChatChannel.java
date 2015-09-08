package org.jotserver.ot.model.chat;

import org.jotserver.ot.model.creature.Creature;

import java.util.Collection;

public interface JoinableChatChannel extends ChatChannel {
	boolean join(Creature creature);

	boolean isInvited(Creature creature);

	boolean invite(Creature creature, Creature invited);

	boolean uninvite(Creature creature, Creature invited);

	boolean kick(Creature creature, Creature kicked);

	Collection<Creature> getMembers();
}
