package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.net.game.PacketType;

public class TurnParser extends AbstractParser {
    public void parse(PacketType type, InputStream message) throws IOException {
        Direction dir;
        switch(type) {
        case TURNNORTH:
            dir = Direction.NORTH;
            break;
        case TURNEAST:
            dir = Direction.EAST;
            break;
        case TURNSOUTH:
            dir = Direction.SOUTH;
            break;
        case TURNWEST:
            dir = Direction.WEST;
            break;
        default:
            throw new RuntimeException("Unknown packet type.");
        }
        getPlayer().turn(dir);
    }
}
