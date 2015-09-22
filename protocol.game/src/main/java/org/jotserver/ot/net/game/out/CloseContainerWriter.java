package org.jotserver.ot.net.game.out;

import org.jotserver.net.CData;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

import java.io.IOException;
import java.io.OutputStream;

public class CloseContainerWriter extends AbstractWriter {

    private Container container;

    public CloseContainerWriter(Player receiver, Container container) {
        super(receiver);
        this.container = container;
    }

    public void write(OutputStream out) throws IOException {
        CData.writeByte(out, 0x6F);
        CData.writeByte(out, getReceiver().getContainerId(container));
    }
}
