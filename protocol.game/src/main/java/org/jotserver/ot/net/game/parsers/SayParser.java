package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.jotserver.net.CData;
import org.jotserver.ot.model.chat.ChatManager;
import org.jotserver.ot.model.chat.DefaultChatChannel;
import org.jotserver.ot.model.chat.IdentifiableChatChannel;
import org.jotserver.ot.model.chat.SpeakType;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.PacketType;

public class SayParser extends AbstractParser {
    private static Logger logger = Logger.getLogger(SayParser.class);

    public void parse(PacketType type, InputStream message) throws IOException {
        int speakTypeId = CData.readByte(message);
        String receiver = "";
        int channelId = 0;
        SpeakType speakType = SpeakType.get(speakTypeId);
        switch(speakType.getType()) {
        case PRIVATE:
            receiver = CData.readString(message);
            break;
        case CHANNEL:
            channelId = CData.readU16(message);
            break;
        }

        String text = CData.readString(message);

        ChatManager cm = getWorld().getChatManager();

        switch(speakType.getType()) {
        case PUBLIC:
            publicSay(speakType, text);
            break;
        case PRIVATE:
            Player toPlayer = getWorld().getPlayerByName(receiver);
            toPlayer.getPrivateChannel().speak(getPlayer(), speakType, text);
            break;
        case CHANNEL:
            IdentifiableChatChannel channel = cm.getCreatureChannel(getPlayer(), channelId);
            channel.speak(getPlayer(), speakType, text);
            break;
        case RULEVIOLATION:
            //TODO: Rule violation reports.
            break;
        case BROADCAST:
            cm.getDefaultChannel().broadcast(getPlayer(), speakType, text);
            break;
        case UNKNOWN:
            logger.error("Unknown speak class " + speakType + " from " + getPlayer() + " with text: " + text);
            break;
        }
    }

    private void publicSay(SpeakType speakType, String text) {
        DefaultChatChannel channel = getWorld().getChatManager().getDefaultChannel();
        switch(speakType) {
        case SAY:
        case MONSTER_SAY:
            channel.speak(getPlayer(), speakType, text);
            break;
        case YELL:
        case MONSTER_YELL:
            channel.yell(getPlayer(), speakType, text);
            break;
        case WHISPER:
            channel.whisper(getPlayer(), speakType, text);
            break;
        }
    }
}
