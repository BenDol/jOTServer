package org.jotserver.ot.model.chat;

import org.jotserver.ot.model.creature.Creature;

public interface ChatChannelSayHandler {
    boolean onSay(Creature creature, SpeakType type, String message);
}