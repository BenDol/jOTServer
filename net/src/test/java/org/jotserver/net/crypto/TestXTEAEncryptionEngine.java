package org.jotserver.net.crypto;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.security.InvalidKeyException;

import org.junit.Before;
import org.junit.Test;

public class TestXTEAEncryptionEngine extends TestXTEA {
	
	private static final byte[] shortByteKey = new byte[] { 113, -32, 73 };
	private static final byte[] longByteKey = new byte[] { 113, -32, 73, 125, 113, 55, -20, -20, -56, -76, -86, -28, -33, 127, -31, -53, 24, -57, 68 };
	
	private static final long[] shortLongKey = new long[] { 1, 2, 3 };
	private static final long[] longLongKey = new long[] { 1, 2, 3, 4, 5 };
	
	private static final byte[] data1 = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Test(expected = InvalidKeyException.class)
	public void initializeFailsWithNullByteKey() throws InvalidKeyException {
		xtea.init((byte[])null);
	}
	
	@Test(expected = InvalidKeyException.class)
	public void initializeFailsWithShortByteKey() throws InvalidKeyException {
		xtea.init(shortByteKey);
	}
	
	@Test(expected = InvalidKeyException.class)
	public void initializeFailsWithLongByteKey() throws InvalidKeyException {
		xtea.init(longByteKey);
	}
	
	@Test(expected = InvalidKeyException.class)
	public void initializeFailsWithNullLongKey() throws InvalidKeyException {
		xtea.init((long[])null);
	}
	
	@Test(expected = InvalidKeyException.class)
	public void initializeFailsWithShortLongKey() throws InvalidKeyException {
		xtea.init(shortLongKey);
	}
	
	@Test(expected = InvalidKeyException.class)
	public void initializeFailsWithLongLongKey() throws InvalidKeyException {
		xtea.init(longLongKey);
	}
	
	
	@Test
	public void encryptionChangesMessage() throws IOException {
		byte[] enc = xtea.encrypt(data1);
		boolean same = true;
		for(int i = 0; i < enc.length; i++) {
			if(enc[i] != data1[i]) {
				same = false;
				break;
			}
		}
		assertFalse(same);
	}
	
	@Test
	public void decryptionReversesEncryption() throws IOException {
		assertArrayEquals(data1, xtea.decrypt(xtea.encrypt(data1)));
	}
	
	

}
