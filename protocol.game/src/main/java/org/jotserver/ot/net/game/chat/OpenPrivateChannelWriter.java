package org.jotserver.ot.net.game.chat;

import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

public class OpenPrivateChannelWriter extends AbstractWriter {
	
	private String receiver;
	
	public OpenPrivateChannelWriter(Player player, String receiver) {
		super(player);
		this.receiver = receiver;
	}
	
	public OpenPrivateChannelWriter(Player player, Creature receiver) {
		super(player);
		this.receiver = receiver.getName();
	}
	
	
	public void write(OutputStream out) throws IOException {
		CData.writeByte(out, 0xAD);
		CData.writeString(out, receiver);
	}

}
