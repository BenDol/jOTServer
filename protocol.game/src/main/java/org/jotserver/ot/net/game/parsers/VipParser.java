package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.player.PlayerAccessException;
import org.jotserver.ot.net.game.PacketType;

public class VipParser extends AbstractParser {
    public void parse(PacketType type, InputStream message) throws IOException {
        switch(type) {
        case ADDVIP:
            parseAddVip(message);
            break;
        case REMOVEVIP:
            parseRemoveVip(message);
            break;
        default:
            throw new IllegalArgumentException("Unknown packet type.");
        }
    }

    private void parseAddVip(InputStream message) throws IOException {
        String name = CData.readString(message);
        Player vip = getWorld().getPlayerByName(name);
        if(vip == null) {
            try {
                vip = getContext().getPlayers().getPlayer(name, getContext().getWorlds());
            } catch (PlayerAccessException e) { }
        }
        if(vip != null) {
            getPlayer().addVip(vip);
        } else {
            getPlayer().getPrivateChannel().sendCancel("A player by that name does not exist.");
        }
    }

    private void parseRemoveVip(InputStream message) throws IOException {
        long globalId = CData.readU32(message);
        getPlayer().removeVip(globalId);
    }
}
