package org.jotserver.ot.model.map;

import java.util.Collection;

public interface TownAccessor {
	Town getTown(int id);
	Town getTown(String name);
	Collection<Town> getTowns();
}
