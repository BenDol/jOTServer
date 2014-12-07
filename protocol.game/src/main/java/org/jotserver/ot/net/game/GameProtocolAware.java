package org.jotserver.ot.net.game;

public interface GameProtocolAware {
	public void setGameProtocol(GameProtocol protocol);
	public boolean isOnline();
}
