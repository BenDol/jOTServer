package org.jotserver.ot.model.item;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.world.LocalGameWorld;

public interface ItemUseEvent {
	LocalGameWorld getWorld();

	Creature getCreature();

	Item getItem();
}
