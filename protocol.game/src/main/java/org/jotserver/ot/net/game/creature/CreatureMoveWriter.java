package org.jotserver.ot.net.game.creature;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class CreatureMoveWriter extends AbstractWriter {

    private int fromStack;
    private Position from;
    private Position to;

    public CreatureMoveWriter(Player receiver, Position from, int fromStack, Creature creature) {
        this(receiver, from, fromStack, creature.getPosition());
    }

    public CreatureMoveWriter(Player receiver, Position from, int fromStack, Position to) {
        super(receiver);
        this.from = from;
        this.fromStack = fromStack;
        this.to = to;
    }

    public void write(OutputStream out) throws IOException {
        OTDataOutputStream otout = new OTDataOutputStream(out);
        otout.writeByte(0x6D);
        otout.writePosition(from);
        otout.writeByte(fromStack);
        otout.writePosition(to);
    }

}
