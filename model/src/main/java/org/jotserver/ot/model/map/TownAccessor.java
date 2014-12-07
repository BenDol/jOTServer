package org.jotserver.ot.model.map;

import java.util.Collection;

public interface TownAccessor {
	public Town getTown(int id);
	public Town getTown(String name);
	public Collection<Town> getTowns();
}
