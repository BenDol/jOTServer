package org.jotserver.ot.net.game.out;

import org.jotserver.net.CData;
import org.jotserver.ot.model.Light;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

import java.io.IOException;
import java.io.OutputStream;

public class LightWriter extends AbstractWriter {
	
	private static final int OPBYTE_CREATURE = 0x8D;
	private static final int OPBYTE_WORLD = 0x82;
	private Light light;
	private Creature creature;
	
	public LightWriter(Player receiver, Light light) {
		super(receiver);
		this.light = light;
		creature = null;
	}
	
	public LightWriter(Player receiver, Creature player, Light light) {
		this(receiver, light);
		this.creature = player;
	}

	public void write(OutputStream out) throws IOException {
		if(creature == null) {
			CData.writeByte(out, OPBYTE_WORLD);
		} else {
			CData.writeByte(out, OPBYTE_CREATURE);
			CData.writeU32(out, creature.getId());
		}
		CData.writeByte(out, light.getLevel());
		CData.writeByte(out, light.getColor());
	}
}
