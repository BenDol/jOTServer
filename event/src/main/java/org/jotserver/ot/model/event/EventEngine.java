package org.jotserver.ot.model.event;

import org.jotserver.ot.model.chat.ChatChannelEventEngine;
import org.jotserver.ot.model.item.ItemEventEngine;
import org.jotserver.ot.model.world.LocalGameWorld;

public interface EventEngine {
    void init(LocalGameWorld world);

    ItemEventEngine getItemEngine();

    ChatChannelEventEngine getChatEngine();
}
