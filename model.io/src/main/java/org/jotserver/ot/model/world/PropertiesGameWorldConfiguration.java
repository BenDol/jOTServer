package org.jotserver.ot.model.world;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.jotserver.configuration.ConfigurationException;
import org.jotserver.configuration.ConnectionProvider;
import org.jotserver.io.PropertiesAccessor;
import org.jotserver.ot.model.OutfitAccessor;
import org.jotserver.ot.model.XMLOutfitAccessor;
import org.jotserver.ot.model.item.ItemTypeAccessor;
import org.jotserver.ot.model.item.OTBItemTypeAccessor;
import org.jotserver.ot.model.item.XMLItemTypeLoader;
import org.jotserver.ot.model.map.MapAccessor;
import org.jotserver.ot.model.map.OTBMMapAccessor;
import org.jotserver.ot.model.player.MySQLPlayerDataAccessor;
import org.jotserver.ot.model.player.PlayerDataAccessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class PropertiesGameWorldConfiguration extends PropertiesAccessor implements GameWorldConfiguration, ConnectionProvider {

    private MapAccessor mapAccessor;
    private OTBItemTypeAccessor itemTypeAccessor;
    private XMLOutfitAccessor outfitAccessor;
    private ComboPooledDataSource dataSource;

    public PropertiesGameWorldConfiguration(String file)
            throws FileNotFoundException, IOException {
        super(file);
        mapAccessor = null;
        itemTypeAccessor = null;
        outfitAccessor = null;
    }

    public Connection getConnection() {
        if(dataSource == null) {
            dataSource = new ComboPooledDataSource();
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

    public String getDirectory() {
        return getString("data.directory");
    }

    public String getMapIdentifier() {
        return getString("data.map.identifier");
    }

    public PlayerDataAccessor getPlayerDataAccessor() {
        return new MySQLPlayerDataAccessor(this);
    }

    public MapAccessor getMapAccessor() {
        if(mapAccessor == null) {
            mapAccessor = new OTBMMapAccessor();
        }
        return mapAccessor;
    }

    public ItemTypeAccessor getItemTypeAccessor() {
        if(itemTypeAccessor == null) {
            itemTypeAccessor = new OTBItemTypeAccessor(10000);
            try {
                itemTypeAccessor.loadFromOtb(new File(getDirectory(), getString("items.otb")).getAbsolutePath());
                new XMLItemTypeLoader(itemTypeAccessor).loadFromXML(new File(getDirectory(), getString("items.xml")).getAbsolutePath());
            } catch (IOException e) {
                itemTypeAccessor = null;
                throw new ConfigurationException("Failed to load items binary.", e);
            }
        }
        return itemTypeAccessor;
    }

    public OutfitAccessor getOutfitAccessor() {
        if(outfitAccessor == null) {
            outfitAccessor = new XMLOutfitAccessor();
            try {
                outfitAccessor.loadFromXML(new File(getDirectory(), getString("outfits.xml")).getAbsolutePath());
            } catch (IOException e) {
                throw new ConfigurationException("Failed to load outfits." , e);
            }
        }
        return outfitAccessor;
    }
}
