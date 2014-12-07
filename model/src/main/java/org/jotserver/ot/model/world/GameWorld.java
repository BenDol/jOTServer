package org.jotserver.ot.model.world;

import java.net.InetSocketAddress;

public interface GameWorld {
	public String getIdentifier();
	public String getName();
	public InetSocketAddress getAddress();
	public boolean isLocal();
}
