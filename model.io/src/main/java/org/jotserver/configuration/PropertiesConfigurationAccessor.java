package org.jotserver.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.jotserver.io.PropertiesAccessor;
import org.jotserver.ot.model.MOTD;
import org.jotserver.ot.model.MOTDAccessException;
import org.jotserver.ot.model.MOTDAccessor;
import org.jotserver.ot.model.account.AccountAccessor;
import org.jotserver.ot.model.account.MySQLAccountAccessor;
import org.jotserver.ot.model.player.MySQLPlayerAccessor;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.model.world.GameWorldAccessor;
import org.jotserver.ot.model.world.GameWorldConfigurationAccessor;
import org.jotserver.ot.model.world.PropertiesGameWorldAccessor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class PropertiesConfigurationAccessor extends PropertiesAccessor implements ConfigurationAccessor, MOTDAccessor, ConnectionProvider {

    private PropertiesGameWorldAccessor gameWorldAccessor;
    private ComboPooledDataSource dataSource;

    public PropertiesConfigurationAccessor(String file) throws FileNotFoundException, IOException {
        super(file);
        gameWorldAccessor = null;
        dataSource = null;
    }

    public Connection getConnection() {
        if(dataSource == null) {
            dataSource = new ComboPooledDataSource();
            //cpds.setDriverClass("org.postgresql.Driver");
            dataSource.setJdbcUrl(getDataSourceUrl());
            dataSource.setUser(getDataSourceUsername());
            dataSource.setPassword(getDataSourcePassword());
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConfigurationException(e);
        }
    }

    private String getDataSourceUrl() {
        String key = "datasource.url";
        return getString(key);
    }

    private String getDataSourceUsername() {
        return getString("datasource.username");
    }

    private String getDataSourcePassword() {
        return getString("datasource.password");
    }

    private String getHost() {
        return getString("host");
    }


    public int getPort() {
        return getInt("port");
    }

    public AccountAccessor getAccountAccessor() {
        return new MySQLAccountAccessor(this);
    }

    public GameWorldAccessor<GameWorld> getGameWorldAccessor() {
        return getPropertiesGameWorldAccessor();
    }

    private PropertiesGameWorldAccessor getPropertiesGameWorldAccessor() {
        if(gameWorldAccessor == null) {
            try {
                gameWorldAccessor = new PropertiesGameWorldAccessor(getParentPath() + getString("world.file"), getHost(), getPort());
            } catch (FileNotFoundException e) {
                throw new ConfigurationException("World file not found.", e);
            } catch (IOException e) {
                throw new ConfigurationException("Failed to read world file.", e);
            }
        }
        return gameWorldAccessor;
    }

    public MOTD getMOTD() throws MOTDAccessException {
        return new MOTD(getInt("motd.number", 1), get("motd.message", "Welcome"));
    }

    public MOTDAccessor getMOTDAccessor() {
        return this;
    }

    public GameWorldConfigurationAccessor getGameWorldConfigurationAccessor() {
        return getPropertiesGameWorldAccessor();
    }

    public PlayerAccessor getPlayerAccessor() {
        return new MySQLPlayerAccessor(this);
    }
}
