package org.jotserver.net.test;


import static org.junit.Assert.*;

import java.io.EOFException;
import java.io.IOException;

import org.apache.mina.core.buffer.IoBuffer;
import org.jotserver.net.BaseServerInputStream;
import org.junit.Before;
import org.junit.Test;

public class TestBaseServerInputStream {
	
	BaseServerInputStream is;
	private static final byte[] buf1 = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	private static final byte[] buf2 = new byte[] {11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
	private static final byte[] buf3 = new byte[] {21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
	
	@Before
	public void setUp() throws Exception {
		is = new BaseServerInputStream();
	}
	
	@Test
	public void emptyWhenCreated() throws IOException {
		assertEquals(0, is.available());
	}
	
	@Test
	public void canAddOneBuffer() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		assertEquals(buf1.length, is.available());
	}
	
	@Test
	public void readAppendedDataOneByte() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		assertEquals(buf1[0], is.read());
	}
	
	@Test
	public void readAppendedDataSeveralBytesInOrderOneAtATime() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		assertEquals(buf1[0], is.read());
		assertEquals(buf1[1], is.read());
		assertEquals(buf1[2], is.read());
	}
	
	@Test(expected = EOFException.class)
	public void readingFromEmptyStreamShouldThrowException() throws IOException {
		is.read();
	}
	
	@Test
	public void availableBytesChangedWhenBytesAreRead() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		is.read();
		assertEquals(buf1.length-1, is.available());
		is.read();
		assertEquals(buf1.length-2, is.available());
		is.read();
		assertEquals(buf1.length-3, is.available());
	}
	
	@Test
	public void closeClearesAllBufferedData() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		is.close();
		assertEquals(0, is.available());
	}
	
	@Test
	public void skippingBytesIsSupported() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		long len = is.skip(3);
		assertEquals(3, len);
		assertEquals(buf1.length-3, is.available());
		assertEquals(buf1[3], is.read());
	}
	
	@Test
	public void readByteArrayEntireStream() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		byte[] tmp = new byte[is.available()];
		int l = is.read(tmp);
		assertEquals(buf1.length, l);
		assertArrayEquals(buf1, tmp);
		assertEquals(0, is.available());
	}
	
	@Test
	public void readByteArrayPartOfStream() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		byte[] tmp = new byte[3];
		int l = is.read(tmp);
		assertEquals(3, l);
		assertArrayEquals(new byte[] {1, 2, 3}, tmp);
		assertEquals(buf1.length-l, is.available());
	}
	
	
	@Test
	public void canAddMultipleBuffers() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		is.append(IoBuffer.wrap(buf2));
		is.append(IoBuffer.wrap(buf3));
		assertEquals(buf1.length + buf2.length + buf3.length, is.available());
	}
	
	@Test
	public void readAppendedDataExceedingFirstBufferOneByteAtATime() throws IOException {
		is.append(IoBuffer.wrap(new byte[] {100}));
		is.append(IoBuffer.wrap(buf1));
		assertEquals(100, is.read());
		assertEquals(buf1.length, is.available());
		assertEquals(buf1[0], is.read());
		assertEquals(buf1.length-1, is.available());
		assertEquals(buf1[1], is.read());
		assertEquals(buf1.length-2, is.available());
	}
	
	@Test
	public void skippingBytesExceedingFirstBuffer() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		is.append(IoBuffer.wrap(buf2));
		is.append(IoBuffer.wrap(buf3));
		int skip = buf1.length+buf2.length+1;
		long len = is.skip(skip);
		assertEquals(skip, len);
		assertEquals(buf3.length-1, is.available());
		assertEquals(buf3[1], is.read());
	}
	
	@Test
	public void readByteArrayEntireStreamMultipleBuffers() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		is.append(IoBuffer.wrap(buf2));
		is.append(IoBuffer.wrap(buf3));
		byte[] res = new byte[buf1.length + buf2.length + buf3.length];
		System.arraycopy(buf1, 0, res, 0, buf1.length);
		System.arraycopy(buf2, 0, res, buf1.length, buf2.length);
		System.arraycopy(buf3, 0, res, buf1.length+buf2.length, buf3.length);
		
		byte[] tmp = new byte[is.available()];
		int l = is.read(tmp);
		assertEquals(res.length, l);
		assertArrayEquals(res, tmp);
		assertEquals(0, is.available());
	}
	
	@Test
	public void readByteArrayPartOfStreamMultipleBuffers() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		is.append(IoBuffer.wrap(buf2));
		is.append(IoBuffer.wrap(buf3));
		byte[] res = new byte[buf1.length + buf2.length + 3];
		System.arraycopy(buf1, 0, res, 0, buf1.length);
		System.arraycopy(buf2, 0, res, buf1.length, buf2.length);
		System.arraycopy(buf3, 0, res, buf1.length+buf2.length, 3);
		
		byte[] tmp = new byte[buf1.length + buf2.length + 3];
		int l = is.read(tmp);
		assertEquals(tmp.length, l);
		assertArrayEquals(res, tmp);
		assertEquals(buf1.length + buf2.length + buf3.length-l, is.available());
	}
	
	@Test
	public void markAndResetWithinFirstBuffer() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		is.append(IoBuffer.wrap(buf2));
		is.append(IoBuffer.wrap(buf3));
		long len = is.skip(2);
		assertEquals(2, len);
		assertTrue(is.markSupported());
		is.mark(0);
		int tmp = is.read();
		is.reset();
		assertEquals(tmp, is.read());
	}
	
	@Test
	public void markAndResetWithinSubsequentBuffer() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		is.append(IoBuffer.wrap(buf2));
		is.append(IoBuffer.wrap(buf3));
		int length = buf1.length + 2;
		long len = is.skip(length);
		assertEquals(length, len);
		is.mark(0);
		length = buf2.length-2+buf3.length;
		byte[] b1 = new byte[length];
		len = is.read(b1, 0, length);
		assertEquals(length, len);
		is.reset();
		length = buf2.length-2+buf3.length;
		byte[] b2 = new byte[length];
		len = is.read(b2, 0, length);
		assertEquals(length, len);
		assertArrayEquals(b1, b2);
	}
	
	@Test
	public void markAndResetWhenEmpty() throws IOException {
		is.mark(0);
		is.reset();
	}
	
	@Test(expected = IOException.class)
	public void resetWithoutMark() throws IOException {
		is.reset();
	}
	
	@Test
	public void negativeSkipSize() throws IOException {
		assertEquals(0, is.skip(-1));
	}
	
	@Test
	public void multipleSuccessiveMarks() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		assertEquals(buf1[0], is.read());
		is.mark(10);
		assertEquals(buf1[1], is.read());
		assertEquals(buf1[2], is.read());
		is.mark(10);
		assertEquals(buf1[3], is.read());
		assertEquals(buf1[4], is.read());
		is.reset();
		assertEquals(buf1[3], is.read());
		assertEquals(buf1[4], is.read());
	}
	
	@Test
	public void multipleSuccessiveMarksWithBigChunks() throws IOException {
		is.append(IoBuffer.wrap(buf1));
		is.append(IoBuffer.wrap(buf2));
		is.append(IoBuffer.wrap(buf3));
		
		byte[] actual = new byte[buf1.length];
		assertEquals(actual.length, is.read(actual));
		assertArrayEquals(buf1, actual);
		is.mark(buf2.length+buf3.length);
		
		actual = new byte[buf2.length];
		assertEquals(actual.length, is.read(actual));
		assertArrayEquals(buf2, actual);
		
		is.mark(buf3.length);
		
		actual = new byte[buf3.length];
		assertEquals(actual.length, is.read(actual));
		assertArrayEquals(buf3, actual);
		
		is.reset();
		
		actual = new byte[buf3.length];
		assertEquals(actual.length, is.read(actual));
		assertArrayEquals(buf3, actual);
	}

}
