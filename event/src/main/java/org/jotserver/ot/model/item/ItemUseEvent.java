package org.jotserver.ot.model.item;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.world.LocalGameWorld;

public interface ItemUseEvent {
	public LocalGameWorld getWorld();
	public Creature getCreature();
	public Item getItem();
}
