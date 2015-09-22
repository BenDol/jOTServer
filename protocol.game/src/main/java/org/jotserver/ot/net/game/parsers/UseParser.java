package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.action.PlayerUseThingAction;
import org.jotserver.ot.model.util.Location;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.net.game.OTDataInputStream;
import org.jotserver.ot.net.game.PacketType;

public class UseParser extends AbstractParser {
    public void parse(PacketType type, InputStream message) throws IOException {
        OTDataInputStream in = new OTDataInputStream(message);
        Position position = in.readPosition();
        /*int spriteId = */in.readU16();
        int stackpos = in.readByte();
        int index = in.readByte();
        getPlayer().suggestNextContainerId(index);
        //boolean isHotkey = (pos.x == 0xFFFF && pos.y == 0 && pos.z == 0);

        Location location = findThing(position, stackpos);

        if(location != null && location.get() != null) {
            Thing thing = location.get();
            if(thing != null) {
                PlayerUseThingAction use = new PlayerUseThingAction(getWorld().getDispatcher(), getPlayer(), thing);
                if(!use.execute()) {
                    getPlayer().getPrivateChannel().sendCancel(use.getError());
                }
            }
        }
    }
}
