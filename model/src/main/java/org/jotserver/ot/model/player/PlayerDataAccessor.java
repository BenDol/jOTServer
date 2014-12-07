package org.jotserver.ot.model.player;

import org.jotserver.ot.model.item.ItemTypeAccessor;
import org.jotserver.ot.model.map.TownAccessor;

public interface PlayerDataAccessor {
	public void loadPlayerData(Player player, ItemTypeAccessor items, TownAccessor towns) throws PlayerAccessException;
	public void savePlayerData(Player player) throws PlayerAccessException;
}
