package org.jotserver.ot.net.game.handler;

import org.jotserver.ot.model.Outfit;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.creature.InternalCreature;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.creature.CreatureChangeOutfitWriter;
import org.jotserver.ot.net.game.creature.CreatureTurnWriter;
import org.jotserver.ot.net.game.creature.CreatureHealthWriter;
import org.jotserver.ot.net.game.creature.PlayerStatsWriter;

public aspect CreatureHandler {
	
	private pointcut creatureTurn(InternalCreature creature) : 
		target(creature) &&
		execution(public void InternalCreature.turn(Direction));
	
	private pointcut creatureChangeOutfit(InternalCreature creature) :
		target(creature) &&
		execution(public void InternalCreature.setOutfit(Outfit));
	
	private pointcut creatureChangeHealth(InternalCreature creature) : 
		target(creature) &&
		(execution(public void InternalCreature.setHealth(int)) ||
				execution(public void InternalCreature.setMaxHealth(int)));
	
	/*
	 * InternalCreature.turn
	 */
	after(InternalCreature internalCreature) returning : creatureTurn(internalCreature) {
		Creature creature = internalCreature.getCreature();
		if(creature.isPlaced()) {
			for(Player player : creature.getSpectators(Player.class)) {
				if(player.isOnline() && player.canSee(creature.getPosition())) {
					player.getGameProtocol().send(new CreatureTurnWriter(player, creature));
				}
			}
		}
	}
	
	/*
	 * InternalCreature.setOutfit
	 */
	after(InternalCreature internalCreature) returning : creatureChangeOutfit(internalCreature) {
		Creature creature = internalCreature.getCreature();
		if(creature.isPlaced()) {
			for(Player player : creature.getSpectators(Player.class)) {
				if(player.isOnline() && player.canSee(creature.getPosition())) {
					player.getGameProtocol().send(new CreatureChangeOutfitWriter(player, creature));
				}
			}
		}
	}
	
	/*
	 * InternalCreature.setHealth
	 * InternalCreature.setMaxHealth
	 */
	after(InternalCreature internalCreature) : creatureChangeHealth(internalCreature) {
		Creature creature = internalCreature.getCreature();
		if(creature.isPlaced()) {
			for(Player player : creature.getSpectators(Player.class)) {
				if(player.isOnline() && player.canSee(creature.getPosition())) {
					if(player.equals(creature)) {
						player.getGameProtocol().send(new PlayerStatsWriter(player));
					}
					player.getGameProtocol().send(new CreatureHealthWriter(player, creature));
				}
			}
		}
	}
}
