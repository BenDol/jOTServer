package org.jotserver.ot.model.event;

import org.jotserver.ot.model.chat.ChatChannelEventEngine;
import org.jotserver.ot.model.item.ItemEventEngine;
import org.jotserver.ot.model.world.LocalGameWorld;

public interface EventEngine {
	public void init(LocalGameWorld world);

	public ItemEventEngine getItemEngine();

	public ChatChannelEventEngine getChatEngine();
}
