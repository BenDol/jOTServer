package org.jotserver.ot.net.game.handler;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.map.InternalTile;
import org.jotserver.ot.model.map.Spectators;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.model.AnimatedTextColor;
import org.jotserver.ot.model.Effect;
import org.jotserver.ot.net.Writer;
import org.jotserver.ot.net.game.creature.CreatureMoveWriter;
import org.jotserver.ot.net.game.creature.TileAddCreatureWriter;
import org.jotserver.ot.net.game.out.FullMapDescriptionWriter;
import org.jotserver.ot.net.game.out.MoveSelfWriter;
import org.jotserver.ot.net.game.out.TileAddItemWriter;
import org.jotserver.ot.net.game.out.TileAnimatedTextWriter;
import org.jotserver.ot.net.game.out.TileRemoveThingWriter;
import org.jotserver.ot.net.game.out.TileEffectWriter;
import org.jotserver.ot.model.SimpleSpectators;

public aspect TileHandler {
	
	/*
	 * Creature-related pointcuts.
	 */
	public pointcut tileAddCreature(InternalTile tile, Creature creature) : 
		target(tile) &&
		args(creature) &&
		execution(public void InternalTile.addCreature(Creature));
	
	public pointcut tileRemoveCreature(InternalTile tile, Creature creature) :
		target(tile) &&
		args(creature) &&
		execution(public void InternalTile.removeCreature(Creature));
	
	private pointcut tileMoveCreature(InternalTile fromTile, Creature creature) : 
		target(fromTile) && 
		args(creature, ..) &&
		execution(public void InternalTile.moveCreature(Creature, ..));
	
	
	/*
	 * Item-related pointcuts.
	 */
	public pointcut tileAddItem(InternalTile tile, Item item) :
		target(tile) &&
		args(item) &&
		execution(public void InternalTile.addItem(Item));
	
	public pointcut tileRemoveItem(InternalTile tile, Item item) : 
		target(tile) &&
		args(item) &&
		execution(public void InternalTile.removeItem(Item));
	
	/*
	 * Other pointcuts.
	 */
	public pointcut tileAddEffect(InternalTile tile, Effect effect) : 
		target(tile) &&
		args(effect) &&
		execution(public void InternalTile.addEffect(Effect));
	
	public pointcut tileAddText(InternalTile tile, AnimatedTextColor color, String text) : 
		target(tile) &&
		args(color, text) &&
		execution(public void InternalTile.addText(AnimatedTextColor, String));
	
	
	/*
	 * InternalTile.addCreature
	 */
	after(InternalTile tile, Creature creature) returning : tileAddCreature(tile, creature) {
		if(tile.isPlaced() && creature.isPlaced()) {
			for(Player player : creature.getSpectators(Player.class)) {
				if(player.isOnline() && player.canSee(creature.getPosition())) {
					if(player == creature) {
						player.getGameProtocol().send(
								new FullMapDescriptionWriter(player, player.getPosition(), tile.getTile().getMap()));
					} else {
						player.getGameProtocol().send(new TileAddCreatureWriter(player, creature.getPosition(), creature));
					}
				}
			}
		}
	}
	
	/*
	 * InternalTile.removeCreature
	 */
	void around(InternalTile tile, Creature creature) : tileRemoveCreature(tile, creature) {
		int stack = tile.getIndexOf(creature);
		Position pos = tile.getTile().getPosition();
		Spectators<Player> spectators = SimpleSpectators.getEmpty(Player.class);
		if(tile.isPlaced() && creature.isPlaced()) {
			spectators = tile.getTile().getContentsSpectators(Player.class);
		}
		proceed(tile, creature);
		for(Player player : spectators) {
			if(player.isOnline()/* && player.canSee(pos)*/) {
				player.getGameProtocol().send(new TileRemoveThingWriter(player, pos, stack));
			}
		}
	}
	
	/*
	 * InternalTile.moveCreature
	 */
	void around(InternalTile fromTile, Creature creature) : tileMoveCreature(fromTile, creature) {
		int stack = fromTile.getIndexOf(creature);
		proceed(fromTile, creature);
		
		if(creature.isPlaced() && fromTile.isPlaced()) {
			Position from = fromTile.getTile().getPosition();
			Position to = creature.getPosition();
			for(Player player : fromTile.getTile().getContentsSpectators(Player.class)) {
				if(player.isOnline()) {
					Writer writer = null;
					if(player == creature) { // Self
						writer = new MoveSelfWriter(player, fromTile.getTile().getMap(), 
										fromTile.getTile().getPosition(), 
										stack, 
										player.getPosition());
					} else if(player.canSee(from) && player.canSee(to)) { // Move
						writer = new CreatureMoveWriter(player, from, stack, creature);
					} else if(player.canSee(from)) { // Remove
						writer = new TileRemoveThingWriter(player, from, stack);
					} else if(player.canSee(to)) { // Add
						writer = new TileAddCreatureWriter(player, to, creature);
					}
					if(writer != null) {
						player.getGameProtocol().send(writer);
					}
				}
			}
		}
	}
	
	/*
	 * InternalTile.addItem
	 */
	after(InternalTile tile, Item item) returning : tileAddItem(tile, item) {
		if(item.isPlaced()) {
			Position pos = item.getPosition();
			for(Player player : item.getSpectators(Player.class)) {
				if(player.canSee(pos) && player.isOnline()) {
					player.getGameProtocol().send(new TileAddItemWriter(player, tile.getTile(), item));
				}
			}
		}
	}
	
	/*
	 * InternalTile.removeItem
	 */
	void around(InternalTile internalTile, Item item) : tileRemoveItem(internalTile, item) {
		int stack = internalTile.getIndexOf(item);
		proceed(internalTile, item);
		Tile tile = internalTile.getTile();
		for(Player player : tile.getContentsSpectators(Player.class)) {
			if(player.isOnline() && player.canSee(tile.getPosition())) {
				player.getGameProtocol().send(new TileRemoveThingWriter(player, tile, stack));
			}
		}
	}
	
	/*
	 * InternalTile.addEffect
	 */
	after(InternalTile internalTile, Effect effect) : tileAddEffect(internalTile, effect) {
		Tile tile = internalTile.getTile();
		for(Player player : tile.getContentsSpectators(Player.class)) {
			if(player.isOnline() && player.canSee(tile.getPosition())) {
				player.getGameProtocol().send(new TileEffectWriter(player, tile, effect));
			}
		}
	}
	
	/*
	 * InternalTile.addText
	 */
	after(InternalTile internalTile, AnimatedTextColor color, String text) : tileAddText(internalTile, color, text) {
		Tile tile = internalTile.getTile();
		for(Player player : tile.getContentsSpectators(Player.class)) {
			if(player.isOnline() && player.canSee(tile.getPosition())) {
				player.getGameProtocol().send(new TileAnimatedTextWriter(player, tile, color, text));
			}
		}
	}
	
	
}
