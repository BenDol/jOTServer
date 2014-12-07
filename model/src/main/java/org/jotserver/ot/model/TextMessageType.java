package org.jotserver.ot.model;

public enum TextMessageType {
	CONSOLE_YELLOW(0x01), //Yellow message in the console
	CONSOLE_LIGHT_BLUE(0x04), //Light blue message in the console
	CONSOLE_ORANGE(0x11), //Orange message in the console
	WARNING(0x12), //Red message in game window and in the console
	EVENT_ADVANCE(0x13), //White message in game window and in the console
	EVENT_DEFAULT(0x14), //White message at the bottom of the game window and in the console
	DEFAULT(0x15), //White message at the bottom of the game window and in the console
	DESCRIPTION(0x16), //Green message in game window and in the console
	STATUS_SMALL(0x17), //White message at the bottom of the game window"
	CONSOLE_BLUE(0x18), //Blue message in the console
	CONSOLE_RED(0x19); //Red message in the console
	
	private int type;
	
	private TextMessageType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	
}
