package org.jotserver.ot.model.action;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.item.Item;

public class UseThingAction extends ActionVisitor {
	
	private Creature creature;
	
	public UseThingAction(Creature creature) {
		this.creature = creature;
	}
	
	
	public void execute(Item item) {
		item.executeUse(creature);
	}
	
	
	public boolean test(Item item) {
		ErrorType error = item.queryUse(creature);
		if(error != ErrorType.NONE) {
			fail(error);
		}
		return !hasFailed();
	}
}
