package org.jotserver.ot.net.game.creature;

import org.jotserver.net.CData;
import org.jotserver.ot.model.Outfit;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

import java.io.IOException;
import java.io.OutputStream;

public class CreatureOutfitWriter extends AbstractWriter {
	
	private Outfit outfit;

	public CreatureOutfitWriter(Player receiver, Creature creature) {
		this(receiver, creature.getOutfit());
	}
	
	public CreatureOutfitWriter(Player receiver, Outfit outfit) {
		super(receiver);
		this.outfit = outfit;
	}

	public void write(OutputStream out) throws IOException {
		CData.writeU16(out, outfit.getType().getLook());
		CData.writeByte(out, outfit.getHead());
		CData.writeByte(out, outfit.getBody());
		CData.writeByte(out, outfit.getLegs());
		CData.writeByte(out, outfit.getFeet());
		CData.writeByte(out, outfit.getAddons());
	}

}
