package org.jotserver.ot.net.game.creature;

import org.jotserver.net.CData;
import org.jotserver.ot.model.Outfit;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

import java.io.IOException;
import java.io.OutputStream;

public class CreatureChangeOutfitWriter extends AbstractWriter {

    private Outfit outfit;
    private Creature creature;

    public CreatureChangeOutfitWriter(Player receiver, Creature creature) {
        super(receiver);
        this.creature = creature;
        this.outfit = creature.getOutfit();
    }

    public void write(OutputStream out) throws IOException {
        CData.writeByte(out, 0x8E);
        CData.writeU32(out, creature.getId());
        new CreatureOutfitWriter(getReceiver(), outfit).write(out);
    }

}
