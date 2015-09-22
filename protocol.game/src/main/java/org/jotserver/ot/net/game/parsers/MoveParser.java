package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.net.game.PacketType;
import org.jotserver.ot.net.game.out.PlayerCancelWalkWriter;

public class MoveParser extends AbstractParser {
    public void parse(PacketType type, InputStream message) throws IOException {
        Direction dir;
        switch(type) {
        case MOVENORTH:
            dir = Direction.NORTH;
            break;
        case MOVEEAST:
            dir = Direction.EAST;
            break;
        case MOVESOUTH:
            dir = Direction.SOUTH;
            break;
        case MOVEWEST:
            dir = Direction.WEST;
            break;
        case MOVENORTHWEST:
            dir = Direction.NORTHWEST;
            break;
        case MOVENORTHEAST:
            dir = Direction.NORTHEAST;
            break;
        case MOVESOUTHWEST:
            dir = Direction.SOUTHWEST;
            break;
        case MOVESOUTHEAST:
            dir = Direction.SOUTHEAST;
            break;
        default:
            throw new IllegalArgumentException("Unknown packet type.");
        }
        parseMove(dir);
    }

    private void parseMove(Direction dir) {
        Player r = getPlayer();
        Tile tile = r.getTile();
        ErrorType error = tile.queryMoveCreature(r, dir);
        if(error == ErrorType.NONE) {
            tile.executeMoveCreature(r, dir);
        }
        if(error != ErrorType.NONE) {
            getPlayer().getGameProtocol().send(new PlayerCancelWalkWriter(getPlayer()));
            getPlayer().getPrivateChannel().sendCancel(error);
        }
    }
}
