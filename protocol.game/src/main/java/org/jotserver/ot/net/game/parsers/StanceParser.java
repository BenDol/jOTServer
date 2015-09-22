package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.player.Stance;
import org.jotserver.ot.net.game.PacketType;

public class StanceParser extends AbstractParser {
    public void parse(PacketType type, InputStream message) throws IOException {
        int stanceId = CData.readByte(message);
        /*int chaseMode = */CData.readByte(message);
        /*int safeMode = */CData.readByte(message);
        Stance stance = Stance.BALANCED;
        switch(stanceId) {
        case 1:
            stance = Stance.OFFENSIVE;
            break;
        case 2:
            stance = Stance.BALANCED;
            break;
        case 3:
            stance = Stance.DEFENSIVE;
            break;
        }
        getPlayer().setStance(stance);
    }
}
