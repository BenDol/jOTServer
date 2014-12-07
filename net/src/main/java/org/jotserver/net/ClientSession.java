package org.jotserver.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;

/**
 * A ClientSession is the abstract representation of a connection between the server 
 * and a client. Each connected client should be associated with an instance of this 
 * class.
 * @author jiddo
 *
 */
public class ClientSession {
	
	private List<ConnectionListener> listeners;
	private IoSession session;
	private OutputStream out;
	private boolean closed;
	
	/**
	 * Constructs a new client session connected to the specified IoSession instance. 
	 * @param session
	 * 			The IoSession associated with this client session.
	 */
	public ClientSession(IoSession session) {
		this.session = session;
		out = new BaseServerOutputStream(session);
		listeners = new ArrayList<ConnectionListener>();
		closed = false;
	}
	
	/**
	 * Provides an output stream which can be used to write data to the corresponding 
	 * client.
	 * @return
	 * 			An output stream which is connected to the underlying client.
	 * @throws ClosedChannelException
	 * 			If this session has been previously closed.
	 */
	public OutputStream getOutputStream() throws IOException {
		if(isClosed()) throw new ClosedChannelException();
		return out;
	}
	
	/**
	 * Closes the connection to the underlying client as well as the output stream
	 * associated with it. 
	 * @throws IOException
	 * 			If this session has already been closed, or if it could not be closed.
	 */
	public void close() throws IOException {
		if(isClosed()) throw new IOException("Connection already closed!");
		sessionClosed();
		out.close();
		try {
			session.close(false).await();
		} catch (InterruptedException e) {}
		closed = true;
	}
	
	/**
	 * Registers a new ConnectionListener to this session. After a call to this method the specified 
	 * listener will be notified whenever a networking event on this session occurs. 
	 * @param listener
	 * 				The listener to add to this session.
	 */
	public void addConnectionListener(ConnectionListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a previously registered connection listener from this session. Once removed the 
	 * specified listener will no longer be notified of events on this session.
	 * @param listener
	 * 				The listener to remove from this session.
	 */
	public void removeConnectionListener(ConnectionListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Removes all the registered connection listeners from this session as if by calling 
	 * removeConnectionListener(ConnectionListener) once for each previously registered 
	 * listener. 
	 */
	public void removeAllConnectionListeners() {
		listeners.clear();
	}
	
	/**
	 * Notifies all the registered connection listeners that this session has been closed.
	 */
	public void sessionClosed() {
		for(ConnectionListener listener : listeners) {
			listener.connectionClosed(this);
		}
	}
	
	/**
	 * Notifies all the registered connection listeners that this session has been created.
	 */
	public void sessionCreated() {
		for(ConnectionListener listener : listeners) {
			listener.connectionCreated(this);
		}
	}
	
	/**
	 * Notifies all the registered connection listeners that this session is idle.
	 */
	public void sessionIdle() {
		for(ConnectionListener listener : listeners) {
			listener.connectionIdle(this);
		}
	}
	
	/**
	 * Notifies all the registered connection listeners that this session has been opened.
	 */
	public void sessionOpened() {
		for(ConnectionListener listener : listeners) {
			listener.connectionOpened(this);
		}
	}
	
	/**
	 * Notifies all the registered connection listeners that a message has been received on 
	 * this session.
	 */
	public void messageReceived(InputStream in) {
		for(ConnectionListener listener : listeners) {
			listener.messageReceived(this, in);
		}
	}
	
	/**
	 * Notifies all the registered connection listeners that a message has been sent on 
	 * this session.
	 */
	public void messageSent() {
		for(ConnectionListener listener : listeners) {
			listener.messageSent(this);
		}
	}
	
	/**
	 * Checks if this client session has been closed.
	 * @return
	 * 			True if this client session has been closed, false otherwise.
	 */
	public boolean isClosed() {
		return closed;
	}
	
	/**
	 * Provides the Internet address to the underlying client. 
	 * @return
	 * 			The Internet address to the client.
	 */
	public InetAddress getClientAddress() {
		InetSocketAddress addr = (InetSocketAddress)session.getRemoteAddress();
		return addr.getAddress();
	}
}
