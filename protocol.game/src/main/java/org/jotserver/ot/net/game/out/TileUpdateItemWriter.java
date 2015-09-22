package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class TileUpdateItemWriter extends AbstractWriter {

    private Position position;
    private int stack;
    private Item item;

    public TileUpdateItemWriter(Player receiver, Position position, int stack, Item item) {
        super(receiver);
        this.position = position;
        this.stack = stack;
        this.item = item;
    }

    public void write(OutputStream out) throws IOException {
        OTDataOutputStream otout = new OTDataOutputStream(out);

        otout.writeByte(0x6B);
        otout.writePosition(position);
        otout.writeByte(stack);
        otout.writeItem(item);
    }
}
