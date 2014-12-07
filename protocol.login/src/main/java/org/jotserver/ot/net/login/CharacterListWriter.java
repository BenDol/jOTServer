package org.jotserver.ot.net.login;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.jotserver.net.CData;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.player.PlayerList;
import org.jotserver.ot.model.player.Premium;
import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.net.Writer;

public class CharacterListWriter implements Writer {
	
	private static final int OPBYTE = 0x64;
	
	private Premium premium;
	private Collection<Player> characters;
	
	public CharacterListWriter() {
		premium = new Premium();
		characters = new PlayerList();
	}
	
	public CharacterListWriter(Premium premium, Collection<Player> characters) {
		this();
		this.premium = premium;
		this.characters = characters;
	}
	
	
	public void write(OutputStream out) throws IOException {
		CData.writeByte(out, OPBYTE);
		writeCharacters(out);
		CData.writeU16(out, premium.getDaysLeft());
	}

	private void writeCharacters(OutputStream out) throws IOException {
		CData.writeByte(out, characters.size());
		for(Player character : characters) {
			writeCharacter(out, character);
		}
	}

	private void writeCharacter(OutputStream out, Player character) throws IOException {
		CData.writeString(out, character.getName());
		GameWorld world = character.getWorld();
		/*if(world == null) {
			world = new BaseGameWorld("", "Unknown", new InetSocketAddress("localhost", 7171), false);
			// TODO: Print error, and probably convert this into a null object solution.
		}*/
		CData.writeString(out, world.getName());
		out.write(world.getAddress().getAddress().getAddress());
		CData.writeU16(out, world.getAddress().getPort());
	}

	public void setPremium(Premium premium) {
		this.premium = premium;
	}

	public void setCharacters(Collection<Player> characters) {
		this.characters = characters;
	}

}
