package org.jotserver.ot.model.action;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Tile;

public class AddCreatureAction extends ActionVisitor {
	
	private Creature creature;
	private boolean pathFind;

	public AddCreatureAction(Creature creature) {
		this.creature = creature;
	}
	public AddCreatureAction(Creature creature, boolean pathFind) {
		this.creature = creature;
		this.pathFind = pathFind;
	}

	public void execute(Tile tile) {
		tile.executeAddCreature(creature);
	}

	public boolean test(Tile tile) {
		ErrorType error = tile.queryAddCreature(creature, pathFind);
		if(error != ErrorType.NONE) {
			fail(error);
		}
		return !hasFailed();
	}
}
