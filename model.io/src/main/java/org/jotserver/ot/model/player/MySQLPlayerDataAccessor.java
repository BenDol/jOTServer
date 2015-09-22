package org.jotserver.ot.model.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jotserver.configuration.ConnectionProvider;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.ItemType;
import org.jotserver.ot.model.item.ItemTypeAccessor;
import org.jotserver.ot.model.map.Town;
import org.jotserver.ot.model.map.TownAccessor;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Position;

public class MySQLPlayerDataAccessor implements PlayerDataAccessor {

    private static final Logger logger = Logger
            .getLogger(MySQLPlayerDataAccessor.class);

    private ConnectionProvider provider;

    public MySQLPlayerDataAccessor(ConnectionProvider provider) {
        this.provider = provider;
    }

    public void loadPlayerData(Player player, ItemTypeAccessor items, TownAccessor towns) throws PlayerAccessException {
        try {
            if (!internalLoadPlayerData(player, items, towns)) {
                throw new PlayerAccessException("Could not find player data.");
            }
        } catch (SQLException e) {
            throw new PlayerAccessException("Failed to load player data.", e);
        }
    }

    private boolean internalLoadPlayerData(Player player, ItemTypeAccessor items, TownAccessor towns) throws SQLException,
            PlayerAccessException {
        Connection connection = provider.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = connection
                    .prepareStatement("SELECT * FROM `player_data` WHERE `id` = ?");
            stmt.setInt(1, player.getGlobalId());
            ResultSet result = stmt.executeQuery();
            try {
                if (result.next()) {
                    parsePlayerData(player, towns, result);
                    internalLoadPlayerInventory(connection, items, player
                            .getInventory());
                    return true;
                } else {
                    return false;
                }
            } finally {
                result.close();
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            connection.close();
        }
    }

    private void parsePlayerData(Player player, TownAccessor towns, ResultSet result)
            throws SQLException {
        Position pos = new Position(result.getInt("player_data.position_x"),
                result.getInt("player_data.position_y"), result
                        .getInt("player_data.position_z"));

        player.setTemporaryPosition(pos);

        int townId = result.getInt("player_data.town_id");
        Town town = towns.getTown(townId);
        if (town != null) {
            player.setTown(town);
        } else {
            logger.warn("Player " + player.getName()
                    + " has an unknown home town id of " + townId + ".");
        }

        player.getInternal().setCapacity(result.getInt("player_data.capacity"));
        player.getInternal().turn(
                Direction.valueOf(result.getString("player_data.direction")
                        .toUpperCase()));

        player.getInternal().setLevel(result.getInt("player_data.level"));
        player.getInternal().setExperience(
                result.getInt("player_data.experience"));

        player.getInternal().setHealth(result.getInt("player_data.health"));
        player.getInternal().setMaxHealth(
                result.getInt("player_data.max_health"));

        player.getInternal().setMagicLevel(
                result.getInt("player_data.magic_level"));
        player.getInternal().setMana(result.getInt("player_data.mana"));
        player.getInternal().setMaxMana(result.getInt("player_data.max_mana"));
        player.getInternal().setSoul(result.getInt("player_data.soul"));

        player.setStamina(result.getInt("player_data.stamina"));
    }

    public void savePlayerData(Player player) throws PlayerAccessException {
        try {
            if (!internalSavePlayerData(player)) {
                throw new PlayerAccessException("Failed to save player.");
            }
        } catch (SQLException e) {
            throw new PlayerAccessException("Error when saving player.", e);
        }
    }

    private boolean internalSavePlayerData(Player player) throws SQLException {
        Connection connection = provider.getConnection();
        PreparedStatement delete = connection
                .prepareStatement("DELETE FROM `player_data` WHERE `id` = ?");
        try {
            delete.setInt(1, player.getGlobalId());
            delete.executeUpdate();
        } finally {
            delete.close();
        }
        PreparedStatement stmt = null;
        try {
            stmt = connection
                    .prepareStatement("INSERT INTO `player_data` (`id`, `position_x`, `position_y`, `position_z`, `town_id`, `capacity`, `direction`, `level`, `experience`, "
                            + "`health`, `max_health`, `magic_level`, `mana`, `max_mana`, `soul`, `stamina`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            int i = 1;
            InternalPlayer internal = player.getInternal();
            Position pos = internal.getPosition();
            if (pos == null) {
                pos = player.getTemporaryPosition();
                if (pos == null) {
                    pos = new Position();
                }
            }

            stmt.setLong(i++, player.getGlobalId());

            stmt.setInt(i++, pos.getX());
            stmt.setInt(i++, pos.getY());
            stmt.setInt(i++, pos.getZ());

            stmt.setInt(i++, player.getTown().getId());

            stmt.setInt(i++, internal.getCapacity());
            stmt.setString(i++, internal.getDirection().toString());

            stmt.setInt(i++, internal.getLevel());
            stmt.setLong(i++, internal.getExperience());

            stmt.setInt(i++, internal.getHealth());
            stmt.setInt(i++, internal.getMaxHealth());

            stmt.setInt(i++, internal.getMagicLevel());
            stmt.setInt(i++, internal.getMana());
            stmt.setInt(i++, internal.getMaxMana());

            stmt.setInt(i++, internal.getSoul());

            stmt.setInt(i++, player.getStamina());

            // stmt.setLong(i++, player.getGlobalId());

            int ret = stmt.executeUpdate();

            if (ret == 1) {
                internalSavePlayerInventory(connection, player.getInventory());
                return true;
            } else {
                return false;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            connection.close();
        }
    }

    private void internalLoadPlayerInventory(Connection connection,
            ItemTypeAccessor items, Inventory inventory) throws SQLException, PlayerAccessException {
        PreparedStatement stmt = connection
                .prepareStatement("SELECT * FROM `player_items` WHERE `player_id` = ? ORDER BY `parent` ASC, `index` DESC");
        try {
            stmt.setInt(1, inventory.getPlayer().getGlobalId());
            ResultSet result = stmt.executeQuery();
            try {
                internalParsePlayerInventory(inventory, items, result);
            } finally {
                result.close();
            }
        } finally {
            stmt.close();
        }
    }

    private void internalParsePlayerInventory(Inventory inventory,
            ItemTypeAccessor items, ResultSet result) throws SQLException, PlayerAccessException {
        Map<Integer, Container> containers = new HashMap<Integer, Container>();
        while (result.next()) {
            int id = result.getInt("player_items.id");
            int parent = result.getInt("player_items.parent");
            int index = result.getInt("player_items.index");
            int itemId = result.getInt("player_items.itemid");
            ItemType type = items.getItemType(itemId);
            if (type == null) {
                throw new PlayerAccessException(
                    "Attempted to load invalid item with id " + itemId
                    + " to player " + inventory.getPlayer() + ".");
            } else {
                Item item = items.createItem(type);
                if (type.isContainer()) {
                    Container container = (Container) item;
                    containers.put(id, container);
                    item = container;
                }
                if (parent == 0) {
                    inventory
                            .executeAddItem(InventorySlot.getSlot(index), item);
                } else {
                    Container container = containers.get(parent);
                    if (container != null) {
                        container.executeAddItem(item);
                    } else {
                        throw new PlayerAccessException(
                                "Item belonging to not yet found container.");
                    }
                }
            }
        }
    }

    private void internalSavePlayerInventory(Connection connection,
            Inventory inventory) throws SQLException {
        PreparedStatement delete = connection
                .prepareStatement("DELETE FROM `player_items` WHERE `player_id` = ?");
        try {
            delete.setInt(1, inventory.getPlayer().getGlobalId());
            delete.executeUpdate();
        } finally {
            delete.close();
        }

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO `player_items` "
            + "(`player_id`, `id`, `parent`, `index`, `itemid`) "
            + "VALUES(?, ?, ?, ?, ?)");
        try {
            int id = 1;
            for (InventorySlot slot : InventorySlot.values()) {
                Item item = inventory.getItem(slot);
                if (item != null) {
                    id = internalSavePlayerItem(stmt, item, inventory
                            .getPlayer().getGlobalId(), id, 0, slot.ordinal());
                }
            }
        } finally {
            stmt.close();
        }
    }

    private int internalSavePlayerItem(PreparedStatement stmt, Item item,
            int playerId, int id, int parent, int index) throws SQLException {
        int thisId = id;
        stmt.setInt(1, playerId);
        stmt.setInt(2, id++);
        stmt.setInt(3, parent);
        stmt.setInt(4, index);

        stmt.setInt(5, item.getId());
        // TODO: Save/load item count. Binary property streams?
        stmt.executeUpdate();

        if (item instanceof Container) {
            Container container = (Container) item;
            for (int i = 0; i < container.getItemCount(); i++) {
                id = internalSavePlayerItem(stmt, container.getItem(i),
                        playerId, id, thisId, i);
            }
        }
        return id;
    }
}
