package org.jotserver.ot.model.item;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.event.EventEngine;
import org.jotserver.ot.model.world.LocalGameWorld;

public aspect ItemHandler {
	
	private pointcut useItem(Item item, Creature creature) : 
		target(item) &&
		args(creature) &&
		execution(public void Item.executeUse(Creature));
	
	after(final Item item, final Creature creature) : useItem(item, creature) {
		final LocalGameWorld world = item.getTile().getGameWorld();
		EventEngine engine = world.eventEngine;
		ItemEventEngine itemEngine = engine.getItemEngine();
		ItemUseEvent event = new ItemUseEvent() {
			public Creature getCreature() {
				return creature;
			}
			public Item getItem() {
				return item;
			}
			public LocalGameWorld getWorld() {
				return world;
			}};
		itemEngine.executeItemUse(event);
	}
	
}
