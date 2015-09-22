package org.jotserver.ot.net.game.chat;

import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.net.CData;
import org.jotserver.ot.model.chat.IdentifiableChatChannel;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;

public class OpenChannelWriter extends AbstractWriter {

    public static enum ChannelType {
        PUBLIC(0xAC), PRIVATE(0xB2);

        public final int opbyte;
        private ChannelType(int opbyte) {
            this.opbyte = opbyte;
        }
    }

    private IdentifiableChatChannel channel;
    private ChannelType type;

    public OpenChannelWriter(Player receiver, ChannelType type, IdentifiableChatChannel channel) {
        super(receiver);
        this.channel = channel;
        this.type = type;
    }


    public void write(OutputStream out) throws IOException {
        CData.writeByte(out, type.opbyte);
        CData.writeU16(out, channel.getId());
        CData.writeString(out, channel.getName());
    }

}
