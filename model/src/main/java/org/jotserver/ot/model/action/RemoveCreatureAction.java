package org.jotserver.ot.model.action;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Tile;

public class RemoveCreatureAction extends ActionVisitor {
	
	private Creature creature;

	public RemoveCreatureAction(Creature creature) {
		this.creature = creature;
	}

	public void execute(Tile tile) {
		tile.executeRemoveCreature(creature);
	}

	public boolean test(Tile tile) {
		ErrorType error = tile.queryRemoveCreature(creature);
		if(error != ErrorType.NONE) {
			fail(error);
		}
		return !hasFailed();
	}
}
