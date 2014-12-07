package org.jotserver.ot.model.action;

import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.Stackable;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Inventory;
import org.jotserver.ot.model.player.Player;

public interface Tester {
	public boolean test(Thing thing);
	
	public boolean test(Item item);

	public boolean test(Stackable stackable);

	public boolean test(Container container);

	public boolean test(Inventory inventory);
	
	public boolean test(Creature creature);

	public boolean test(Player player);
	
	public boolean test(Tile tile);
}