package org.jotserver.ot.net.game.chat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.jotserver.net.CData;
import org.jotserver.ot.model.chat.ChatManager;
import org.jotserver.ot.model.chat.IdentifiableChatChannel;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

public class ChannelsDialogWriter extends AbstractWriter {

    private ChatManager chatManager;

    public ChannelsDialogWriter(Player player, ChatManager chatManager) {
        super(player);
        this.chatManager = chatManager;
    }


    public void write(OutputStream out) throws IOException {
        CData.writeByte(out, 0xAB);

        Collection<? extends IdentifiableChatChannel> channels = chatManager.getAvailableChannels(getReceiver());

        CData.writeByte(out, channels.size());

        for(IdentifiableChatChannel channel : channels) {
            CData.writeU16(out, channel.getId());
            CData.writeString(out, channel.getName());
        }
    }

}
