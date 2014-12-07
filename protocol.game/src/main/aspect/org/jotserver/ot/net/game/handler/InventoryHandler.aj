package org.jotserver.ot.net.game.handler;

import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.player.InternalInventory;
import org.jotserver.ot.model.player.InventorySlot;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.creature.InventorySetItemWriter;

public aspect InventoryHandler {
	
	public pointcut inventorySetItem(InternalInventory inventory, InventorySlot slot, Item item) :
		target(inventory) &&
		args(slot, item) &&
		execution(public void InternalInventory.setItem(InventorySlot, Item));
	
	
	after(InternalInventory inventory, InventorySlot slot, Item item) returning : inventorySetItem(inventory, slot, item) {
		Player player = inventory.getInventory().getPlayer();
		if(player.isOnline()) {
			player.getGameProtocol().send(new InventorySetItemWriter(player, slot, item));
		}
	}
	
}
