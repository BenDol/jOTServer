package org.jotserver.ot.model.chat;

import java.util.Collection;
import java.util.Collections;

import org.jotserver.ot.model.TextMessageType;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.action.ErrorType;

public class PrivateMessageChannel implements OwnedChatChannel, JoinableChatChannel {
	
	private Creature owner;
	
	public PrivateMessageChannel(Creature owner) {
		this.owner = owner;
	}
	
	
	public boolean close(Creature creature) {
		return false;
	}

	
	public Creature getOwner() {
		return owner;
	}

	
	public boolean speak(Creature creature, SpeakType type, String text) {
		return true;
	}

	
	public boolean invite(Creature creature, Creature invited) {
		return false;
	}

	
	public boolean join(Creature creature) {
		return true;
	}

	
	public boolean kick(Creature creature, Creature kicked) {
		return false;
	}

	
	public boolean uninvite(Creature creature, Creature invited) {
		return false;
	}
	
	
	public Collection<Creature> getMembers() {
		return Collections.singleton(owner);
	}
	
	public void sendMessage(TextMessageType type, String message) {
		//
	}
	
	public void sendCancel(String string) {
		sendMessage(TextMessageType.STATUS_SMALL, string);
	}
	
	public void sendCancel(ErrorType error) {
		sendCancel(error.getMessage());
	}
	
	public void sendDescription(String string) {
		sendMessage(TextMessageType.DESCRIPTION, string);
	}
	
	public boolean isInvited(Creature creature) {
		return true;
	}
	
}
