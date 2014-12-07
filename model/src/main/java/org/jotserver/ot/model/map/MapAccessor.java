package org.jotserver.ot.model.map;

import org.jotserver.ot.model.world.LocalGameWorld;


public interface MapAccessor {
	public Map loadMap(String directory, String identifier, LocalGameWorld world);
	public void freeMap(String identifier);
}
