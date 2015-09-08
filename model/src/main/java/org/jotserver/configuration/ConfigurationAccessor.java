package org.jotserver.configuration;

import org.jotserver.ot.model.MOTDAccessor;
import org.jotserver.ot.model.account.AccountAccessor;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.model.world.GameWorldAccessor;
import org.jotserver.ot.model.world.GameWorldConfigurationAccessor;

import java.sql.Connection;

public interface ConfigurationAccessor extends ConfigurationProvider {
	Connection getConnection();
	
	int getPort();
	
	AccountAccessor getAccountAccessor();

	GameWorldAccessor<GameWorld> getGameWorldAccessor();

	GameWorldConfigurationAccessor getGameWorldConfigurationAccessor();

	MOTDAccessor getMOTDAccessor();

	PlayerAccessor getPlayerAccessor();
}
