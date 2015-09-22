package org.jotserver.ot.model.chat;

import org.jotserver.ot.model.creature.Creature;

public interface OwnedChatChannel extends ChatChannel {
    Creature getOwner();

    boolean close(Creature creature);
}
