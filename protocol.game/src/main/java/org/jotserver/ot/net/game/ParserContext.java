package org.jotserver.ot.net.game;

import org.jotserver.ot.model.account.AccountAccessor;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.world.GameWorldAccessor;
import org.jotserver.ot.model.world.LocalGameWorld;

public interface ParserContext {
    Player getPlayer();
    LocalGameWorld getWorld();
    GameWorldAccessor<LocalGameWorld> getWorlds();
    PlayerAccessor getPlayers();
    AccountAccessor getAccounts();
}
