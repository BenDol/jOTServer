package org.jotserver.ot.model.action.item;

import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.ItemAttribute;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Inventory;
import org.jotserver.ot.model.player.Player;

public class PlayerRemoveItemCylinderAction extends RemoveItemCylinderAction {

	private Item item;
	private Player player;

	public PlayerRemoveItemCylinderAction(Player player, Item item) {
		super(item);
		this.player = player;
		this.item = item;
	}
	
	
	public boolean test(Inventory inventory) {
		if(!super.test(inventory)) {
			//
		} else if(!inventory.getPlayer().equals(player)) {
			fail(ErrorType.NOTPOSSIBLE);
		} else {
			testItem();
		}
		return !hasFailed();
	}
	
	
	public boolean test(Tile tile) {
		if(!super.test(tile)) {
			//
		} else if(tile.getPosition().distanceTo(player.getPosition()) > 1) {
			fail(ErrorType.TOOFARAWAY);
		} else {
			testItem();
		}
		return !hasFailed();
	}
	
	
	public boolean test(Container container) {
		if(!super.test(container)) {
			//
		} else if(player.getPosition().distanceTo(container.getPosition()) > 1) {
			fail(ErrorType.TOOFARAWAY);
		} else if(!container.isContentsSpectator(player)) {
			fail(ErrorType.NOTPOSSIBLE);
		} else {
			testItem();
		}
		return !hasFailed();
	}
	
	private void testItem() {
		if(!item.hasAttribute(ItemAttribute.MOVEABLE)) {
			fail(ErrorType.NOTMOVEABLE);
		} 
	}
}
