package org.jotserver.ot.net.game.out;

import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.ot.model.AnimatedTextColor;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

public class TileAnimatedTextWriter extends AbstractWriter {

	private String text;
	private Tile tile;
	private AnimatedTextColor color;

	public TileAnimatedTextWriter(Player receiver, Tile tile, AnimatedTextColor color, String text) {
		super(receiver);
		this.tile = tile;
		this.color = color;
		this.text = text;
	}

	public void write(OutputStream out) throws IOException {
		OTDataOutputStream otout = new OTDataOutputStream(out);
		otout.writeByte(0x84);
		otout.writePosition(tile.getPosition());
		otout.writeByte(color.getId());
		otout.writeString(text);
	}

}
