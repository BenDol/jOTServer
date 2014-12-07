package org.jotserver.ot.model.chat;

import java.util.HashMap;
import java.util.Map;

public enum SpeakType {
	SAY				(0x01, Type.PUBLIC), 
	WHISPER			(0x02, Type.PUBLIC), 
	YELL			(0x03, Type.PUBLIC), 
	PRIVATE			(0x04, Type.PRIVATE), 
	CHANNEL_YELLOW	(0x05, Type.CHANNEL), 
	RVR_CHANNEL		(0x06, Type.RULEVIOLATION),
	RVR_ANSWER		(0x07, Type.RULEVIOLATION), 
	RVR_CONTINUE	(0x08, Type.RULEVIOLATION), 
	BROADCAST		(0x09, Type.BROADCAST), 
	CHANNEL_RED1	(0x0A /* Red #c */, Type.CHANNEL),
	PRIVATE_RED		(0x0B, Type.PRIVATE), 
	CHANNEL_ORANGE	(0x0C, Type.CHANNEL), 
	UNKNOWN1		(0x0D, Type.UNKNOWN), 
	CHANNEL_RED2	(0x0E /* Anonymous, #d */, Type.CHANNEL),
	UNKNOWN2		(0x0F, Type.UNKNOWN), 
	MONSTER_SAY		(0x10, Type.PUBLIC), 
	MONSTER_YELL	(0x11, Type.PUBLIC);
	
	private static Map<Integer, SpeakType> lookup = new HashMap<Integer, SpeakType>();
	
	static {
		for(SpeakType c : SpeakType.values()) {
			lookup.put(c.getId(), c);
		}
	}
	
	public static SpeakType get(int id) {
		return lookup.get(id);
	}
	
	private int id;
	private Type type;
	
	private SpeakType(int id, Type type) {
		this.id = id;
		this.type = type;
	}

	public int getId() {
		return id;
	}
	
	public Type getType() {
		return type;
	}
	
	public enum Type {
		PUBLIC, CHANNEL, PRIVATE, RULEVIOLATION, BROADCAST, UNKNOWN;
	}
	
}