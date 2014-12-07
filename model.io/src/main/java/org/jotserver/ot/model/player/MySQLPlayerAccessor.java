package org.jotserver.ot.model.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.jotserver.configuration.ConnectionProvider;
import org.jotserver.ot.model.account.Account;
import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.model.world.GameWorldAccessor;

public class MySQLPlayerAccessor implements PlayerAccessor, ConnectionProvider {
	private static final Logger logger = Logger.getLogger(MySQLPlayerAccessor.class);
	
	private ConnectionProvider provider;

	private PlayerVipAccessor vipAccessor;
	
	public MySQLPlayerAccessor(ConnectionProvider provider) {
		this.provider = provider;
		vipAccessor = null;
	}

	public Player getPlayer(String name, GameWorldAccessor<? extends GameWorld> gameWorldAccessor) throws PlayerAccessException {
		try {
			return loadPlayer(name, gameWorldAccessor);
		} catch (SQLException e) {
			logger.error("Failed to load player.", e);
			throw new PlayerAccessException("Failed to load player.", e);
		}
	}

	public PlayerList getPlayerList(Account account, GameWorldAccessor<? extends GameWorld> gameWorldAccessor) throws PlayerAccessException {
		try {
			return loadPlayerList(account.getNumber(), gameWorldAccessor);
		} catch (SQLException e) {
			logger.error("Failed to load player list", e);
			throw new PlayerAccessException("Failed to load character list.\nPlease contact an administrator.");
		}
	}
	
	private Player loadPlayer(String name, GameWorldAccessor<? extends GameWorld> gameWorldAccessor) throws SQLException {
		Connection connection = provider.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM `players` WHERE `players`.`name` = ?");
			stmt.setString(1, name);
			ResultSet result = stmt.executeQuery();
			try {
				if(result.next()) {
					return parsePlayer(result, gameWorldAccessor);
				} else {
					return null;
				}
			} finally {
				result.close();
			}
		} finally {
			if(stmt != null) {
				stmt.close();
			}
			connection.close();
		}
	}
	
	private PlayerList loadPlayerList(long accountNumber, GameWorldAccessor<? extends GameWorld> gameWorldAccessor) throws SQLException {
		Connection connection = provider.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM `players` WHERE `players`.`account_number` = ?");
			stmt.setLong(1, accountNumber);
			ResultSet result = stmt.executeQuery();
			try {
				PlayerList ret = new PlayerList();
				while(result.next()) {
					ret.add(parsePlayer(result, gameWorldAccessor));
				}
				return ret;
			} finally {
				result.close();
			}
		} finally {
			if(stmt != null) {
				stmt.close();
			}
			connection.close();
		}
	}
	
	protected Player parsePlayer(ResultSet result, GameWorldAccessor<? extends GameWorld> gameWorldAccessor) throws SQLException {
		int id = result.getInt("players.id");
		String name = result.getString("players.name");
		GameWorld world = gameWorldAccessor.getGameWorld(result.getString("players.world"));
		Player player = new Player(id, name, world);
		
		return player;
	}

	public void createPlayer(Account account, Player player)
			throws PlayerAccessException {
		try {
			savePlayer(account, player);
		} catch(SQLException e) {
			throw new PlayerAccessException("Failed to create player.", e);
		}
	}
	
	private void savePlayer(Account account, Player player) throws SQLException {
		Connection connection = provider.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("INSERT INTO `players` (`id`, `name`, `account_number`, `world`) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, player.getGlobalId());
			stmt.setString(2, player.getName());
			stmt.setLong(3, account.getNumber());
			stmt.setString(4, player.getWorld().getIdentifier());
			stmt.execute();
			int result = stmt.getUpdateCount();
			if(result != 1) {
				throw new SQLException("Expected one updated row, but found " + result + " updated rows.");
			} else {
				ResultSet keys = stmt.getGeneratedKeys();
				if(keys.next()) {
					player.setGlobalId(keys.getInt(1));
				} else {
					throw new SQLException("Could not determine the new player's global id.");
				}
			}
		} finally {
			if(stmt != null) {
				stmt.close();
			}
			connection.close();
		}
	}

	public PlayerVipAccessor getPlayerVipAccessor()
			throws PlayerAccessException {
		if(vipAccessor == null) {
			vipAccessor = new MySQLPlayerVipAccessor(this);
		}
		return vipAccessor;
	}

	public Connection getConnection() {
		return provider.getConnection();
	}
}
