package org.jotserver.ot.model.world;

import java.sql.Connection;

import org.jotserver.ot.model.OutfitAccessor;
import org.jotserver.ot.model.item.ItemTypeAccessor;
import org.jotserver.ot.model.map.MapAccessor;
import org.jotserver.ot.model.player.PlayerDataAccessor;
import org.junit.Ignore;

@Ignore
public class TestableGameWorldConfiguration implements GameWorldConfiguration {

    public Connection getConnection() {
        return null;
    }

    public String getDirectory() {
        return null;
    }

    public ItemTypeAccessor getItemTypeAccessor() {
        return null;
    }

    public MapAccessor getMapAccessor() {
        return null;
    }

    public String getMapIdentifier() {
        return null;
    }

    public OutfitAccessor getOutfitAccessor() {
        return null;
    }

    public PlayerDataAccessor getPlayerDataAccessor() {
        return null;
    }

    public boolean getBoolean(String key) {
        return false;
    }

    public int getInt(String key) {
        return 0;
    }

    public String getString(String key) {
        return null;
    }

}
