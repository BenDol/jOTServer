package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class TileRemoveThingWriter extends AbstractWriter {
	
	private int stack;
	private Position position;

	public TileRemoveThingWriter(Player receiver, Tile tile, int stack) {
		this(receiver, tile.getPosition(), stack);
	}
	
	public TileRemoveThingWriter(Player receiver, Position position, int stack) {
		super(receiver);
		this.position = position;
		this.stack = stack;
	}

	public void write(OutputStream out) throws IOException {
		OTDataOutputStream otout = new OTDataOutputStream(out);
		otout.writeByte(0x6C);
		otout.writePosition(position);
		otout.writeByte(stack);
	}
}
