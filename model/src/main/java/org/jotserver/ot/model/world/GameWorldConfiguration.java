package org.jotserver.ot.model.world;

import java.sql.Connection;

import org.jotserver.configuration.ConfigurationProvider;
import org.jotserver.ot.model.OutfitAccessor;
import org.jotserver.ot.model.item.ItemTypeAccessor;
import org.jotserver.ot.model.map.MapAccessor;
import org.jotserver.ot.model.player.PlayerDataAccessor;

public interface GameWorldConfiguration extends ConfigurationProvider {	
	public String getMapIdentifier();
	public Connection getConnection();
	public String getDirectory();
	public PlayerDataAccessor getPlayerDataAccessor();
	public MapAccessor getMapAccessor();
	public ItemTypeAccessor getItemTypeAccessor();
	public OutfitAccessor getOutfitAccessor();
}
