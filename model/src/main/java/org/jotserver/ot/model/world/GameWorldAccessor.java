package org.jotserver.ot.model.world;

import java.util.Collection;


public interface GameWorldAccessor<E extends GameWorld> {
	E getGameWorld(String identifier);
	Collection<E> getGameWorlds();
}
