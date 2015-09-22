package org.jotserver.ot.net.game.creature;

import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.net.CDataOutputStream;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

public class PlayerAddVipWriter extends AbstractWriter {

    private Player vip;
    private boolean online;

    public PlayerAddVipWriter(Player player, Player vip, boolean online) {
        super(player);
        this.vip = vip;
        this.online = online;
    }

    public void write(OutputStream out) throws IOException {
        CDataOutputStream cout = new CDataOutputStream(out);
        cout.writeByte(0xD2);
        cout.writeU32(vip.getGlobalId());
        cout.writeString(vip.getName());
        cout.writeByte(online? 1 : 0);
    }

}
