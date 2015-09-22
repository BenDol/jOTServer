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
    void execute(Thing thing);

    void execute(Item item);

    void execute(Stackable stackable);

    void execute(Container container);

    void execute(Inventory inventory);

    void execute(Creature creature);

    void execute(Player player);

    void execute(Tile tile);
}
