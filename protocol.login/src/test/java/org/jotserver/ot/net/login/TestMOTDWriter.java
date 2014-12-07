package org.jotserver.ot.net.login;


import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jotserver.net.CDataInputStream;
import org.jotserver.ot.model.MOTD;
import org.junit.Before;
import org.junit.Test;

public class TestMOTDWriter {

	private static final int NUMBER = 1337;
	private static final String MESSAGE = "This is a message";

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void writeSimple() throws IOException {
		MOTDWriter w = new MOTDWriter(NUMBER, MESSAGE);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		w.write(out);
		
		CDataInputStream in = new CDataInputStream(new ByteArrayInputStream(out.toByteArray()));
		assertEquals(0x14, in.readByte());
		assertEquals(String.valueOf(NUMBER) + "\n" + MESSAGE, in.readString());
	}
	
	@Test
	public void writeChanged() throws IOException {
		MOTDWriter w = new MOTDWriter();
		w.setMessage(MESSAGE);
		w.setNumber(NUMBER);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		w.write(out);
		
		CDataInputStream in = new CDataInputStream(new ByteArrayInputStream(out.toByteArray()));
		assertEquals(0x14, in.readByte());
		assertEquals(String.valueOf(NUMBER) + "\n" + MESSAGE, in.readString());
	}
	
	@Test
	public void writeMOTDObject() throws IOException {
		MOTDWriter w = new MOTDWriter(new MOTD(NUMBER, MESSAGE));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		w.write(out);
		
		CDataInputStream in = new CDataInputStream(new ByteArrayInputStream(out.toByteArray()));
		assertEquals(0x14, in.readByte());
		assertEquals(String.valueOf(NUMBER) + "\n" + MESSAGE, in.readString());
	}
	
}
