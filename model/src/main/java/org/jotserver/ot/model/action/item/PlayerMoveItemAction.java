package org.jotserver.ot.model.action.item;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.action.ActionVisitable;
import org.jotserver.ot.model.action.ActionVisitor;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.action.PlayerAction;
import org.jotserver.ot.model.action.item.AddItemTargetAction.Result;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Dispatcher;
import org.jotserver.ot.model.util.Location;

public class PlayerMoveItemAction extends PlayerAction {
	
	private Location to;
	private int count;
	private Item item;

	public PlayerMoveItemAction(Dispatcher dispatcher, Player player, Item item, int count, Location to) {
		super(dispatcher, player);
		this.item = item;
		this.count = count;
		this.to = to;
	}
	
	
	public boolean execute() {
		ActionVisitor remove = null;
		ActionVisitable removeFrom = null;
		Item moveItem = item;
		
		if(player.getPosition().distanceTo(item.getPosition()) > 1) {
			if(!walkToExecute(item.getPosition(), 1)) {
				fail(ErrorType.THEREISNOWAY);
				return false;
			} else {
				return true;
			}
		}
		
		PlayerRemoveItemTargetAction removeTarget = new PlayerRemoveItemTargetAction(player, count);
		PlayerRemoveItemCylinderAction removeCylinder = new PlayerRemoveItemCylinderAction(player, item);
		if(item.test(removeTarget)) {
			remove = removeTarget;
			removeFrom = item;
			moveItem = removeTarget.getRemovedItem();
		} else if(item.getParent() != null && item.getParent().test(removeCylinder)) {
			remove = removeCylinder;
			removeFrom = item.getParent();
			moveItem = removeCylinder.getRemovedItem();
		} else {
			fail(removeCylinder.getError());
			return false;
		}
		
		ActionVisitor add = null;
		ActionVisitable addTo = null;
		
		PlayerAddItemTargetAction addTarget = new PlayerAddItemTargetAction(player, moveItem);
		ActionVisitor addCylinder = new PlayerAddItemCylinderAction(player, moveItem, to);
		Thing toThing = to.get();
		if(toThing != null && toThing.test(addTarget)) {
			add = addTarget;
			addTo = toThing;
		} else if(toThing != null && addTarget.getResult() == Result.PARTIAL) {
			int delta = count - addTarget.getPartialCount();
			count = addTarget.getPartialCount();
			if(execute()) { // First add what we can to the target.
				count = delta;
				return execute(); // Then try to add the rest normally. 
			} else {
				return false;
			}
		} else {
			Cylinder toCylinder = to.getCylinder();
			if(toCylinder.test(addCylinder)) {
				add = addCylinder;
				addTo = toCylinder;
			} else {
				fail(addCylinder.getError());
				return false;
			}
		}
		
		removeFrom.execute(remove);
		addTo.execute(add);
		return true;
	}

}
