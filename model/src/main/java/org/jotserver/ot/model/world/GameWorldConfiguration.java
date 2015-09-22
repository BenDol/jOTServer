package org.jotserver.ot.model.world;

import java.sql.Connection;

import org.jotserver.configuration.ConfigurationProvider;
import org.jotserver.ot.model.OutfitAccessor;
import org.jotserver.ot.model.item.ItemTypeAccessor;
import org.jotserver.ot.model.map.MapAccessor;
import org.jotserver.ot.model.player.PlayerDataAccessor;

public interface GameWorldConfiguration extends ConfigurationProvider {
    String getMapIdentifier();
    Connection getConnection();
    String getDirectory();
    PlayerDataAccessor getPlayerDataAccessor();
    MapAccessor getMapAccessor();
    ItemTypeAccessor getItemTypeAccessor();
    OutfitAccessor getOutfitAccessor();
}
