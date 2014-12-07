package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.action.PlayerMoveThingAction;
import org.jotserver.ot.model.util.Dispatcher;
import org.jotserver.ot.model.util.Location;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.net.game.OTDataInputStream;
import org.jotserver.ot.net.game.PacketType;

public class MoveThingParser extends AbstractParser {
	public void parse(PacketType type, InputStream message) throws IOException {
		OTDataInputStream msg = new OTDataInputStream(message);
		
		Position source = msg.readPosition();
		/*int spriteId = */msg.readU16();
		int fromStackPos = msg.readByte();
		Position destination = msg.readPosition();
		int count = msg.readByte();
		
		Location from = findThing(source, fromStackPos);
		Location to = findThing(destination, -1);

		if(to != null && from != null) {
			Dispatcher dispatcher = getWorld().getDispatcher();
			PlayerMoveThingAction action = new PlayerMoveThingAction(dispatcher, getPlayer(), from, to, count);
			if(!action.execute()) {
				getPlayer().getPrivateChannel().sendCancel(action.getError());
			}
		} else {
			getPlayer().getPrivateChannel().sendCancel(ErrorType.NOTPOSSIBLE);
		}
	}
}
