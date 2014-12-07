package org.jotserver.ot.model.action;

import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.Stackable;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Inventory;
import org.jotserver.ot.model.player.Player;

public abstract class ActionVisitor implements Visitor, Tester {
	
	protected ErrorType error;
	
	public ActionVisitor() {
		error = ErrorType.NONE;
	}
	
	public boolean test(Thing thing) { return false; }
	public boolean test(Item item) { return test((Thing)item); }
	public boolean test(Stackable stackable) { return test((Item)stackable); }
	public boolean test(Container container) { return test((Item)container); }
	public boolean test(Inventory inventory) { return false; }
	
	public boolean test(Creature creature) { return test((Thing)creature); }
	public boolean test(Player player) { return test((Creature)player); }
	
	public boolean test(Tile tile) { return false; }
	
	
	public void execute(Thing thing) {}
	public void execute(Item item) { execute((Thing)item); }
	public void execute(Stackable stackable) { execute((Item)stackable); }
	public void execute(Container container) { execute((Item)container); }
	public void execute(Inventory inventory) {}
	
	public void execute(Creature creature) { execute((Thing)creature); }
	public void execute(Player player) { execute((Creature)player); }
	
	public void execute(Tile tile) {}
	
	public ErrorType getError() {
		return error;
	}
	
	protected void fail(ErrorType error) {
		this.error = error;
	}
	
	public boolean hasFailed() {
		return error != ErrorType.NONE;
	}
	
}
