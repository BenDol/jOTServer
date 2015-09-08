package org.jotserver.net.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jotserver.net.BaseServer;
import org.jotserver.net.ClientSession;
import org.jotserver.net.ConnectionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBaseServer {
	
	private Mockery context;
	
	private BaseServer server;
	
	@Before
	public void setUp() throws Exception {
		context = new Mockery();
		server = new BaseServer(7171);
	}

	@After
	public void tearDown() throws Exception {
		if(server.isStarted()) {
			server.stop();
		}
	}

	@Test
	public void createIdleServerWithSetPort() {
		assertEquals(7171, server.getPort());
	}
	
	@Test
	public void startStopListeningToPort() throws IOException {
		server.start();
		assertTrue(server.isStarted());
		server.stop();
		assertFalse(server.isStarted());
	}
	
	@Test(expected = IllegalStateException.class)
	public void alreadyStartedShouldThrowException() throws IOException {
		server.start();
		server.start();
	}
	
	@Test(expected = IllegalStateException.class)
	public void alreadyStoppedShouldThrowException() throws IOException {
		server.start();
		server.stop();
		server.stop();
	}
	
	@Test
	public void connectToAndDisconnectFromStartedServer() throws IOException {
		server.start();
		Socket socket = new Socket("localhost", 7171);
		assertTrue(socket.isConnected());
		socket.close();
		assertTrue(socket.isClosed());
	}
	
	@Test(expected = ConnectException.class)
	public void connectToClosedServerShouldFail() throws IOException {
		server.start();
		server.stop();
		/*Socket socket = */new Socket("localhost", 7171);
	}
	
	@Test
	public void shouldHandleMultipleConnections() throws IOException {
		server.start();
		Socket socket1 = new Socket("localhost", 7171);
		Socket socket2 = new Socket("localhost", 7171);
		Socket socket3 = new Socket("localhost", 7171);
		
		assertTrue(socket1.isConnected());
		assertTrue(socket2.isConnected());
		assertTrue(socket3.isConnected());
		
		socket1.close();
		socket2.close();
		socket3.close();
		
		assertTrue(socket1.isClosed());
		assertTrue(socket2.isClosed());
		assertTrue(socket3.isClosed());
		
		server.stop();
	}
	
	/**
	 * TODO: For some reason this test won't work since the client-side sockets aren't notified when closed...
	 */
	/*@Test
	public void shouldDisconnectSocketsWhenStopped() throws IOException {
		server.start();
		Socket socket = new Socket("localhost", 7171);
		server.stop();
		assertTrue(socket.isClosed());
	}*/
	
	@Test
	public void canHaveConnectionListeners() {
		ConnectionListener listener = context.mock(ConnectionListener.class);
		server.addConnectionListener(listener);
		server.removeConnectionListener(listener);
		server.removeAllConnectionListeners();
	}
	
	@Test
	public void forwardsArtificialEvents() throws Exception {
		final ConnectionListener listener = context.mock(ConnectionListener.class);
		final IoSession session = context.mock(IoSession.class);
		final IoBuffer message = IoBuffer.allocate(0);
		
		server.addConnectionListener(listener);
		
		context.checking(new Expectations() {{
			oneOf(listener).connectionCreated(with(any(ClientSession.class)));
			oneOf(listener).connectionOpened(with(any(ClientSession.class)));
			oneOf(listener).connectionClosed(with(any(ClientSession.class)));
			oneOf(listener).connectionIdle(with(any(ClientSession.class)));
			oneOf(listener).messageSent(with(any(ClientSession.class)));
			oneOf(listener).messageReceived(with(any(ClientSession.class)), with(any(InputStream.class)));
			allowing(session).getAttribute("clientSession"); will(returnValue(new ClientSession(session)));
			allowing(session).getAttribute("inputStream"); will(returnValue(null));
			allowing(session);
		}});
		server.sessionCreated(session);
		server.sessionOpened(session);
		server.sessionClosed(session);
		server.sessionIdle(session, null);
		server.messageSent(session, message);
		server.messageReceived(session, message);
		
		context.assertIsSatisfied();
	}
	
	@Test
	public void providesInputStream() throws Exception {
		final ByteArrayOutputStream ss = new ByteArrayOutputStream();
		final IoSession session = new IoSessionStub();
		
		ConnectionListener listener = new ConnectionListener() {

			public void connectionClosed(ClientSession client) {}
			public void connectionCreated(ClientSession client) {}
			public void connectionIdle(ClientSession client) {}
			public void connectionOpened(ClientSession client) {}
			public void messageSent(ClientSession client) {}
			
			public void messageReceived(ClientSession client, InputStream in) {
				try {
					// The streams should merge together...
					if(in.available() != 7) {
						return;
					}
					while(in.available() > 0) {
						ss.write(in.read());
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
		server.addConnectionListener(listener);
		
		server.sessionCreated(session);
		server.sessionOpened(session);
		server.messageReceived(session, IoBuffer.wrap(new byte[] {1, 2, 3}));
		server.messageReceived(session, IoBuffer.wrap(new byte[] {4, 5, 6, 7}));
		
		assertArrayEquals(new byte[] {1, 2, 3, 4, 5, 6, 7}, ss.toByteArray());
	}
	
	@Test
	public void closeWithConnectedClient() throws IOException {
		server.start();
		Socket socket1 = new Socket("localhost", 7171);
		assertTrue(socket1.isConnected());
		server.stop();
		socket1.close();
		assertTrue(socket1.isClosed());
	}
	
}
