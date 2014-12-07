package org.jotserver.ot.net.game.out;

import org.jotserver.net.CData;
import org.jotserver.ot.model.TextMessageType;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

import java.io.IOException;
import java.io.OutputStream;

public class TextMessageWriter extends AbstractWriter {

	private String message;
	private TextMessageType type;

	public TextMessageWriter(Player receiver, TextMessageType type, String message) {
		super(receiver);
		this.type = type;
		this.message = message;
	}

	public void write(OutputStream out) throws IOException {
		CData.writeByte(out, 0xB4);
		CData.writeByte(out, type.getType());
		CData.writeString(out, message);
	}
}
