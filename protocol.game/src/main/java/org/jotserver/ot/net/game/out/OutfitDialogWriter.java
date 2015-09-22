package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.OutfitType;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;
import org.jotserver.ot.net.game.creature.CreatureOutfitWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

public class OutfitDialogWriter extends AbstractWriter {

    private Collection<OutfitType> outfits;

    public OutfitDialogWriter(Player receiver, Collection<OutfitType> outfits) {
        super(receiver);
        this.outfits = outfits;
    }

    public void write(OutputStream out) throws IOException {
        OTDataOutputStream otout = new OTDataOutputStream(out);
        otout.writeByte(0xC8);
        new CreatureOutfitWriter(getReceiver(), getReceiver().getOutfit()).write(out);

        if(!outfits.isEmpty()) {
            int count = Math.min(25, outfits.size());
            otout.writeByte(count);
            Iterator<OutfitType> it = outfits.iterator();
            for(; it.hasNext() && count > 0; count--) {
                OutfitType outfit = it.next();
                otout.writeU16(outfit.getLook());
                otout.writeString(outfit.getName());
                otout.writeByte(0); // TODO: Addons.
            }
        }
    }
}
