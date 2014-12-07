package org.jotserver.ot.model.action;

import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.Stackable;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Inventory;
import org.jotserver.ot.model.player.Player;

public interface Visitor {
	public void execute(Thing thing);
	
	public void execute(Item item);
	public void execute(Stackable stackable);
	public void execute(Container container);
	public void execute(Inventory inventory);
	
	public void execute(Creature creature);
	public void execute(Player player);
	
	public void execute(Tile tile);
}
