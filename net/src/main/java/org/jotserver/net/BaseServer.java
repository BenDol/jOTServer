package org.jotserver.net;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * An instance of the BaseServer class is the main entry point for any server based on this 
 * net module. Once started it will automatically listen for and accept incoming client connections
 * and relay them to the registered ConnectionListeners. Incoming connections will be wrapped in 
 * ClientSession objects. 
 * 
 * The BaseServer uses a specific amount of threads for raw IO processing as well as IO event processing. 
 * These are set to 2 and 16 threads, respectively. 
 * 
 * @author jiddo
 *
 */
public class BaseServer implements IoHandler {
    private static final Logger logger = Logger.getLogger(BaseServer.class);

    private static final Object ATTRIBUTEKEY_CLIENTSESSION = "clientSession";
    private static final Object ATTRIBUTEKEY_INPUTSTREAM = "inputStream";

    private static final int EVENTTHREADS = 16;
    private static final int IOTHREADS = 2;

    private int port;
    private boolean started;

    private NioSocketAcceptor acceptor;

    private List<ConnectionListener> listeners;
    private List<ClientSession> connections;

    private OrderedThreadPoolExecutor eventExecutor;

    private BaseServer() {
        acceptor = null;
        eventExecutor = null;
        this.port = 0;
        started = false;
        listeners = new ArrayList<ConnectionListener>();
        connections = new ArrayList<ClientSession>();
    }

    /**
     * Constructs a new BaseServer which, after being started, will listen to the specified TCP
     * port.
     * @param port
     *             The TCP port to listen on for incoming connections.
     */
    public BaseServer(int port) {
        this();
        this.port = port;
    }

    /**
     * Returns the port that this server is specified to listen on.
     * @return
     *             The port this server listens on
     */
    public int getPort() {
        return port;
    }

    /**
     * Initializes this server and starts listening on the previously specified port.
     * @throws IOException
     *             If the server could not bind to the specified port for some reason.
     * @throws IllegalStateException
     *             If the server is already started.
     */
    public void start() throws IOException {
        if(started) throw new IllegalStateException("Server already started.");

        connections.clear();

        acceptor = new NioSocketAcceptor(IOTHREADS);
        eventExecutor = new OrderedThreadPoolExecutor(EVENTTHREADS);
        
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

        chain.addFirst( "threadPool", new ExecutorFilter(eventExecutor) );
        acceptor.setReuseAddress( true );
        acceptor.getSessionConfig().setReuseAddress(true);
        acceptor.setHandler( this );
        acceptor.bind( new InetSocketAddress( port ) );
        
        started = true;
    }

    /**
     * Checks if this server has been started or not.
     * @return
     *             True if the server has been started, false otherwise.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Stops this server from accepting new connections, and closes all previously
     * established connections.
     * @throws IllegalStateException
     *             If the server has not been started.
     */
    public void stop() {
        if(!started) throw new IllegalStateException("Server not running.");
        acceptor.unbind();
        for(ClientSession session : new ArrayList<ClientSession>(connections)) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        connections.clear();
        acceptor.dispose();
        eventExecutor.shutdown();
        started = false;
    }

    public void sessionCreated(IoSession session) {
        ClientSession clientSession = getClientSessionInstance(session);
        connections.add(clientSession);
        for(ConnectionListener listener : listeners) {
            listener.connectionCreated(clientSession);
        }
        clientSession.sessionCreated();
    }

    public void exceptionCaught(IoSession session, Throwable exception)
            throws Exception {
        logger.error("Unhandled error in connection to client at address " + session.getRemoteAddress().toString() + ".", exception);
        ClientSession clientSession = getClientSessionInstance(session);
        if(clientSession != null) {
            clientSession.close();
        }
    }

    public void messageReceived(IoSession session, Object message) throws Exception {
        IoBuffer buffer = (IoBuffer)message;
        ClientSession clientSession = getClientSessionInstance(session);
        BaseServerInputStream in = getSessionInputStreamInstance(session);
        in.append(buffer);
        for(ConnectionListener listener : listeners) {
            listener.messageReceived(clientSession, in);
        }
        clientSession.messageReceived(in);
    }

    public void messageSent(IoSession session, Object message) throws Exception {
        ClientSession clientSession = getClientSessionInstance(session);
        for(ConnectionListener listener : listeners) {
            listener.messageSent(clientSession);
        }
        clientSession.messageSent();
    }

    public void sessionClosed(IoSession session) throws Exception {
        ClientSession clientSession = getClientSessionInstance(session);
        connections.remove(clientSession);
        for(ConnectionListener listener : listeners) {
            listener.connectionClosed(clientSession);
        }
        clientSession.sessionClosed();
        connections.remove(clientSession);
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        ClientSession clientSession = getClientSessionInstance(session);
        for(ConnectionListener listener : listeners) {
            listener.connectionIdle(clientSession);
        }
        clientSession.sessionIdle();
    }

    public void sessionOpened(IoSession session) throws Exception {
        ClientSession clientSession = getClientSessionInstance(session);
        for(ConnectionListener listener : listeners) {
            listener.connectionOpened(clientSession);
        }
        clientSession.sessionOpened();
    }

    /**
     * Retrieves the ClientSession object that corresponds to the given IoSession.
     * If the IoSession has not previously been registered with this server, it will
     * be assigned a newly created ClientSession.
     * @param session
     *                 The IoSession for which the ClientSession should be provided.
     * @return
     *                 The ClientSession associated with the given IoSession.
     */
    private ClientSession getClientSessionInstance(IoSession session) {
        ClientSession ret = (ClientSession)session.getAttribute(ATTRIBUTEKEY_CLIENTSESSION);
        if(ret == null) {
            ret = new ClientSession(session);
            session.setAttribute(ATTRIBUTEKEY_CLIENTSESSION, ret);
        }
        return ret;
    }

    /**
     * Retrieves the BaseServerInputStream object that corresponds to the given IoSession.
     * If the IoSession has not previously been registered with this server, it will
     * be assigned a newly created BaseServerInputStream.
     * @param session
     *                 The IoSession for which the BaseServerInputStream should be provided.
     * @return
     *                 The BaseServerInputStream associated with the given IoSession.
     */
    private BaseServerInputStream getSessionInputStreamInstance(IoSession session) {
        BaseServerInputStream ret = (BaseServerInputStream)session.getAttribute(ATTRIBUTEKEY_INPUTSTREAM);
        if(ret == null) {
            ret = new BaseServerInputStream();
            session.setAttribute(ATTRIBUTEKEY_INPUTSTREAM, ret);
        }
        return ret;
    }

    /**
     * Registers a new ConnectionListener to this server. After a call to this method the specified
     * listener will be notified whenever a networking event on this server occurs.
     * @param listener
     *                 The listener to add to this server.
     */
    public void addConnectionListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a previously registered connection listener from this server. Once removed the
     * specified listener will no longer be notified of events on this server.
     * @param listener
     *                 The listener to remove from this server.
     */
    public void removeConnectionListener(ConnectionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Removes all the registered connection listeners from this server as if by calling
     * removeConnectionListener(ConnectionListener) once for each previously registered
     * listener.
     */
    public void removeAllConnectionListeners() {
        listeners.clear();
    }
}
