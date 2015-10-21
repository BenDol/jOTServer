package org.jotserver.ot.model.action.item;

import org.jotserver.ot.model.action.ActionVisitor;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.Stackable;

public class RemoveItemTargetAction extends ActionVisitor {
	
	private int count;
	private Item removedItem;
	
	public RemoveItemTargetAction(int count) {
		this.count = count;
		removedItem = null;
	}

	public void execute(Stackable stackable) {
		Stackable clone = stackable.clone(count);
		removedItem = clone;
		
		stackable.executeRemoveCount(count);
	}

	public boolean test(Stackable stackable) {
		ErrorType error = stackable.queryRemoveCount(count);
		if(error != ErrorType.NONE) {
			fail(error);
		}
		if(!hasFailed()) {
			Stackable clone = stackable.clone(count);
			removedItem = clone;
		}
		return !hasFailed();
	}
	
	public Item getRemovedItem() {
		return removedItem;
	}
}
