package org.jotserver.ot.net.game.chat;

import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.ot.model.chat.ChatChannel;
import org.jotserver.ot.model.chat.IdentifiableChatChannel;
import org.jotserver.ot.model.chat.SpeakType;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

public class CreatureSayWriter extends AbstractWriter {
	
	private ChatChannel channel;
	private Creature creature;
	private SpeakType type;
	private String text;

	public CreatureSayWriter(Player receiver, ChatChannel channel, Creature creature, SpeakType type, String text) {
		super(receiver);
		
		this.channel = channel;
		this.creature = creature;
		this.type = type;
		this.text = text;
		
	}

	
	public void write(OutputStream out) throws IOException {
		OTDataOutputStream otout = new OTDataOutputStream(out);
		
		otout.writeByte(0xAA);
		otout.writeU32(0x00000000);
		
		otout.writeString(creature.getName());
		
		int level = creature instanceof Player ? 
				((Player)creature).getLevel() : 0;
		otout.writeU16(level);
		
		otout.writeByte(type.getId());
		
		switch(type.getType()) {
		case PUBLIC:
			otout.writePosition(creature.getPosition());
			break;
		case BROADCAST:
		case PRIVATE:
			break;
		case CHANNEL:
			if(channel instanceof IdentifiableChatChannel) {
				otout.writeU16(((IdentifiableChatChannel)channel).getId());
			} else {
				throw new UnsupportedOperationException();
			}
			break;
		default:
			throw new UnsupportedOperationException();
		}
		
		otout.writeString(text);
	}

}
