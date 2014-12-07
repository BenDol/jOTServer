package org.jotserver.ot.net.game.creature;

import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

public class CreatureHealthWriter extends AbstractWriter {

	private Creature creature;

	public CreatureHealthWriter(Player receiver, Creature creature) {
		super(receiver);
		this.creature = creature;
	}
	
	public void write(OutputStream out) throws IOException {
		CData.writeByte(out, 0x8C);
		CData.writeU32(out, creature.getId());
		CData.writeByte(out, creature.getHealthPercent());
	}
	
}
