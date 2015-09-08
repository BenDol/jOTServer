package org.jotserver.ot.model;

import org.jotserver.ot.model.action.ActionVisitable;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Spectators;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.Location;

public interface Cylinder extends ActionVisitable {
	Tile getTile();

	Cylinder getParent();

	boolean isPlaced();

	boolean isVisibleTo(Creature creature);
	
	<T extends Creature> Spectators<T> getContentsSpectators(Class<T> type);
	
	Location getLocationOf(Thing thing);
}
