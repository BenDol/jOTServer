package org.jotserver.ot.model.chat;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.jotserver.ot.model.creature.Creature;

public class PrivateChatChannel implements JoinableChatChannel, IdentifiableChatChannel, OwnedChatChannel {

    private LinkedList<Creature> members;
    private Creature owner;
    private int id;

    public PrivateChatChannel(int id, Creature owner) {
        this.id = id;
        this.owner = owner;
        members = new LinkedList<Creature>();
    }

    public String getName() {
        return owner.getName() + "'s Channel";
    }

    public boolean speak(Creature creature, SpeakType type, String text) {
        return false;
    }

    public Creature getOwner() {
        return owner;
    }

    public boolean close(Creature creature) {
        return false;
    }

    public int getId() {
        return id;
    }

    public Collection<Creature> getMembers() {
        return Collections.unmodifiableCollection(members);
    }

    public boolean invite(Creature creature, Creature invited) {
        return false;
    }

    public boolean join(Creature creature) {
        return false;
    }

    public boolean kick(Creature creature, Creature kicked) {
        return false;
    }

    public boolean uninvite(Creature creature, Creature invited) {
        return false;
    }

    public boolean isInvited(Creature creature) {
        return owner.equals(creature);
    }
}
