package org.jotserver.ot.net.game.creature;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class CreatureTurnWriter extends AbstractWriter {

    private Creature creature;

    public CreatureTurnWriter(Player receiver, Creature creature) {
        super(receiver);
        this.creature = creature;
    }

    public void write(OutputStream out) throws IOException {
        OTDataOutputStream otout = new OTDataOutputStream(out);
        otout.writeByte(0x6B); // Update tile item?
        otout.writePosition(creature.getPosition());
        otout.writeByte(creature.getTile().getIndexOf(creature));
        otout.writeU16(0x63); /*99*/
        otout.writeU32(creature.getId());
        otout.writeByte(creature.getDirection().ordinal());
    }

}
