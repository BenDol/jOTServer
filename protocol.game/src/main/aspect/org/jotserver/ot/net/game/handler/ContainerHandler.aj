package org.jotserver.ot.net.game.handler;

import org.jotserver.ot.model.SimpleSpectators;
import org.jotserver.ot.model.item.InternalContainer;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.net.game.out.CloseContainerWriter;
import org.jotserver.ot.net.game.out.ContainerAddItemWriter;
import org.jotserver.ot.net.game.out.ContainerRemoveItemWriter;
import org.jotserver.ot.net.game.out.OpenContainerWriter;

public aspect ContainerHandler {
	
	public pointcut openContainer(Player player, InternalContainer internalContainer) :
		target(internalContainer) &&
		args(player) &&
		execution(public void InternalContainer.open(Creature));
	
	public pointcut closeContainer(Player player, InternalContainer internalContainer) :
		target(internalContainer) &&
		args(player) &&
		execution(public void InternalContainer.close(Creature));
	
	public pointcut addContainerItem(InternalContainer internalContainer, Item item) :
		target(internalContainer) &&
		args(item) &&
		execution(public void InternalContainer.addItem(Item));
	
	public pointcut removeContainerItem(InternalContainer internalContainer, int slot) :
		target(internalContainer) &&
		args(slot) &&
		execution(public void InternalContainer.removeItem(int));
	
	after(Player player, InternalContainer internalContainer) returning : 
			openContainer(player, internalContainer) {
		
		if(player.isOnline()) {
			player.getGameProtocol().send(new OpenContainerWriter(player, internalContainer.getContainer()));
		}
	}
	
	before(Player player, InternalContainer internalContainer) : 
		closeContainer(player, internalContainer) {
		
		if(player.isOnline()) {
			player.getGameProtocol().send(new CloseContainerWriter(player, internalContainer.getContainer()));
		}
	}
	
	after(InternalContainer internalContainer, Item item) : addContainerItem(internalContainer, item) {
		for(Player player : new SimpleSpectators<Player>(Player.class, internalContainer.getSpectators())) {
			if(player.isOnline()) {
				player.getGameProtocol().send(new ContainerAddItemWriter(player, internalContainer.getContainer(), item));
			}
		}
	}
	
	after(InternalContainer internalContainer, int slot) : removeContainerItem(internalContainer, slot) {
		for(Player player : new SimpleSpectators<Player>(Player.class, internalContainer.getSpectators())) {
			if(player.isOnline()) {
				player.getGameProtocol().send(new ContainerRemoveItemWriter(player, internalContainer.getContainer(), slot));
			}
		}
	}
	
}
