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
	boolean test(Thing thing);
	
	boolean test(Item item);

	boolean test(Stackable stackable);

	boolean test(Container container);

	boolean test(Inventory inventory);
	
	boolean test(Creature creature);

	boolean test(Player player);
	
	boolean test(Tile tile);
}