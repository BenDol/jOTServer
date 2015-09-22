package org.jotserver.ot.model.map;

import org.jotserver.ot.model.world.LocalGameWorld;


public interface MapAccessor {
    Map loadMap(String directory, String identifier, LocalGameWorld world);
    void freeMap(String identifier);
}
