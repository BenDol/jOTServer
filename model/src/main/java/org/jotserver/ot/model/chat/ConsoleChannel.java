package org.jotserver.ot.model.chat;

import java.util.StringTokenizer;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.world.LocalGameWorld;

public class ConsoleChannel extends PublicChatChannel {

	private LocalGameWorld world;

	public ConsoleChannel(LocalGameWorld world, int id, String name) {
		super(id, name);
		this.world = world;
	}

	
	public boolean join(Creature creature) {
		return true;
	}

	
	public boolean speak(Creature creature, SpeakType type, String text) {
		return true;
	}

	private void parseCommand(Creature creature, String text) {
		/*StringTokenizer token = new StringTokenizer(text);
		String command = token.nextToken();
		Map map = creature.getTile().getMap();
		if(command.equalsIgnoreCase("teleport") || command.equalsIgnoreCase("tp")) {
			Direction dir = creature.getDirection();
			String string = token.nextToken();
			try {
				dir = Direction.valueOf(string.toUpperCase());
				string = token.nextToken();
			} catch(IllegalArgumentException e) {}
			
			int distance = Integer.parseInt(string);
			
			Position delta = new Position(new Position(), dir);
			delta = delta.scale(distance);
			Position destination = creature.getPosition().add(delta);
			Tile destinationTile = map.getTile(destination);
			if(destinationTile != null) {
				Tile tile = creature.getTile();
				if(tile != null) {
					tile.executeRemoveCreature(creature);
				}
				destinationTile.executeAddCreature(creature);
			}
		} else if(command.equalsIgnoreCase("create")) {
			String name = token.nextToken();
			while(token.hasMoreTokens()) {
				name += " " + token.nextToken();
			}
			ItemType type = world.getItemTypes().getItemType(name);
			if(type != null) {
				Item item = world.getItemTypes().createItem(type);
				creature.getTile().executeAddItem(item);
			}
		} else if(command.equalsIgnoreCase("shutdown")) {
			Runtime.getRuntime().exit(0);
		} else if(command.equalsIgnoreCase("heal")) {
			creature.getCombat().executeHeal(creature.getMaxHealth()-creature.getHealth());
			creature.getTile().executeAddEffect(Effect.MAGIC_ENERGY);
		} else if(command.equalsIgnoreCase("town")) {
			String name = token.nextToken();
			Town t = map.getTownAccessor().getTown(name);
			if(t != null) {
				Tile tile = map.getTile(t.getPosition());
				if(tile != null) {
					Tile tile1 = creature.getTile();
					if(tile1 != null) {
						tile1.executeRemoveCreature(creature);
					}
					tile.executeAddCreature(creature);
				}
			}
		}*/
	}
	
	
	
}
