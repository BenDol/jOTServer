package org.jotserver.net.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jotserver.net.BaseServerOutputStream;
import org.junit.Before;
import org.junit.Test;


public class TestBaseServerOutputStream {
	
	private Mockery context = new Mockery();
	
	private IoSession session;
	private BaseServerOutputStream os;
	
	@Before
	public void setUp() {
		session = context.mock(IoSession.class);
		os = new BaseServerOutputStream(session);
	}
	
	@Test
	public void backedByAnIoSession() throws IOException {
		context.checking(new Expectations() {{
			allowing(session).close(false);
		}});
		BaseServerOutputStream os = new BaseServerOutputStream(session);
		assertEquals(session, os.getSession());
		os.close();
	}
	
	@Test
	public void writeOneByte() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).write(IoBuffer.wrap(new byte[] {10}));
		}});
		os.write(10);
		os.flush();
		context.assertIsSatisfied();
	}
	
	@Test
	public void writeMultipleBytesOneAtATime() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).write(IoBuffer.wrap(new byte[] {10, 11, 12, 13, 14, 15}));
		}});
		os.write(10);
		os.write(11);
		os.write(12);
		os.write(13);
		os.write(14);
		os.write(15);
		os.flush();
		context.assertIsSatisfied();
	}
	
	@Test
	public void writeMultipleBytesIneOneChunk() throws IOException {
		final byte[] buf = new byte[] {10, 11, 12, 13, 14, 15};
		context.checking(new Expectations() {{
			oneOf(session).write(IoBuffer.wrap(buf));
		}});
		os.write(buf);
		os.flush();
		context.assertIsSatisfied();
	}
	
	@Test
	public void writeMultipleBytesIneOneChunk2() throws IOException {
		byte[] buf = new byte[] {10, 11, 12, 13, 14, 15};
		final byte[] buf2 = Arrays.copyOfRange(buf, 1, buf.length-1);
		context.checking(new Expectations() {{
			oneOf(session).write(IoBuffer.wrap(buf2));
		}});
		os.write(buf, 1, buf.length-2);
		os.flush();
		context.assertIsSatisfied();
	}
	
	@Test
	public void closingStreamClosesUnderlyingIoSession() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).close(false);
		}});
		os.close();
		context.assertIsSatisfied();
	}
	
	@Test
	public void sizeOfStreamWhenEmpty() {
		assertEquals(0, os.size());
	}
	
	@Test
	public void sizeOfStreamAfterWritingOnce() throws IOException {
		os.write(10);
		assertEquals(1, os.size());
	}
	
	@Test
	public void sizeOfStreamAfterWritingSeveralTimes() throws IOException {
		os.write(10);
		os.write(11);
		os.write(12);
		os.write(13);
		os.write(14);
		os.write(15);
		assertEquals(6, os.size());
	}
	
	@Test
	public void sizeOfStreamAfterFlushing() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).write(IoBuffer.wrap(new byte[] {10, 11, 12}));
		}});
		os.write(10);
		os.write(11);
		os.write(12);
		os.flush();
		os.write(13);
		os.write(14);
		os.write(15);
		assertEquals(3, os.size());
		context.assertIsSatisfied();
	}
	
}
