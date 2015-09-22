package org.jotserver.ot.model.player;

import org.jotserver.ot.model.item.ItemTypeAccessor;
import org.jotserver.ot.model.map.TownAccessor;

public interface PlayerDataAccessor {
    void loadPlayerData(Player player, ItemTypeAccessor items, TownAccessor towns) throws PlayerAccessException;
    void savePlayerData(Player player) throws PlayerAccessException;
}
