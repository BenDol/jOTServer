package org.jotserver.ot.net.game.handler;

import org.jotserver.ot.model.TextMessageType;
import org.jotserver.ot.model.chat.*;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.Writer;
import org.jotserver.ot.net.game.chat.CreatureSayWriter;
import org.jotserver.ot.net.game.chat.OpenChannelWriter;
import org.jotserver.ot.net.game.chat.OpenPrivateChannelWriter;
import org.jotserver.ot.net.game.out.TextMessageWriter;

public aspect ChatHandler {

    public pointcut creatureSay(DefaultChatChannel channel, Creature creature, SpeakType type, String text) :
        target(channel) &&
        args(creature, type, text) &&
        (execution(public boolean ChatChannel.speak(Creature, SpeakType, String)) ||
                execution(public boolean DefaultChatChannel.broadcast(Creature, SpeakType, String)) ||
                execution(public boolean DefaultChatChannel.yell(Creature, SpeakType, String)) ||
                execution(public boolean DefaultChatChannel.whisper(Creature, SpeakType, String)));

    public pointcut joinChatChannel(Creature creature, JoinableChatChannel channel) :
        target(channel) &&
        args(creature) &&
        execution(public boolean JoinableChatChannel.join(Creature));

    public pointcut talkToChannel(PublicChatChannel channel, Creature creature, SpeakType type, String text) :
        target(channel) &&
        args(creature, type, text) &&
        execution(public boolean ChatChannel.speak(Creature, SpeakType, String));

    public pointcut talkToCreature(PrivateMessageChannel channel, Creature creature, SpeakType type, String text) :
        target(channel) &&
        args(creature, type, text) &&
        execution(public boolean ChatChannel.speak(Creature, SpeakType, String));

    public pointcut messageToCreature(PrivateMessageChannel channel, TextMessageType type, String text) :
        target(channel) &&
        args(type, text) &&
        execution(public void PrivateMessageChannel.sendMessage(TextMessageType, String));


    /*
     * DefaultChatChannel.speak/.broadcast/.yell/.whisper
     */
    after(DefaultChatChannel channel, Creature creature, SpeakType type, String text) returning(boolean ret) :
            creatureSay(channel, creature, type, text) {

        if(ret && creature.isPlaced()) {
            Iterable<Player> receivers = null;
            if(type.getType() == SpeakType.Type.BROADCAST) {
                receivers = creature.getTile().getGameWorld().getPlayers();
            } else if(type.getType() == SpeakType.Type.PUBLIC) {
                switch(type) {
                case SAY:
                case MONSTER_SAY:
                    receivers = creature.getSpectators(Player.class);
                    break;
                case YELL:
                case MONSTER_YELL:
                    receivers = creature.getSpectators(Player.class);
                    break;
                case WHISPER:
                    receivers = creature.getSpectators(Player.class);
                    break;
                }
            }
            if(receivers != null) {
                for(Player player : receivers) {
                    if(player.isOnline()) {
                        player.getGameProtocol().send(new CreatureSayWriter(player, channel, creature, type, text));
                    }
                }
            }
        }
    }

    /*
     * JoinableChatChannel.join
     */
    after(Creature creature, JoinableChatChannel channel) returning(boolean ret) : joinChatChannel(creature, channel) {
        if(creature instanceof Player) {
            Player player = (Player)creature;
            if(player.isOnline() ){
                Writer writer = null;
                if(channel instanceof PublicChatChannel) {
                    writer = new OpenChannelWriter(player, OpenChannelWriter.ChannelType.PUBLIC, (PublicChatChannel)channel);
                } else if(channel instanceof PrivateMessageChannel) {
                    writer = new OpenPrivateChannelWriter(player, ((PrivateMessageChannel)channel).getOwner().getName());
                }
                if(writer != null) {
                    player.getGameProtocol().send(writer);
                }
            }
        }
    }

    /*
     * PublicChatChannel.speak
     */
    after(PublicChatChannel channel, Creature creature, SpeakType type, String text) returning(boolean ret) :
            talkToChannel(channel, creature, type, text) {
        if(ret) {
            for(Creature c : channel.getMembers()) {
                if(c instanceof Player) {
                    Player p = (Player)c;
                    if(p.isOnline()) {
                        p.getGameProtocol().send(new CreatureSayWriter(p, channel, creature, type, text));
                    }
                }
            }
        }
    }

    /*
     * PrivateMessageChannel.speak
     */
    after(PrivateMessageChannel channel, Creature creature, SpeakType type, String text) returning(boolean ret) :
            talkToCreature(channel, creature, type, text) {

        if(channel.getOwner() instanceof Player) {
            Player player = (Player)channel.getOwner();
            if(player.isOnline()) {
                player.getGameProtocol().send(new CreatureSayWriter(player, channel, creature, type, text));
            }
        }

    }

    /*
     * PrivateMessageChannel.sendMessage
     */
    after(PrivateMessageChannel channel, TextMessageType type, String text) returning :
                messageToCreature(channel, type, text) {

        Creature owner = channel.getOwner();
        if(owner instanceof Player) {
            Player player = (Player)owner;
            if(player.isOnline()) {
                player.getGameProtocol().send(new TextMessageWriter(player, type, text));
            }
        }
    }
}