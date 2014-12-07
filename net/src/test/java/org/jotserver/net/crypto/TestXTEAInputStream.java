package org.jotserver.net.crypto;


import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.net.encryption.XTEAEncryptionEngine;
import org.jotserver.net.encryption.XTEAInputStream;
import org.junit.Before;
import org.junit.Test;

public class TestXTEAInputStream extends TestXTEA {
	
	private Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
	
	private XTEAInputStream in;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		in = new XTEAInputStream(new ByteArrayInputStream(new byte[] {}), xtea);
	}
	
	private void setUp(byte[] data) {
		in = new XTEAInputStream(new ByteArrayInputStream(data), xtea);
	}
	
	@Test
	public void availableReportsAvailableLengthOfUnderlyingStream() throws IOException {
		setUp(dataChunk);
		assertEquals(dataChunk.length, in.available());
	}
	
	@Test
	public void readWhenEmptyReturnsMinusOne() throws IOException {
		assertEquals(-1, in.read());
	}
	
	@Test
	public void closeClosesUnderlyingStream() throws IOException {
		final InputStream inputStream = context.mock(InputStream.class);
		in = new XTEAInputStream(inputStream, xtea);
		context.checking(new Expectations() {{
			oneOf(inputStream).close();
		}});
		in.close();
	}
	
	@Test
	public void markAndResetAreSupported() throws IOException {
		assertTrue(in.markSupported());
		in.mark(0);
		in.reset();
	}
	
	@Test
	public void markAndResetWithinOneBlock() throws IOException {
		setUp(dataChunk);
		in.read();
		in.mark(10);
		int a = in.read();
		in.read();
		in.reset();
		assertEquals(decrypt(dataChunk)[1], a);
		assertEquals(a, in.read());
	}
	
	@Test
	public void markAndResetSpanningSeveralBlocks() throws IOException {
		byte[] data = merge(merge(dataChunk, dataChunk), merge(dataChunk, dataChunk));
		setUp(data);
		
		int length = dataChunk.length+2;
		long len = in.skip(length);
		assertEquals(length, len);
		in.read();
		in.mark(20);
		byte[] a = new byte[20];
		len = in.read(a);
		assertEquals(a.length, len);
		in.reset();
		byte[] b = new byte[20];
		len = in.read(b);
		assertEquals(b.length, len);
		
		assertArrayEquals(a, b);
	}
	
	@Test
	public void skippedDataCanBeReadAfterReset() throws IOException {
		setUp(dataChunk);
		in.mark(8);
		long len = in.skip(8);
		assertEquals(8, len);
		in.reset();
		byte[] a = new byte[8];
		len = in.read(a);
		assertEquals(a.length, len);
		assertArrayEquals(decrypt(dataChunk), a);
	}
	
	@Test
	public void oneByteCorrectlyDecrypted() throws IOException {
		setUp(dataChunk);
		assertEquals(xtea.decrypt(dataChunk)[0], in.read());
	}
	
	@Test
	public void severalBytesOneAtATimeCorrectlyDecrypted() throws IOException {
		setUp(dataChunk);
		assertEquals(xtea.decrypt(dataChunk)[0], in.read());
		assertEquals(xtea.decrypt(dataChunk)[1], in.read());
		assertEquals(xtea.decrypt(dataChunk)[2], in.read());
		assertEquals(xtea.decrypt(dataChunk)[3], in.read());
		assertEquals(xtea.decrypt(dataChunk)[4], in.read());
		assertEquals(xtea.decrypt(dataChunk)[5], in.read());
	}
	
	@Test
	public void severalBytesChunkCorrectlyDecrypted() throws IOException {
		setUp(dataChunk);
		byte[] b = new byte[dataChunk.length];
		int l = in.read(b);
		
		assertEquals(dataChunk.length, l);
		assertArrayEquals(xtea.decrypt(dataChunk), b);
	}
	
	@Test
	public void attemptToReadMoreBytesThenAvailable() throws IOException {
		setUp(dataChunk);
		byte[] b = new byte[dataChunk.length*2];
		int l = in.read(b);
		
		assertEquals(dataChunk.length, l);
		assertArrayEquals(xtea.decrypt(dataChunk), sub(b, 0, dataChunk.length));
	}
	
	@Test
	public void readDataSpanningMultipleBlocks() throws IOException {
		byte[] data = merge(merge(dataChunk, dataChunk), merge(dataChunk, dataChunk));
		setUp(data);
		byte[] b = new byte[dataChunk.length*2];
		int l = in.read(b);
		
		assertEquals(b.length, l);
		assertArrayEquals(sub(decrypt(data), 0, b.length), b);
	}
	
	@Test
	public void availableCountsBufferedData() throws IOException {
		byte[] data = merge(merge(dataChunk, dataChunk), merge(dataChunk, dataChunk));
		setUp(data);
		
		in.read();
		
		assertEquals(data.length-1, in.available());
		
	}
	
	@Test
	public void skipSkipsOverCorrectData() throws IOException {
		setUp(dataChunk);
		long len = in.skip(3);
		assertEquals(3, len);
		assertEquals(decrypt(dataChunk)[3], in.read());
	}
	
	@Test
	public void skipSkipsOverMoreThenOneChunk() throws IOException {
		byte[] data = merge(merge(dataChunk, dataChunk), merge(dataChunk, dataChunk));
		setUp(data);
		in.read();
		int length = dataChunk.length*2+5;
		long len = in.skip(length);
		assertEquals(length, len);
		assertEquals(decrypt(dataChunk)[6], in.read());
	}
	
	@Test(expected = IOException.class)
	public void resetWithoutMarkThrowsException() throws IOException {
		in.reset();
	}
	
	
	
	
	private byte[] decrypt(byte[] b) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for(int i = 0; i < b.length; i+=XTEAEncryptionEngine.blockSize) {
			xtea.decrypt(out, sub(b, i, XTEAEncryptionEngine.blockSize));
		}
		return out.toByteArray();
	}
	
	private byte[] sub(byte[] b, int off, int len) {
		byte[] ret = new byte[len];
		for(int i = 0; i < len; i++) {
			ret[i] = b[off+i];
		}
		return ret;
	}

}
