package org.jotserver.ot.net.game.creature;

import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

public class CreatureWriter extends AbstractWriter {
	
	private static final int OPBYTE_UNKNOWN = 0x61;
	private static final int OPBYTE_KNOWN = 0x62;
	private Creature creature;
	
	public CreatureWriter(Player player, Creature creature) {
		super(player);
		this.creature = creature;
	}

	public void write(OutputStream out) throws IOException {
		Creature removed = getReceiver().getKnownCreaturesCache().addCreature(creature);
		if(removed == null) {
			CData.writeU16(out, OPBYTE_KNOWN);
			CData.writeU32(out, creature.getId());
		} else {
			long replace = removed == creature ? 0 : removed.getId();
			CData.writeU16(out, OPBYTE_UNKNOWN);
			CData.writeU32(out, replace);
			CData.writeU32(out, creature.getId());
			CData.writeString(out, creature.getName());
		}
		
		CData.writeByte(out, creature.getHealthPercent());
		CData.writeByte(out, creature.getDirection().ordinal());
		
		new CreatureOutfitWriter(getReceiver(), creature).write(out);
		
		CData.writeByte(out, creature.getLight().getLevel());
		CData.writeByte(out, creature.getLight().getColor());
		
		CData.writeU16(out, creature.getSpeed());
		CData.writeByte(out, 0); // TODO: Skull system
		CData.writeByte(out, 0); // TODO: Party Shield system
	}

}
