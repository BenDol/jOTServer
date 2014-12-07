package org.jotserver.ot.net.io;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.net.CData;
import org.junit.Before;
import org.junit.Test;

public class TestMessageInputStream {

	private static final byte[] DATA_ARRAY = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
	private Mockery context;
	private MessageInputStream empty;
	private ByteArrayInputStream data;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		
		empty = new MessageInputStream(new ByteArrayInputStream(new byte[] {0x00, 0x00}));
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		CData.writeU16(out, DATA_ARRAY.length);
		out.write(DATA_ARRAY);
		data = new ByteArrayInputStream(out.toByteArray());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void unmarkableStreamNotAccepted() throws IllegalArgumentException, IOException {
		final InputStream unmarkable = context.mock(InputStream.class);
		context.checking(new Expectations() {{
			oneOf(unmarkable).markSupported(); returnValue(false);
		}});
		new MessageInputStream(unmarkable);
	}
	
	@Test(expected = IOException.class)
	public void insufficientHeaderDataAvailable() throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(new byte[] {0x00});
		new MessageInputStream(in);
	}
	
	@Test(expected = IOException.class)
	public void notEnoughBodyDataAvailable() throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(new byte[] {0x01, 0x00});
		new MessageInputStream(in);
	}
	
	@Test
	public void constructValidStream() throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(new byte[] {0x01, 0x00, 0x00});
		MessageInputStream min = new MessageInputStream(in);
		assertEquals(1, min.available());
	}
	
	@Test
	public void readSingleBytes() throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(new byte[] {0x01, 0x00, 0x12});
		MessageInputStream min = new MessageInputStream(in);
		assertEquals(0x12, min.read());
		assertEquals(-1, min.read());
	}
	
	@Test
	public void closeClosesUnderlyingStream() throws IOException {
		ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] {0x00, 0x00});
		final InputStream mi = context.mock(InputStream.class);
		context.checking(new Expectations() {{
			oneOf(mi).close();
		}});
		MessageInputStream min = new MessageInputStream(bin) {{
			in = mi;
		}};
		min.close();
		context.assertIsSatisfied();
	}
	
	@Test
	public void markSupported() {
		assertTrue(empty.markSupported());
	}
	
	@Test
	public void markAndResetWorks() throws IOException {
		MessageInputStream in = new MessageInputStream(data);
		assertEquals(DATA_ARRAY.length, in.available());
		assertEquals(DATA_ARRAY[0], in.read());
		in.mark(5);
		assertEquals(DATA_ARRAY.length-1, in.available());
		assertEquals(DATA_ARRAY[1], in.read());
		assertEquals(DATA_ARRAY[2], in.read());
		assertEquals(DATA_ARRAY.length-3, in.available());
		in.reset();
		assertEquals(DATA_ARRAY.length-1, in.available());
		assertEquals(DATA_ARRAY[1], in.read());
		assertEquals(DATA_ARRAY[2], in.read());
		assertEquals(DATA_ARRAY.length-3, in.available());
	}
	
	@Test
	public void skipWorks() throws IOException {
		MessageInputStream in = new MessageInputStream(data);
		assertEquals(3, in.skip(3));
		assertEquals(DATA_ARRAY[3], in.read());
	}
	
	@Test
	public void skipTooManyBytes() throws IOException {
		MessageInputStream in = new MessageInputStream(data);
		assertEquals(DATA_ARRAY.length, in.skip(DATA_ARRAY.length+1));
		assertEquals(-1, in.read());
	}
	
	@Test
	public void readArrayFromEmpty() throws IOException {
		byte[] b = new byte[10];
		assertEquals(-1, empty.read(b));
		assertEquals(-1, empty.read(b, 0, 10));
	}
	
	@Test
	public void readArrayWithinAvailable() throws IOException {
		MessageInputStream in = new MessageInputStream(data);
		byte[] b = new byte[DATA_ARRAY.length];
		assertEquals(b.length, in.available());
		assertEquals(b.length, in.read(b));
		assertEquals(0, in.available());
	}
	
	@Test
	public void readMoreThenAvailable() throws IOException {
		MessageInputStream in = new MessageInputStream(data);
		byte[] b = new byte[DATA_ARRAY.length+1];
		assertEquals(DATA_ARRAY.length, in.available());
		assertEquals(DATA_ARRAY.length, in.read(b));
		assertEquals(0, in.available());
	}
}
