package org.jotserver.ot.model.chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ChatChannelList<T extends IdentifiableChatChannel> {
	
	private Map<Integer, T> channels;
	
	public ChatChannelList() {
		channels = new HashMap<Integer, T>();
	}
	
	public T getChannel(int id) {
		return channels.get(id);
	}
	
	public void addChannel(T channel) {
		channels.put(channel.getId(), channel);
	}
	
	public boolean removeChannel(T channel) {
		return removeChannel(channel.getId());
	}
	
	public boolean removeChannel(int id) {
		return channels.remove(id) != null;
	}

	public Collection<T> getChannels() {
		return new ArrayList<T>(channels.values());
	}
}
