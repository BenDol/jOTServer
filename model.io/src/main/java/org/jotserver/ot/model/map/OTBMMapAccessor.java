package org.jotserver.ot.model.map;

import org.apache.log4j.Logger;
import org.jotserver.configuration.ConfigurationException;
import org.jotserver.ot.model.world.LocalGameWorld;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class OTBMMapAccessor implements MapAccessor {
    private static Logger logger = Logger.getLogger(OTBMMapAccessor.class);

    private HashMap<String, OTBMMap> maps;

    public OTBMMapAccessor() {
        maps = new HashMap<String, OTBMMap>();
    }

    public void freeMap(String identifier) {
        maps.remove(identifier);
    }

    public Map loadMap(String directory, String identifier, LocalGameWorld world) {
        OTBMMap map = maps.get(identifier);
        if(map == null) {
            map = new OTBMMap(world);
            try {
                long t = System.nanoTime();
                map.loadFromOTBM(new File(directory, identifier + ".otbm").getAbsolutePath(), world.getItemTypes());
                logger.info("Load time for map " + identifier + ": " + (System.nanoTime()-t)/1000000000.0 + " seconds.");
                maps.put(identifier, map);
            } catch (IOException e) {
                map = null;
                throw new ConfigurationException("Failed to load map " + identifier + ".", e);
            }
        }
        return map;
    }
}
