package org.jotserver.ot.model.chat;

import java.util.HashMap;

import org.jotserver.ot.model.world.LocalGameWorld;


public class ChatChannelEventEngine {
	private HashMap<ChatChannel, ChannelEventMappings> mappings;
	private DefaultChatChannelMappings defaultMappings;
	
	public ChatChannelEventEngine(LocalGameWorld world) {
		defaultMappings = new DefaultChatChannelMappings();
		mappings = new HashMap<ChatChannel, ChannelEventMappings>();
		mappings.put(world.getChatManager().getDefaultChannel(), defaultMappings);
	}
	
	public ChannelEventMappings getMappings(ChatChannel channel) {
		ChannelEventMappings map = mappings.get(channel);
		if(map == null) {
			map = new ChannelEventMappings();
			mappings.put(channel, map);
		}
		return map;
	}
	
	public void registerChannelSayHandler(ChatChannel channel, ChatChannelSayHandler handler) {
		getMappings(channel).addHandler(handler);
	}
	
	public void registerDefaultHandler(DefaultChatChannelHandler handler) {
		getDefaultMappings().addHandler(handler);
	}
	
	public DefaultChatChannelMappings getDefaultMappings() {
		return defaultMappings;
	}
	
}
