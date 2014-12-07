package org.jotserver.ot.net;


import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jotserver.net.CData;
import org.junit.Before;
import org.junit.Test;

public class TestDisconnectClientWriter {

	private static final String MESSAGE = "Some Message";

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void writeDefaultTypedMessage() throws IOException {
		DisconnectClientWriter w = new DisconnectClientWriter(MESSAGE);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		w.write(out);
		ByteArrayOutputStream ref = new ByteArrayOutputStream();
		CData.writeByte(ref, 0x0A);
		CData.writeString(ref, MESSAGE);
		assertArrayEquals(ref.toByteArray(), out.toByteArray());
	}
	
	@Test
	public void writeModifiedMessage() throws IOException {
		DisconnectClientWriter w = new DisconnectClientWriter("Some other message");
		int type = 0x0F;
		w.setError(type);
		w.setMessage(MESSAGE);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		w.write(out);
		ByteArrayOutputStream ref = new ByteArrayOutputStream();
		CData.writeByte(ref, type);
		CData.writeString(ref, MESSAGE);
		assertArrayEquals(ref.toByteArray(), out.toByteArray());
	}
	
}
