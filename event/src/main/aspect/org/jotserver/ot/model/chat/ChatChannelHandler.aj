package org.jotserver.ot.model.chat;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.event.EventEngine;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.world.LocalGameWorld;

public aspect ChatChannelHandler {

    public pointcut creatureSpeak(ChatChannel channel, Creature creature, SpeakType type, String message) :
        target(channel) &&
        args(creature, type, message) &&
        execution(public boolean ChatChannel.speak(Creature, SpeakType, String));

    boolean around(ChatChannel channel, Creature creature, SpeakType type, String message) :
        creatureSpeak(channel, creature, type, message) {

        Tile tile = creature.getTile();
        if(tile != null) {
            LocalGameWorld world = tile.getGameWorld();
            EventEngine events = world.getEventEngine();

            boolean ret = events.getChatEngine().getMappings(channel).onSay(creature, type, message);

            return ret && proceed(channel, creature, type, message);
        } else {
            return false;
        }
    }
}
