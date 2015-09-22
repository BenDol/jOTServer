package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.ot.model.util.Location;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.net.game.OTDataInputStream;
import org.jotserver.ot.net.game.PacketType;

public class LookParser extends AbstractParser {
    public void parse(PacketType type, InputStream message) throws IOException {
        OTDataInputStream msg = new OTDataInputStream(message);
        Position position = msg.readPosition();
        /*int spriteId = */msg.readU16();
        int stack = msg.readByte();

        Location location = findThing(position, stack);

        if(location != null && location.get() != null) {
            getPlayer().lookAt(location.get());
        }
    }
}
