package org.jotserver.net;

import java.io.InputStream;

/**
 * Event listener interface for connections between a server and a client.
 * @author jiddo
 *
 */
public interface ConnectionListener {

    /**
     * Called when a new connection has been created.
     * @param client
     *             The client session for which this event has occurred on.
     */
    void connectionCreated(ClientSession client);

    /**
     * Called when a connection has been opened.
     * @param client
     *             The client session for which this event has occurred on.
     */
    void connectionOpened(ClientSession client);

    /**
     * Called when a connection has been closed.
     * @param client
     *             The client session for which this event has occurred on.
     */
    void connectionClosed(ClientSession client);

    /**
     * Called when a message has been received.
     * @param client
     *             The client session for which this event has occurred on.
     */
    void messageReceived(ClientSession client, InputStream in);

    /**
     * Called when a message has been sent.
     * @param client
     *             The client session for which this event has occurred on.
     */
    void messageSent(ClientSession client);

    /**
     * Called when a connection has become idle.
     * @param client
     *             The client session for which this event has occurred on.
     */
    void connectionIdle(ClientSession client);
}
