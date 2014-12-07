package org.jotserver.ot.net.login;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.jotserver.net.CDataInputStream;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.player.Premium;
import org.jotserver.ot.model.world.BaseGameWorld;
import org.junit.Before;
import org.junit.Test;

public class TestCharacterListWriter {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void writesEmptyListNoPremiumByDefault() throws IOException {
		CharacterListWriter w = new CharacterListWriter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		w.write(out);
		CDataInputStream in = new CDataInputStream(new ByteArrayInputStream(out.toByteArray()));
		assertEquals(0x64, in.readByte());
		assertEquals(0, in.readByte());
		assertEquals(0, in.readU16());
		assertEquals(0, in.available());
	}
	
	@Test
	public void writeSingleCharacter() throws IOException {
		InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 7171);
		BaseGameWorld world = new BaseGameWorld("world", "MyWorld", addr, true);
		Player player = new Player(0, "Test", world);
		CharacterListWriter w = new CharacterListWriter(new Premium(), Arrays.asList(player));
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		w.write(out);
		
		CDataInputStream in = new CDataInputStream(new ByteArrayInputStream(out.toByteArray()));
		assertEquals(0x64, in.readByte());
		assertEquals(1, in.readByte());
		assertEquals(player.getName(), in.readString());
		assertEquals(world.getName(), in.readString());
		byte[] a = world.getAddress().getAddress().getAddress();
		byte[] b = new byte[a.length];
		assertEquals(b.length, in.read(b));
		assertArrayEquals(a, b);
		assertEquals(world.getAddress().getPort(), in.readU16());
		
		assertEquals(0, in.readU16());
		assertEquals(0, in.available());
		
	}
	
	@Test
	public void changePremiumAndCharacters() throws IOException {
		Player player = new Player(0, "Test", null);
		CharacterListWriter w = new CharacterListWriter(new Premium(), Arrays.asList(player));
		
		w.setPremium(new Premium(Calendar.getInstance().getTimeInMillis() + 24*60*60*1000 + 1000));
		w.setCharacters(new ArrayList<Player>());
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		w.write(out);
		
		CDataInputStream in = new CDataInputStream(new ByteArrayInputStream(out.toByteArray()));
		assertEquals(0x64, in.readByte());
		assertEquals(0, in.readByte());
		
		assertEquals(1, in.readU16());
		assertEquals(0, in.available());
		
	}
	
}
