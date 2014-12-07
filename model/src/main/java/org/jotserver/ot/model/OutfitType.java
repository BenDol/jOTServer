package org.jotserver.ot.model;

public class OutfitType {
	
	public static enum Type {
		FEMALE, MALE, MONSTER, GAMEMASTER, COMMUNITYMANAGER;
	}

	private final boolean enabled;
	private final int look;
	private final Type type;
	private final String name;
	
	public OutfitType(String name, Type type, int look, boolean enabled) {
		this.name = name;
		this.type = type;
		this.look = look;
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getLook() {
		return look;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	
	
}
