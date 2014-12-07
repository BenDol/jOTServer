package org.jotserver.ot.model.chat;

import org.jotserver.ot.model.creature.Creature;

public interface OwnedChatChannel extends ChatChannel {
	public Creature getOwner();
	public boolean close(Creature creature);
}
