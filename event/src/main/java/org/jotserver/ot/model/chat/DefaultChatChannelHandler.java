package org.jotserver.ot.model.chat;

import org.jotserver.ot.model.creature.Creature;

public interface DefaultChatChannelHandler extends ChatChannelSayHandler {
    boolean onYell(Creature creature, SpeakType type, String message);

    boolean onWhisper(Creature creature, SpeakType type, String message);

    boolean onBroadcast(Creature creature, SpeakType type, String message);
}
