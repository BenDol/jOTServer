package org.jotserver.configuration;

import java.sql.Connection;

import org.jotserver.configuration.ConfigurationAccessor;
import org.jotserver.ot.model.MOTDAccessor;
import org.jotserver.ot.model.account.AccountAccessor;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.model.world.GameWorldAccessor;
import org.jotserver.ot.model.world.GameWorldConfigurationAccessor;
import org.junit.Ignore;

@Ignore
public class TestableConfigurationAccessor implements ConfigurationAccessor {

	public AccountAccessor getAccountAccessor() {
		return null;
	}

	public Connection getConnection() {
		return null;
	}

	public GameWorldAccessor<GameWorld> getGameWorldAccessor() {
		return null;
	}

	public GameWorldConfigurationAccessor getGameWorldConfigurationAccessor() {
		return null;
	}

	public MOTDAccessor getMOTDAccessor() {
		return null;
	}

	public PlayerAccessor getPlayerAccessor() {
		return null;
	}

	public int getPort() {
		return 0;
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
