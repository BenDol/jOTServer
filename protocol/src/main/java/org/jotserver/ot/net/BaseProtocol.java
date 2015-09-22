package org.jotserver.ot.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.jotserver.net.CData;
import org.jotserver.net.ClientSession;
import org.jotserver.net.ConnectionListener;
import org.jotserver.ot.net.io.MessageInputStream;

public class BaseProtocol implements ConnectionListener, Protocol {
    private static final Logger logger = Logger.getLogger(BaseProtocol.class);

    private ClientSession session;
    private Protocol currentProtocol;
    private List<ProtocolProvider> providers;

    public BaseProtocol(ClientSession clientSession) {
        session = clientSession;
        setCurrentProtocol(this);
        providers = new ArrayList<ProtocolProvider>();
    }

    public BaseProtocol(ClientSession clientSession, Collection<ProtocolProvider> providers) {
        this(clientSession);
        this.providers.addAll(providers);
    }


    public void connectionClosed(ClientSession client) {
    }


    public void connectionCreated(ClientSession client) {
    }


    public void connectionIdle(ClientSession client) {
    }


    public void connectionOpened(ClientSession client) {
    }


    public void messageReceived(ClientSession client, InputStream in) {
        try {
            InputStream message = parseHeader(in);
            while(message != null) {

                getCurrentProtocol().parsePacket(message);
                message.skip(message.available());

                message = parseHeader(in);
            }
        } catch (IOException e) {
            logger.error("Unhandled error when parsing packet: " + e.getMessage());
            try {
                client.close();
            } catch (IOException e1) {
                logger.error("Failed to close client connection upon error.");
            }
        }
    }


    public void messageSent(ClientSession client) {
    }

    public ClientSession getClientSession() {
        return session;
    }

    public Protocol getCurrentProtocol() {
        return currentProtocol;
    }

    protected Protocol getProtocolById(int protocolId) {
        Protocol ret = null;
        for(ProtocolProvider provider : providers) {
            ret = provider.getProtocol(protocolId);
            if(ret != null) {
                break;
            }
        }
        return ret;
    }

    public void addProtocolProvider(ProtocolProvider provider) {
        providers.add(provider);
    }

    public void setCurrentProtocol(Protocol protocol) {
        this.currentProtocol = protocol;
        if(protocol != null) {
            protocol.init(session);
        }
    }

    protected InputStream parseHeader(InputStream in) {
        try {
            InputStream ret = new MessageInputStream(in);
            return ret;
        } catch(IOException e) {
            return null;
        }
    }


    public void parseFirst(InputStream message) {
    }


    public void parsePacket(InputStream message) throws IOException {
        int protocolId = CData.readByte(message);
        Protocol protocol = getProtocolById(protocolId);
        if(protocol != null) {
            setCurrentProtocol(protocol);
            getCurrentProtocol().parseFirst(message);
        } else {
            logger.trace("Unknown protocol requested (ID: " + protocolId + ")");
            session.close();
        }
    }


    public void init(ClientSession session) {
    }

}
