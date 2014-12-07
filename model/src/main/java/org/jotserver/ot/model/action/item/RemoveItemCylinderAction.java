package org.jotserver.ot.model.action.item;

import org.jotserver.ot.model.action.ActionVisitor;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Inventory;

public class RemoveItemCylinderAction extends ActionVisitor {
	
	private Item item;
	private Item removedItem;
	
	public RemoveItemCylinderAction(Item item) {
		this.item = item;
		removedItem = item;
	}
	
	
	public void execute(Inventory inventory) {
		inventory.executeRemoveItem(item);
		removedItem = item;
	}
	
	
	public void execute(Tile tile) {
		tile.executeRemoveItem(item);
		removedItem = item;
	}
	
	
	public void execute(Container container) {
		container.executeRemoveItem(item);
		removedItem = item;
	}
	
	
	
	public boolean test(Inventory inventory) {
		ErrorType error = inventory.queryRemoveItem(item);
		if(error != ErrorType.NONE) {
			fail(error);
		}
		return !hasFailed();
	}
	
	
	public boolean test(Tile tile) {
		ErrorType error = tile.queryRemoveItem(item);
		if(error != ErrorType.NONE) {
			fail(error);
		}
		return !hasFailed();
	}
	
	
	public boolean test(Container container) {
		ErrorType error = container.queryRemoveItem(item);
		if(error != ErrorType.NONE) {
			fail(error);
		}
		return !hasFailed();
	}

	public Item getRemovedItem() {
		return removedItem;
	}
	
}
