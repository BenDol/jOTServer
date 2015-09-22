package org.jotserver.ot.net.game.out;

import org.jotserver.net.CData;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

import java.io.IOException;
import java.io.OutputStream;

public class PlayerCancelWalkWriter extends AbstractWriter {

    public PlayerCancelWalkWriter(Player receiver) {
        super(receiver);
    }

    public void write(OutputStream out) throws IOException {
        CData.writeByte(out, 0xB5);
        CData.writeByte(out, getReceiver().getDirection().ordinal());
    }
}
