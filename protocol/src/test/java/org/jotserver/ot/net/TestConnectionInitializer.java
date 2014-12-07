package org.jotserver.ot.net;


import static org.junit.Assert.*;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.net.ClientSession;
import org.junit.Before;
import org.junit.Test;

public class TestConnectionInitializer {

	private ConnectionInitializer init;
	private Mockery context;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		init = new ConnectionInitializer();
	}
	
	@Test
	public void addsConnectionListener() {
		final ClientSession session = context.mock(ClientSession.class);
		context.checking(new Expectations() {{
			oneOf(session).addConnectionListener(with(any(BaseProtocol.class)));
		}});
		init.connectionCreated(session);
		context.assertIsSatisfied();
	}
	
	@Test
	public void addProtocolProvider() {
		ProtocolProvider provider = context.mock(ProtocolProvider.class);
		assertFalse(init.getProviders().contains(provider));
		init.addProtocolProvider(provider);
		assertTrue(init.getProviders().contains(provider));
		init = new ConnectionInitializer(Arrays.asList(provider));
		assertTrue(init.getProviders().contains(provider));
	}
	
}
