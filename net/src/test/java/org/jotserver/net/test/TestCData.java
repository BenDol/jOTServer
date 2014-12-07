package org.jotserver.net.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.jotserver.net.CData;
import org.junit.Test;

public class TestCData {

	private static final int LONG = 13371337;
	private static final byte[] LONG_BYTES = new byte[] { 
							(byte)(LONG & 0x000000FFL), 
							(byte)((LONG & 0x0000FF00L) >> 8),
							(byte)((LONG & 0x00FF0000L) >> 16), 
							(byte)((LONG & 0xFF000000L) >> 24)
							};
	private static final int BYTE = 123;
	private static final byte[] BYTE_BYTES = new byte[] { (byte)(BYTE & 0xFF) };
	private static final int INTEGER = 1337;
	private static final byte[] INTEGER_BYTES = new byte[] { (byte)(INTEGER & 0x00FF), (byte)((INTEGER & 0xFF00) >> 8) };

	@Test
	public void readString() throws IOException {
		String str = "TestString";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] bytes = str.getBytes("ISO-8859-1");
		CData.writeU16(out, bytes.length);
		out.write(bytes);
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		String result = CData.readString(in);
		assertEquals(str, result);
	}

	@Test
	public void readByte() throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(BYTE_BYTES);
		int result = CData.readByte(in);
		assertEquals(BYTE, result);
	}

	@Test
	public void readU16() throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(INTEGER_BYTES);
		int result = CData.readU16(in);
		assertEquals(INTEGER, result);
	}

	@Test
	public void readU32() throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(LONG_BYTES);
		long result = CData.readU32(in);
		assertEquals(LONG, result);
	}

	@Test
	public void writeU16() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		CData.writeU16(out, INTEGER);
		assertArrayEquals(INTEGER_BYTES, out.toByteArray());
	}

	@Test
	public void testWriteU32() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		CData.writeU32(out, LONG);
		assertArrayEquals(LONG_BYTES, out.toByteArray());
	}

	@Test
	public void testWriteByte() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		CData.writeByte(out, BYTE);
		assertArrayEquals(BYTE_BYTES, out.toByteArray());
	}

	@Test
	public void testWriteString() throws IOException {
		String str = "TestString";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] bytes = str.getBytes("ISO-8859-1");
		CData.writeU16(out, bytes.length);
		out.write(bytes);
		
		ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		CData.writeString(out2, str);
		assertArrayEquals(out.toByteArray(), out2.toByteArray());
	}

}
