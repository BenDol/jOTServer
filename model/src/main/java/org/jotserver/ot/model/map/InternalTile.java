/**
 * 
 */
package org.jotserver.ot.model.map;

import org.jotserver.ot.model.AnimatedTextColor;
import org.jotserver.ot.model.Effect;
import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.ItemAttribute;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Position;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class InternalTile {
	private final Tile tile;
	
	public final Map map;
	public final Position position;
	
	private Item ground;
	private LinkedList<Item> downItems;
	private LinkedList<Item> topItems;
	private LinkedList<Creature> creatures;
	
	public InternalTile(Tile tile, Map map, Position position) {
		ground = null;
		downItems = new LinkedList<Item>();
		topItems = new LinkedList<Item>();
		creatures = new LinkedList<Creature>();
		
		this.map = map;
		this.position = position;
		this.tile = tile;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public Tile getTile(Direction direction) {
		return map.getTile(new Position(position, direction));
	}
	
	public List<Item> getDownItems() {
		return Collections.unmodifiableList(downItems);
	}
	public List<Item> getTopItems() {
		return Collections.unmodifiableList(topItems);
	}
	@SuppressWarnings("unchecked")
	public List<Creature> getCreatures() {
		return (List<Creature>)creatures.clone();
	}
	
	public Item getGround() {
		return ground;
	}
	
	// TODO: Doesn't seem to be working identical to real Tibia when on top of door items.
	public int getIndexOf(Thing thing) {
		int ret = -1;
		if(thing == null) {
			return ret;
		} else if(ground != null) {
			ret++;
			if(thing.equals(ground)) {
				return ret;
			}
		}
		for(Item item : getTopItems()) {
			ret++;
			if(thing.equals(item)) {
				return ret;
			}
		}
		for(Creature creature : creatures) {
			ret++;
			if(creature.equals(thing)) {
				return ret;
			}
		}
		for(Item item : getDownItems()) {
			ret++;
			if(thing.equals(item)) {
				return ret;
			}
		}
		
		return -1;
	}
	
	public boolean isPlaced() {
		return map != null && position != null;
	}
	
	public void addEffect(Effect effect) {
		//
	}
	
	public void addText(AnimatedTextColor color, String text) {
		//
	}
	
	/*
	 * Action methods
	 */
	
	public void moveCreature(Creature creature, Direction direction) {
		if(creature == null) {
			throw new IllegalArgumentException("Can not move null creature.");
		} else if(creatures.remove(creature)) {
			Tile toTile = getTile(direction);
			if(toTile == null) {
				throw new IllegalArgumentException("Destination tile does not exist.");
			} else {
				toTile.getInternal().internalAddCreature(creature);
			}
		} else {
			throw new IllegalArgumentException("Creature not found!");
		}
	}
	
	public void moveCreature(Creature creature, Tile destination) {
		if(creature == null) {
			throw new IllegalArgumentException("Can not move null creature.");
		} else if(destination == null) {
			throw new IllegalArgumentException("Can not move creature to null.");
		} else if(creatures.remove(creature)) {
			destination.getInternal().internalAddCreature(creature);
		} else {
			throw new IllegalArgumentException("Creature not found.");
		}
	}
	
	public void removeCreature(Creature creature) {
		if(creature == null) {
			throw new IllegalArgumentException("Can not remove null creature.");
		} else if(!creatures.remove(creature)) {
			throw new IllegalArgumentException("Creature not found!");
		} else {
			creature.setParent(null);
		}
	}
	
	public void addCreature(Creature creature) {
		if(creature == null) {
			throw new IllegalArgumentException("Can not add null creature");
		} else if(creatures.contains(creature)) {
			throw new IllegalArgumentException("Creature already on tile!");
		} else if(creature.getParent() != null) {
			throw new IllegalArgumentException("Creature already placed on another tile-");
		} else {
			internalAddCreature(creature);
		}
	}
	
	public void addItem(Item item) {
		if(item == null) {
			throw new IllegalArgumentException("Cannot add null item.");
		} else if(item.isGround()) {
			if(ground == null) {
				ground = item;
			} else {
				throw new IllegalArgumentException("Tile already has a ground item.");
			}
		} else if(item.hasAttribute(ItemAttribute.ALWAYSONTOP)) {
			boolean inserted = false;
			ListIterator<Item> it = topItems.listIterator();
			while(it.hasNext()){
				Item i = it.next();
				if(i.getAlwaysOnTopOrder() > item.getAlwaysOnTopOrder()) {
					it.set(item);
					it.add(i);
					inserted = true;
					break;
				}
			}

			if(!inserted) {
				topItems.addLast(item);
			}
		} else {
			downItems.addFirst(item);
		}
		item.setParent(getTile());
	}
	
	public void removeItem(Item item) {
		if(item == null) {
			throw new IllegalArgumentException("Cannot remove null item.");
		} else if(ground != null && ground.equals(item)) {
			ground = null;
		} else if(!downItems.remove(item) && !topItems.remove(item)) {
			throw new IllegalArgumentException("Item not found.");
		}
		item.setParent(null);
	}
	
	/*
	 * Internal helper methods
	 */
	private void internalAddCreature(Creature creature) {
		creatures.addFirst(creature);
		creature.setParent(getTile());
	}
}