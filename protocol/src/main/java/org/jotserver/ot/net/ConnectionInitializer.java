package org.jotserver.ot.net;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jotserver.net.ClientSession;
import org.jotserver.net.ConnectionListener;

public class ConnectionInitializer implements ConnectionListener {
	
	private List<ProtocolProvider> providers;
	
	public ConnectionInitializer() {
		providers = new ArrayList<ProtocolProvider>();
	}
	
	public ConnectionInitializer(Collection<ProtocolProvider> providers) {
		this();
		this.providers.addAll(providers);
	}
	
	public void addProtocolProvider(ProtocolProvider provider) {
		providers.add(provider);
	}
	
	public Collection<ProtocolProvider> getProviders() {
		return new ArrayList<ProtocolProvider>(providers);
	}
	
	public void connectionClosed(ClientSession client) {
	}

	
	public void connectionCreated(ClientSession client) {
		client.addConnectionListener(new BaseProtocol(client, providers));
	}

	
	public void connectionIdle(ClientSession client) {
	}

	
	public void connectionOpened(ClientSession client) {
	}

	
	public void messageReceived(ClientSession client, InputStream in) {
	}

	
	public void messageSent(ClientSession client) {
	}

}
