package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class TileAddItemWriter extends AbstractWriter {

	private Item item;
	private Tile tile;

	public TileAddItemWriter(Player receiver, Tile tile, Item item) {
		super(receiver);
		this.tile = tile;
		this.item = item;
	}

	public void write(OutputStream out) throws IOException {
		OTDataOutputStream otout = new OTDataOutputStream(out);
		otout.writeByte(0x6A);
		otout.writePosition(tile.getPosition());
		otout.writeItem(item);
	}
}
