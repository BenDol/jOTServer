package org.jotserver.ot.model.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class LocalGameWorldAccessor implements GameWorldAccessor<LocalGameWorld> {

    private HashMap<String, LocalGameWorld> worlds;

    private LocalGameWorldAccessor() {
        worlds = new HashMap<String, LocalGameWorld>();
    }

    public LocalGameWorldAccessor(Collection<LocalGameWorld> worlds) {
        this();
        for(LocalGameWorld world : worlds) {
            this.worlds.put(world.getIdentifier(), world);
        }
    }

    public LocalGameWorld getGameWorld(String identifier) {
        return worlds.get(identifier);
    }

    public Collection<LocalGameWorld> getGameWorlds() {
        return new ArrayList<LocalGameWorld>(worlds.values());
    }
}
