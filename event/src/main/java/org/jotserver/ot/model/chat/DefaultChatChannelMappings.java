package org.jotserver.ot.model.chat;

import java.util.LinkedList;

import org.jotserver.ot.model.creature.Creature;

public class DefaultChatChannelMappings extends ChannelEventMappings {
	
	private LinkedList<DefaultChatChannelHandler> mappings;
	
	public DefaultChatChannelMappings() {
		mappings = new LinkedList<DefaultChatChannelHandler>();
	}
	
	public void addHandler(ChatChannelSayHandler handler) {
		if(handler instanceof DefaultChatChannelHandler) {
			mappings.add((DefaultChatChannelHandler)handler);
		}
	}
	
	public boolean onSay(Creature creature, SpeakType type, String message) {
		for(DefaultChatChannelHandler handler : mappings) {
			if(!handler.onSay(creature, type, message)) {
				return false;
			}
		}
		return true;
	}
	public boolean onYell(Creature creature, SpeakType type, String message) {
		for(DefaultChatChannelHandler handler : mappings) {
			if(!handler.onYell(creature, type, message)) {
				return false;
			}
		}
		return true;
	}
	public boolean onWhisper(Creature creature, SpeakType type, String message) {
		for(DefaultChatChannelHandler handler : mappings) {
			if(!handler.onWhisper(creature, type, message)) {
				return false;
			}
		}
		return true;
	}
	public boolean onBroadcast(Creature creature, SpeakType type, String message) {
		for(DefaultChatChannelHandler handler : mappings) {
			if(!handler.onBroadcast(creature, type, message)) {
				return false;
			}
		}
		return true;
	}
	
}
