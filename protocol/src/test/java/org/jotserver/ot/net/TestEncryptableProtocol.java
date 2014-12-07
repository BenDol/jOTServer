package org.jotserver.ot.net;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

import javax.crypto.CipherInputStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.net.ClientSession;
import org.jotserver.net.encryption.XTEAInputStream;
import org.jotserver.net.encryption.XTEAOutputStream;
import org.jotserver.ot.net.io.MessageOutputStream;
import org.junit.Before;
import org.junit.Test;

public class TestEncryptableProtocol {

	private TestableProtocol prot;
	private Mockery context;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		prot = new TestableProtocol();
	}
	
	@Test
	public void defaultClientIsNull() {
		assertNull(prot.client);
	}
	
	@Test
	public void defaultEncryptionEngineIsNull() {
		assertNull(prot.encryptionEngine);
	}
	
	@Test
	public void initAssignsClientSession() {
		ClientSession client = context.mock(ClientSession.class);
		prot.init(client);
		assertSame(client, prot.client);
	}
	
	@Test(expected = IllegalStateException.class)
	public void cannotGetXTEAInputStreamWhenNotInitialized() {
		prot.getDecryptedInputStream(new ByteArrayInputStream(new byte[] {}));
	}
	
	@Test(expected = IllegalStateException.class)
	public void cannotGetXTEAOutputStreamWhenNotInitialized() {
		prot.encryptStreamXTEA(new ByteArrayOutputStream());
	}
	
	@Test
	public void initAndAquireXTEA() throws InvalidKeyException {
		long[] keys = new long[] {1, 2, 3, 4};
		prot.initXTEAEngine(keys);
		assertArrayEquals(keys, prot.encryptionEngine.key);
		InputStream in = prot.getDecryptedInputStream(new ByteArrayInputStream(new byte[] {}));
		assertTrue(in instanceof XTEAInputStream);
		
		OutputStream out = prot.encryptStreamXTEA(new ByteArrayOutputStream());
		assertTrue(out instanceof XTEAOutputStream);
	}
	
	@Test(expected = GeneralSecurityException.class)
	public void decryptInvalidStreamRSA() throws IOException, GeneralSecurityException {
		InputStream in = prot.decryptStreamRSA(new ByteArrayInputStream(new byte[] {1, 1, 1, 1}));
	}
	
	@Test
	public void decryptValidStreamRSA() throws IOException, GeneralSecurityException {
		InputStream in = prot.decryptStreamRSA(new ByteArrayInputStream(new byte[] {}));
		assertTrue(in instanceof CipherInputStream);
	}
	
	@Test
	public void getClientXTEAEncryptedMessageOutputStream() throws IOException, InvalidKeyException {
		prot.initXTEAEngine(new long[] {1, 2, 3, 4});
		
		final ClientSession client = context.mock(ClientSession.class);
		prot.init(client);
		
		final OutputStream sout = new ByteArrayOutputStream();
		context.checking(new Expectations() {{
			oneOf(client).getOutputStream(); will(returnValue(sout));
		}});
		
		OutputStream out = prot.getEncryptedMessageOutputStream();
		
		assertTrue(out instanceof MessageOutputStream);
		context.assertIsSatisfied();
	}
	
}
