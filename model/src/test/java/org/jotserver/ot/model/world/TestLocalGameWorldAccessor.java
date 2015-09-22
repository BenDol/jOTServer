package org.jotserver.ot.model.world;


import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestLocalGameWorldAccessor {

    private ArrayList<LocalGameWorld> worlds;
    private LocalGameWorldAccessor accessor;

    @Before
    public void setUp() throws Exception {
        worlds = new ArrayList<LocalGameWorld>();

        GameWorld world = new BaseGameWorld("world1", "World1");
        worlds.add(new LocalGameWorld(null, world, null, null));

        world = new BaseGameWorld("world2", "World2");
        worlds.add(new LocalGameWorld(null, world, null, null));

        world = new BaseGameWorld("world3", "World3");
        worlds.add(new LocalGameWorld(null, world, null, null));

        accessor = new LocalGameWorldAccessor(worlds);
    }

    @Test
    public void findsGameWorldByIdentifier() {
        assertEquals(worlds.get(0), accessor.getGameWorld("world1"));
        assertEquals(worlds.get(1), accessor.getGameWorld("world2"));
        assertEquals(worlds.get(2), accessor.getGameWorld("world3"));
    }

    @Test
    public void providesWorldList() {
        assertTrue(worlds.containsAll(accessor.getGameWorlds()));
        assertEquals(worlds.size(), accessor.getGameWorlds().size());
    }

}
