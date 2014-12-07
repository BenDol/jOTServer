package org.jotserver.ot.model.chat;

import org.jotserver.ot.model.creature.Creature;

public class DefaultChatChannel implements ChatChannel {

	public boolean speak(Creature creature, SpeakType type, String text) {
		return true;
	}
	
	public boolean yell(Creature creature, SpeakType type, String text) {
		return true;
	}
	
	public boolean whisper(Creature creature, SpeakType type, String text) {
		return true;
	}
	
	public boolean broadcast(Creature creature, SpeakType type, String text) {
		return true;
	}
}
