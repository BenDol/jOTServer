package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.net.game.PacketType;

public class CloseContainerParser extends AbstractParser {
    public void parse(PacketType type, InputStream message) throws IOException {
        int containerId = CData.readByte(message);
        Container container = getPlayer().getContainer(containerId);
        if(container != null) {
            container.close(getPlayer());
        }
    }
}
