package org.jotserver.ot.net.game;

public interface GameProtocolAware {
	void setGameProtocol(GameProtocol protocol);
	boolean isOnline();
}
