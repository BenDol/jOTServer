package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.ot.net.game.PacketType;

public class StopWalkParser extends AbstractParser {
    public void parse(PacketType type, InputStream message) throws IOException {
        getContext().getPlayer().cancelWalk();
    }
}
