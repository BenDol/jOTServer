package org.jotserver.ot.net.game;

import org.jotserver.net.CData;
import org.jotserver.ot.net.AccountLoginParser;

import java.io.IOException;
import java.io.InputStream;

public class CharacterLoginParser extends AccountLoginParser {
	
	private boolean gmLogin;
	private String playerName;

	public CharacterLoginParser() {
		super();
		gmLogin = false;
		playerName = null;
	}

	public CharacterLoginParser(InputStream message) throws IOException {
		super(message);
	}

	public void parse(InputStream in) throws IOException {
		gmLogin = CData.readByte(in) != 0;
		number = CData.readU32(in);
		playerName = CData.readString(in);
		password = CData.readString(in);
	}
	
	public boolean isGmLogin() {
		return gmLogin;
	}

	public String getPlayerName() {
		return playerName;
	}
	
}
