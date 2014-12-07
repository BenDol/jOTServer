package org.jotserver.ot.model.action.item;

import org.jotserver.ot.model.action.ActionVisitor;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Inventory;
import org.jotserver.ot.model.player.InventorySlot;
import org.jotserver.ot.model.util.Location;

public class AddItemCylinderAction extends ActionVisitor {
	
	private Item item;
	private Location to;
	
	public AddItemCylinderAction(Item item, Location to) {
		this.item = item;
		this.to = to;
	}

	public void execute(Inventory inventory) {
		InventorySlot slot = InventorySlot.getSlot(to.getIndex());
		inventory.executeAddItem(slot, item);
	}

	public void execute(Tile tile) {
		tile.executeAddItem(item);
	}

	public void execute(Container container) {
		container.executeAddItem(item);
	}

	public boolean test(Container container) {
		ErrorType error = container.queryAddItem(item);
		if(error != ErrorType.NONE) {
			fail(error);
		}
		return !hasFailed();
	}

	public boolean test(Inventory inventory) {
		InventorySlot slot = InventorySlot.getSlot(to.getIndex());
		if(slot == null) {
			fail(ErrorType.NOTPOSSIBLE);
		} else {
			ErrorType error = inventory.queryAddItem(slot, item);
			if(error != ErrorType.NONE) {
				fail(error);
			}
		}
		return !hasFailed();
	}

	public boolean test(Tile tile) {
		ErrorType error = tile.queryAddItem(item);
		if(error != ErrorType.NONE) {
			fail(error);
		}
		return !hasFailed();
	}
}
