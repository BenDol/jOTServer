package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.Effect;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class TileEffectWriter extends AbstractWriter {

    private Effect effect;
    private Tile tile;

    public TileEffectWriter(Player receiver, Tile tile, Effect effect) {
        super(receiver);
        this.tile = tile;
        this.effect = effect;
    }

    public void write(OutputStream out) throws IOException {
        OTDataOutputStream otout = new OTDataOutputStream(out);
        otout.writeByte(0x83);
        otout.writePosition(tile.getPosition());
        otout.writeByte(effect.getType());
    }
}
