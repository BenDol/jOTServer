package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;
import org.jotserver.ot.net.game.creature.CreatureWriter;

import java.io.IOException;
import java.io.OutputStream;

public class TileWriter extends AbstractWriter {

    private Tile tile;

    public TileWriter(Player receiver, Tile tile) {
        super(receiver);
        this.tile = tile;
    }

    public void write(OutputStream out) throws IOException {
        OTDataOutputStream otout = new OTDataOutputStream(out);
        // TODO: Maxcount for tile things!

        if(tile.getGround() != null) {
            otout.writeItem(tile.getGround());
        }

        for(Item item : tile.getTopItems()) {
            otout.writeItem(item);
        }

        for(Creature p : tile.getCreatures()) {
            new CreatureWriter(getReceiver(), p).write(out);
        }

        for(Item item : tile.getDownItems()) {
            otout.writeItem(item);
        }
    }

}
