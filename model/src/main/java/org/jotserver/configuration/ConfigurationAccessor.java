package org.jotserver.configuration;

import org.jotserver.ot.model.MOTDAccessor;
import org.jotserver.ot.model.account.AccountAccessor;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.model.world.GameWorldAccessor;
import org.jotserver.ot.model.world.GameWorldConfigurationAccessor;

import java.sql.Connection;

public interface ConfigurationAccessor extends ConfigurationProvider {
	public Connection getConnection();
	
	public int getPort();
	
	public AccountAccessor getAccountAccessor();

	public GameWorldAccessor<GameWorld> getGameWorldAccessor();

	public GameWorldConfigurationAccessor getGameWorldConfigurationAccessor();

	public MOTDAccessor getMOTDAccessor();

	public PlayerAccessor getPlayerAccessor();
}
