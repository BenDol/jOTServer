package org.jotserver.ot.model.player;

import java.util.List;

import org.jotserver.ot.model.world.GameWorld;

public interface PlayerVipAccessor {
    List<Player> getPlayerVipList(Player player, GameWorld world) throws PlayerAccessException;
    void savePlayerVipList(Player player) throws PlayerAccessException;
}
