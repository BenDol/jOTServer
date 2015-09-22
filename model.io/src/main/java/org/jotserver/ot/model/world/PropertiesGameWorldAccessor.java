package org.jotserver.ot.model.world;

import org.jotserver.configuration.ConfigurationException;
import org.jotserver.io.PropertiesAccessor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

public class PropertiesGameWorldAccessor extends PropertiesAccessor implements GameWorldAccessor<GameWorld>, GameWorldConfigurationAccessor {

    private Map<String, GameWorld> cache;
    private int localPort;
    private String localHost;

    public PropertiesGameWorldAccessor(String file, String localHost, int localPort) throws FileNotFoundException, IOException {
        super(file);

        this.localHost = localHost;
        this.localPort = localPort;

        cache = new HashMap<String, GameWorld>();
        load();
    }

    public GameWorld getGameWorld(String identifier) {
        GameWorld ret = cache.get(identifier);
        return ret;
    }

    public Collection<GameWorld> getGameWorlds() {
        return new ArrayList<GameWorld>(cache.values());
    }

    private void load() {
        for(String key : properties.stringPropertyNames()) {
            StringTokenizer token = new StringTokenizer(key, ".");
            if(token.hasMoreTokens()) {
                String identifier = token.nextToken();
                if(!cache.containsKey(identifier)) {
                    load(identifier);
                }
            }
        }
    }

    private void load(String string) {
        String identifier = get(string + ".identifier", string);
        String name = getString(string + ".name");

        boolean local = getBoolean(string + ".local");
        String host;
        int port;
        if(local) {
            host = localHost;
            port = localPort;
        } else {
            host = getString(string + ".host");
            port = getInt(string + ".port");
        }

        InetSocketAddress address = new InetSocketAddress(host, port);
        if(address.isUnresolved()) {
            throw new ConfigurationException("Failed to resolve host name for world " + string + ".");
        }
        GameWorld ret = new BaseGameWorld(identifier, name, address, local);
        cache.put(string, ret);
    }

    public GameWorldConfiguration getGameWorldConfiguration(String identifier) {
        try {
            return new PropertiesGameWorldConfiguration(getParentPath() + identifier + ".properties");
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load game world configuration for " + identifier + ".", e);
        }
    }
}
