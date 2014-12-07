package org.jotserver.ot.net.game.creature;

import org.jotserver.net.CData;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

import java.io.IOException;
import java.io.OutputStream;

public class PlayerStatsWriter extends AbstractWriter {
	
	private static final int OPBYTE = 0xA0;
	
	public PlayerStatsWriter(Player player) {
		super(player);
	}

	public void write(OutputStream out) throws IOException {
		CData.writeByte(out, OPBYTE);
		Player player = getReceiver();
		
		CData.writeU16(out, player.getHealth());
		CData.writeU16(out, player.getMaxHealth());
		
		CData.writeU16(out, player.getFreeCapacity());
		
		CData.writeU32(out, player.getExperience());
		CData.writeU16(out, player.getLevel());
		CData.writeByte(out, 0); // TODO: Level percent
		
		CData.writeU16(out, player.getMana());
		CData.writeU16(out, player.getMaxMana());
		
		CData.writeByte(out, player.getMagicLevel());
		CData.writeByte(out, 0); // TODO: Magic level percent
		CData.writeByte(out, player.getSoul());
		CData.writeU16(out, player.getStamina());
	}

}
