package org.jotserver.ot.model.player;

import org.jotserver.ot.model.account.Account;
import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.model.world.GameWorldAccessor;

public interface PlayerAccessor {
	PlayerList getPlayerList(Account account,
			GameWorldAccessor<? extends GameWorld> gameWorldAccessor) throws PlayerAccessException;
	
	Player getPlayer(String name, GameWorldAccessor<? extends GameWorld> gameWorldAccessor)
			throws PlayerAccessException;
	
	PlayerVipAccessor getPlayerVipAccessor() throws PlayerAccessException;
	
	void createPlayer(Account account, Player player) throws PlayerAccessException;
}
