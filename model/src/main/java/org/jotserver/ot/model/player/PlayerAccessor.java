package org.jotserver.ot.model.player;

import org.jotserver.ot.model.account.Account;
import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.model.world.GameWorldAccessor;

public interface PlayerAccessor {
	public PlayerList getPlayerList(Account account,
			GameWorldAccessor<? extends GameWorld> gameWorldAccessor) throws PlayerAccessException;
	
	public Player getPlayer(String name, GameWorldAccessor<? extends GameWorld> gameWorldAccessor)
			throws PlayerAccessException;
	
	public PlayerVipAccessor getPlayerVipAccessor() throws PlayerAccessException;
	
	public void createPlayer(Account account, Player player) throws PlayerAccessException;
}
