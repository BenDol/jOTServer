package org.jotserver.ot.model.chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jotserver.ot.model.creature.Creature;

public class PublicChatChannel implements JoinableChatChannel, IdentifiableChatChannel {
	
	private Set<Creature> creatures;
	
	private String name;
	private int id;

	public PublicChatChannel(int id, String name) {
		this.id = id;
		this.name = name;
		creatures = new HashSet<Creature>();
	}
	
	public boolean invite(Creature creature, Creature invited) {
		return false;
	}
	
	public boolean join(Creature creature) {
		return creatures.add(creature);
	}
	
	public boolean kick(Creature creature, Creature kicked) {
		if(creature == kicked) {
			return creatures.remove(kicked);
		} else {
			return false;
		}
	}
	
	public boolean uninvite(Creature creature, Creature invited) {
		return false;
	}
	
	public boolean speak(Creature creature, SpeakType type, String text) {
		return true;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public Collection<Creature> getMembers() {
		return new ArrayList<Creature>(creatures);
	}

	public boolean isInvited(Creature creature) {
		return true;
	}
}
