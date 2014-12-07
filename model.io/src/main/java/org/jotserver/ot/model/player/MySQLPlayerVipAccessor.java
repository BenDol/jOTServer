package org.jotserver.ot.model.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.model.world.GameWorldAccessor;

public class MySQLPlayerVipAccessor implements PlayerVipAccessor {
	
	private MySQLPlayerAccessor playerAccessor;

	public MySQLPlayerVipAccessor(MySQLPlayerAccessor playerAccessor) {
		this.playerAccessor = playerAccessor;
	}
	
	public List<Player> getPlayerVipList(Player player, GameWorld world)
			throws PlayerAccessException {
		try {
			return internalLoadPlayerVipList(player, world);
		} catch(SQLException e) {
			throw new PlayerAccessException("Failed to load VIP list.", e);
		}
	}
	
	private List<Player> internalLoadPlayerVipList(Player player, GameWorld world) throws SQLException {
		List<Player> vipList = new LinkedList<Player>();
		Connection connection = playerAccessor.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("SELECT * FROM `players` WHERE `world` = ? AND `id` IN(SELECT `player_id` FROM `account_vip` WHERE `account_id` IN(SELECT `account_number` FROM `players` WHERE `id` = ?))");
			stmt.setString(1, world.getIdentifier());
			stmt.setInt(2, player.getGlobalId());
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				Player vip = playerAccessor.parsePlayer(result, new SimpleGameWorldAccessor(world));
				vipList.add(vip);
			}
		} finally {
			if(stmt != null) {
				stmt.close();
			}
			connection.close();
		}
		return vipList;
	}

	public void savePlayerVipList(Player player) throws PlayerAccessException {
		List<Player> vipList = player.getInternal().getVipList();
		try {
			internalSavePlayerVipList(player, vipList);
		} catch (SQLException e) {
			throw new PlayerAccessException("Failed to save VIP list.", e);
		}
	}
	
	private void internalSavePlayerVipList(Player player, List<Player> vipList) throws SQLException {
		Connection connection = playerAccessor.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("DELETE FROM `account_vip` WHERE `account_id` IN(SELECT `account_number` FROM `players` WHERE `id` = ?) AND `player_id` IN(SELECT `id` FROM `players` WHERE `world` = ?)");
			stmt.setInt(1, player.getGlobalId());
			stmt.setString(2, player.getWorld().getIdentifier());
			stmt.executeUpdate();
			stmt.close();
			stmt = null;
			
			stmt = connection.prepareStatement("INSERT INTO `account_vip` (`account_id`, `player_id`) SELECT `account_number`, ? FROM `players` WHERE `id` = ?;");
			for(Player vip : vipList) {
				stmt.setInt(1, vip.getGlobalId());
				stmt.setInt(2, player.getGlobalId());
				stmt.executeUpdate();
			}
		} finally {
			if(stmt != null) {
				stmt.close();
			}
			connection.close();
		}
	}

}

class SimpleGameWorldAccessor implements GameWorldAccessor<GameWorld> {
	
	private HashMap<String, GameWorld> worldMap;
	
	public SimpleGameWorldAccessor(GameWorld... worlds) {
		worldMap = new HashMap<String, GameWorld>();
		for(GameWorld world : worlds) {
			worldMap.put(world.getIdentifier().toLowerCase(), world);
		}
	}
	
	public GameWorld getGameWorld(String identifier) {
		return worldMap.get(identifier.toLowerCase());
	}
	
	public Collection<GameWorld> getGameWorlds() {
		return Collections.unmodifiableCollection(worldMap.values());
	}
	
}