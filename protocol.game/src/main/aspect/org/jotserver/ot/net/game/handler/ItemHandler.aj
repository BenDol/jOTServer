package org.jotserver.ot.net.game.handler;

import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.FluidType;
import org.jotserver.ot.model.item.InternalItem;
import org.jotserver.ot.model.item.InternalStackable;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.player.InventorySlot;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.net.game.creature.InventorySetItemWriter;
import org.jotserver.ot.net.game.out.ContainerUpdateItemWriter;
import org.jotserver.ot.net.game.out.TileUpdateItemWriter;

public aspect ItemHandler {
	
	public pointcut updateItem(InternalItem internalItem) : 
		target(internalItem) &&
		(execution(public void InternalStackable.setCount(int)) ||
				execution(public void InternalItem.setFluidType(FluidType)));
	
	after(InternalItem internalItem) returning : updateItem(internalItem) {
		Item item = internalItem.getItem();
		if(item.isPlaced()) {
			Position pos = item.getPosition();
			for(Player player : item.getSpectators(Player.class)) {
				if(player.isOnline()) {
					if(player.getInventory().equals(item.getParent())) { // In inventory
						InventorySlot slot = player.getInventory().getSlot(item);
						player.getGameProtocol().send(new InventorySetItemWriter(player, slot, item));
					} else if(item.getParent() instanceof Container) { // In container
						int stack = ((Container)item.getParent()).getSlot(item);
						player.getGameProtocol().send(new ContainerUpdateItemWriter(player, (Container)item.getParent(), stack));
					} else if(player.canSee(pos)) { // On map
						int stack = item.getTile().getIndexOf(item);
						player.getGameProtocol().send(new TileUpdateItemWriter(player, pos, stack, item));
					}
				}
			}
		}
	}
	
}
