package org.jotserver.ot.net.game.handler;

import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.GameProtocol;

public aspect PlayerGameProtocolAwareness {
		
	declare parents: Player implements GameProtocolAware;

	private GameProtocol Player.protocol = null;
	
	public void Player.setGameProtocol(GameProtocol protocol) {
		this.protocol = protocol;
	}
	
	public boolean Player.isOnline() {
		return protocol != null && protocol.isOnline();
	}
	
	public GameProtocol Player.getGameProtocol() {
		return protocol;
	}
	
}
