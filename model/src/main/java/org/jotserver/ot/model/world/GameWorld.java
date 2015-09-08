package org.jotserver.ot.model.world;

import java.net.InetSocketAddress;

public interface GameWorld {
	String getIdentifier();
	String getName();
	InetSocketAddress getAddress();
	boolean isLocal();
}
