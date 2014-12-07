package org.jotserver.ot.model.player;

import java.util.List;

import org.jotserver.ot.model.world.GameWorld;

public interface PlayerVipAccessor {
	public List<Player> getPlayerVipList(Player player, GameWorld world) throws PlayerAccessException;
	public void savePlayerVipList(Player player) throws PlayerAccessException;
}
