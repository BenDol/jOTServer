package org.jotserver.net.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;

import org.apache.mina.core.session.IoSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jotserver.net.ClientSession;
import org.jotserver.net.ConnectionListener;
import org.junit.Test;


public class TestClientSession {
	private Mockery context = new Mockery();
	
	@Test
	public void backedByIoSession() {
		/*ClientSession session = */new ClientSession(context.mock(IoSession.class));
	}
	
	@Test
	public void providesOutputStream() throws IOException {
		ClientSession session = new ClientSession(context.mock(IoSession.class));
		OutputStream stream = session.getOutputStream();
		assertNotNull(stream);
	}
	
	@Test
	public void outputStreamsForSameSessionAreIdentical() throws IOException {
		ClientSession session = new ClientSession(context.mock(IoSession.class));
		OutputStream stream1 = session.getOutputStream();
		OutputStream stream2 = session.getOutputStream();
		assertEquals(stream1, stream2);
	}
	
	@Test
	public void closeConnectionAndUnderlyingSession() throws IOException {
		final IoSession raw = context.mock(IoSession.class);
		ClientSession session = new ClientSession(raw);
		context.checking(new Expectations() {{
			atLeast(1).of(raw).close(false);
		}});
		session.close();
		context.assertIsSatisfied();
	}
	
	@Test(expected = IOException.class)
	public void cannotCloseMultipleTimes() throws IOException {
		final IoSession raw = context.mock(IoSession.class);
		ClientSession session = new ClientSession(raw);
		context.checking(new Expectations() {{
			atLeast(1).of(raw).close(false);
		}});
		session.close();
		session.close();
		context.assertIsSatisfied();
	}
	
	@Test
	public void sessionHasClosedState() throws IOException {
		final IoSession raw = context.mock(IoSession.class);
		ClientSession session = new ClientSession(raw);
		context.checking(new Expectations() {{
			atLeast(1).of(raw).close(false);
		}});
		
		session.close();
		assertTrue(session.isClosed());
	}
	
	@Test(expected = ClosedChannelException.class)
	public void closedSessionCannotAquireOutputStreams() throws IOException {
		final IoSession raw = context.mock(IoSession.class);
		ClientSession session = new ClientSession(raw);
		context.checking(new Expectations() {{
			atLeast(1).of(raw).close(false);
		}});
		session.close();
		session.getOutputStream();
	}
	
	@Test
	public void supportsConnectionListeners() {
		final IoSession raw = context.mock(IoSession.class);
		ClientSession session = new ClientSession(raw);
		ConnectionListener listener = context.mock(ConnectionListener.class);
		session.addConnectionListener(listener);
		session.removeConnectionListener(listener);
		session.removeAllConnectionListeners();
	}

	@Test
	public void forwardsConnectionEvents() {
		final IoSession raw = context.mock(IoSession.class);
		final ConnectionListener listener = context.mock(ConnectionListener.class);
		final ClientSession session = new ClientSession(raw);
		
		session.addConnectionListener(listener);
		
		context.checking(new Expectations() {{
			oneOf(listener).connectionCreated(session);
			oneOf(listener).connectionOpened(session);
			oneOf(listener).connectionClosed(session);
			oneOf(listener).connectionIdle(session);
			oneOf(listener).messageReceived(session, null);
			oneOf(listener).messageSent(session);
		}});
		session.sessionCreated();
		session.sessionOpened();
		session.sessionClosed();
		session.sessionIdle();
		session.messageReceived(null);
		session.messageSent();
		context.assertIsSatisfied();
	}
	
	@Test
	public void providesClientAddress() {
		final IoSession raw = context.mock(IoSession.class);
		final InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 1);
		
		context.checking(new Expectations() {{
			oneOf(raw).getRemoteAddress(); will(returnValue(inetSocketAddress));
		}});
		
		ClientSession session = new ClientSession(raw);
		InetAddress addr = session.getClientAddress();
		
		assertEquals(inetSocketAddress.getAddress(), addr);
	}
	
	
}