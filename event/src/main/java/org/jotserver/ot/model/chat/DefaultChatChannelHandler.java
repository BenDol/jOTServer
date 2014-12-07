package org.jotserver.ot.model.chat;

import org.jotserver.ot.model.creature.Creature;

public interface DefaultChatChannelHandler extends ChatChannelSayHandler {
	public boolean onYell(Creature creature, SpeakType type, String message);
	public boolean onWhisper(Creature creature, SpeakType type, String message);
	public boolean onBroadcast(Creature creature, SpeakType type, String message);
}
