package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.PacketType;

public class AttackParser extends AbstractParser {
    public void parse(PacketType type, InputStream message) throws IOException {
        Player player = getContext().getPlayer();
        long creatureId = CData.readU32(message);
        Creature creature = null;
        for(Creature current : player.getKnownCreaturesCache().getCreatures()) {
            if(current.getId() == creatureId) {
                creature = current;
                break;
            }
        }
        //player.getCombat().setTarget(creature); TODO
    }
}
