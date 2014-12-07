package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.chat.ChatManager;
import org.jotserver.ot.model.chat.IdentifiableChatChannel;
import org.jotserver.ot.model.chat.JoinableChatChannel;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.PacketType;
import org.jotserver.ot.net.game.chat.ChannelsDialogWriter;

public class ChannelParser extends AbstractParser {
	public void parse(PacketType type, InputStream message) throws IOException {
		switch(type) {
		case REQUESTCHANNELS:
			parseRequestChannels();
			break;
		case OPENCHANNEL:
			parseOpenChannel(message);
			break;
		case CLOSECHANNEL:
			parseCloseChannel(message);
			break;
		case OPENPRIVATECHANNEL:
			parseOpenPrivateChannel(message);
			break;
		default:
			throw new IllegalArgumentException("Unknown package type.");
		}
	}

	private void parseRequestChannels() {
		getPlayer().getGameProtocol().send(new ChannelsDialogWriter(getPlayer(), getWorld().getChatManager()));
	}

	private void parseOpenPrivateChannel(InputStream message)
			throws IOException {
		String receiver = CData.readString(message);
		Player p = getWorld().getPlayerByName(receiver);
		if(p != null) {
			p.getPrivateChannel().join(getPlayer());
		}
	}
	
	private void parseOpenChannel(InputStream message) throws IOException {
		int channelId = CData.readU16(message);
		
		ChatManager chatManager = getWorld().getChatManager();
		IdentifiableChatChannel channel = chatManager.getCreatureChannel(getPlayer(), channelId);
		if(channel != null && channel instanceof JoinableChatChannel) {
			((JoinableChatChannel)channel).join(getPlayer());
		}
	}
	
	private void parseCloseChannel(InputStream message) throws IOException {
		int channelId = CData.readU16(message);
		
		ChatManager chatManager = getWorld().getChatManager();
		IdentifiableChatChannel channel = chatManager.getCreatureChannel(getPlayer(), channelId);
		if(channel != null && channel instanceof JoinableChatChannel) {
			((JoinableChatChannel)channel).kick(getPlayer(), getPlayer());
		}
	}
}
