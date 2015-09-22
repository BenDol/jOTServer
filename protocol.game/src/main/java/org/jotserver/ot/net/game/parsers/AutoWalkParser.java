package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.creature.CreatureWalkBrain;
import org.jotserver.ot.model.creature.DirectionPath;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.net.game.PacketType;

public class AutoWalkParser extends AbstractParser {
    public void parse(PacketType type, InputStream message) throws IOException {
        Player player = getContext().getPlayer();
        DirectionPath path = new DirectionPath(player.getPosition());
        int size = CData.readByte(message);
        for (int i = 0; i < size; i++) {
            int dir = CData.readByte(message);
            Direction direction = Direction.NONE;

            switch (dir) {
            case 1:
                direction = Direction.EAST;
                break;
            case 2:
                direction = Direction.NORTHEAST;
                break;
            case 3:
                direction = Direction.NORTH;
                break;
            case 4:
                direction = Direction.NORTHWEST;
                break;
            case 5:
                direction = Direction.WEST;
                break;
            case 6:
                direction = Direction.SOUTHWEST;
                break;
            case 7:
                direction = Direction.SOUTH;
                break;
            case 8:
                direction = Direction.SOUTHEAST;
                break;
            default:
                continue;
            }
            path.addStep(direction);
        }
        if (!path.isEmpty()) {
            CreatureWalkBrain walkBrain = new CreatureWalkBrain(player, path);
            player.walk(walkBrain);
            getWorld().getDispatcher().run(walkBrain, walkBrain.getDelay());
        }
    }
}
