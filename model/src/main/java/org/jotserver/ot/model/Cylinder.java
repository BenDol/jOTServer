package org.jotserver.ot.model;

import org.jotserver.ot.model.action.ActionVisitable;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Spectators;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.Location;

public interface Cylinder extends ActionVisitable {
	public Tile getTile();
	public Cylinder getParent();
	public boolean isPlaced();
	public boolean isVisibleTo(Creature creature);
	
	public <T extends Creature> Spectators<T> getContentsSpectators(Class<T> type);
	
	public Location getLocationOf(Thing thing);
}
