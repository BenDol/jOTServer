package org.jotserver.ot.model.chat;

import java.util.Collection;
import java.util.Collections;

import org.jotserver.ot.model.creature.Creature;

public class ChatManager {
	
	private DefaultChatChannel defaultChannel;
	private ChatChannelList<PublicChatChannel> publicChannels;
	
	public ChatManager() {
		defaultChannel = new DefaultChatChannel();
		publicChannels = new ChatChannelList<PublicChatChannel>();
	}
	
	public PublicChatChannel getPublicChannel(int id) {
		return publicChannels.getChannel(id);
	}
	
	public Collection<PublicChatChannel> getPublicChannels() {
		return publicChannels.getChannels();
	}
	
	public void addPublicChannel(PublicChatChannel channel) {
		publicChannels.addChannel(channel);
	}
	
	public DefaultChatChannel getDefaultChannel() {
		return defaultChannel;
	}
	
	public Collection<? extends IdentifiableChatChannel> getAvailableChannels(Creature creature) {
		return getPublicChannels();
	}

	public IdentifiableChatChannel getCreatureChannel(Creature player, int id) {
		return getPublicChannel(id);
	}
	
}
