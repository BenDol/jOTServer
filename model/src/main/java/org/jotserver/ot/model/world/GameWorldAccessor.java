package org.jotserver.ot.model.world;

import java.util.Collection;


public interface GameWorldAccessor<E extends GameWorld> {
	public E getGameWorld(String identifier);
	public Collection<E> getGameWorlds();
}
