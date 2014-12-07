package org.jotserver.ot.net.game.creature;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class TileAddCreatureWriter extends AbstractWriter {
	
	private Position position;
	private Creature creature;

	public TileAddCreatureWriter(Player receiver, Position position, Creature creature) {
		super(receiver);
		this.position = position;
		this.creature = creature;
	}

	public void write(OutputStream out) throws IOException {
		OTDataOutputStream otout = new OTDataOutputStream(out);
		otout.writeByte(0x6A);
		otout.writePosition(position);
		
		new CreatureWriter(getReceiver(), creature).write(out);
	}
}
