package org.jotserver.ot.model.chat;

import org.jotserver.ot.model.creature.Creature;

public interface ChatChannel {
	boolean speak(Creature creature, SpeakType type, String text);
}
