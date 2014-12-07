package org.jotserver.ot.model.map;

import org.jotserver.ot.model.AnimatedTextColor;
import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.Effect;
import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.action.Tester;
import org.jotserver.ot.model.action.Visitor;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.ItemAttribute;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Location;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.model.world.LocalGameWorld;
import org.jotserver.ot.model.world.WorldState;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Tile implements Cylinder {

	InternalTile internal;
	
	public Tile(Map map, Position position) {
		internal = new InternalTile(this, map, position);
	}

	public boolean test(Tester action) {
		return action.test(this);
	}

	public void execute(Visitor visitor) {
		visitor.execute(this);
	}
	
	protected InternalTile getInternal() {
		return internal;
	}
	
	public Position getPosition() {
		return internal.position;
	}
	
	public boolean isVisibleTo(Creature creature) {
		return creature.canSee(getPosition());
	}
	
	public Map getMap() {
		return internal.map;
	}
	
	public int getIndexOf(Thing thing) {
		return internal.getIndexOf(thing);
	}
	
	public Thing getThing(int index) {
		if(index < 0) {
			return null;
		}
		if(getGround() != null) {
			if(index == 0) {
				return getGround();
			} else {
				index--;
			}
		}
		
		List<Iterable<? extends Thing>> things = new LinkedList<Iterable<? extends Thing>>();
		things.add(getTopItems());
		things.add(getCreatures());
		things.add(getDownItems());
		
		for(Iterable<? extends Thing> t : things) {
			for(Thing thing : t) {
				if(index <= 0) {
					return thing;
				}
				index--;
			}
		}
		return null;
	}
	
	/*
	 * Creature action methods
	 */
	
	public ErrorType queryAddCreature(Creature creature) {
		return queryAddCreature(creature, false);
	}
	
	public ErrorType queryAddCreature(Creature creature, boolean pathFind) {
		ErrorType ret = ErrorType.NONE;
		Tile tile = getDestination();
		if((pathFind && tile != this) || !getCreatures().isEmpty()) {
			// TODO: Should BLOCKPATHFIND checking be added here as well?
			ret = ErrorType.NOTPOSSIBLE;
		} else if(tile.getGround() == null) {
			ret = ErrorType.NOTPOSSIBLE;
		} else if(tile.hasAttribute(ItemAttribute.BLOCKSOLID)) {
			ret = ErrorType.NOTENOUGHROOM;
		}
		return ret;
	}
	
	public void executeAddCreature(Creature creature) {
		Tile tile = getDestination();
		tile.getInternal().addCreature(creature);
	}
	
	public ErrorType queryMoveCreature(Creature creature, Direction direction) {
		Tile destination = getTile(direction);
		if(destination == null) {
			return ErrorType.NOTPOSSIBLE;
		} else {
			return queryMoveCreature(creature, destination);
		}
	}
	
	public ErrorType queryMoveCreature(Creature creature, Tile destination) {
		ErrorType ret = ErrorType.NONE;
		if(getPosition().distanceTo(destination.getPosition()) > 1 ||
				getPosition().getZDistanceTo(destination.getPosition()) > 1) {
			ret = ErrorType.NOTPOSSIBLE;
		}
		
		if(ret == ErrorType.NONE) {
			ret = queryRemoveCreature(creature);
		}
		if(ret == ErrorType.NONE) {
			ret = destination.queryAddCreature(creature);
		}
		return ret;
	}
	
	public void executeMoveCreature(Creature creature, Direction direction) {
		Tile destination = getTile(direction);
		executeMoveCreature(creature, destination);
	}
	
	public void executeMoveCreature(Creature creature, Tile destination) {
		internal.moveCreature(creature, destination);
		Tile newDestination = destination.getDestination();
		if(destination != newDestination) {
			destination.internal.moveCreature(creature, newDestination);
		}
		Direction direction = getPosition().directionTo(destination.getPosition());
		direction = direction.normalize();
		if(direction != Direction.NONE && creature.getDirection() != direction) {
			creature.turn(direction);
		}
	}
	
	public ErrorType queryRemoveCreature(Creature creature) {
		if(!getCreatures().contains(creature)) {
			return ErrorType.NOTPOSSIBLE;
		}
		return ErrorType.NONE;
	}
	
	public void executeRemoveCreature(Creature creature) {
		internal.removeCreature(creature);
	}
	
	/*
	 * Item action methods
	 */
	
	public ErrorType queryAddItem(Item item) {
		Tile tile = getDestination();
		
		if(!tile.getCreatures().isEmpty() && item.hasAttribute(ItemAttribute.BLOCKSOLID)) {
			return ErrorType.NOTPOSSIBLE;
		}
		for(Item i : tile.getItems()) {
			if(i.hasAttribute(ItemAttribute.BLOCKSOLID)) {
				if(item.hasAttribute(ItemAttribute.PICKUPABLE)) {
					if(!i.hasAttribute(ItemAttribute.HASHEIGHT) || i.hasAttribute(ItemAttribute.PICKUPABLE)) {
						return ErrorType.NOTPOSSIBLE;
					}
				} else {
					return ErrorType.NOTPOSSIBLE;
				}
			}
		}
		return ErrorType.NONE;
	}
	
	public void executeAddItem(Item item) {
		Tile tile = getDestination();
		if(item.isSplash()) {
			for(Item oldItem : getItems()) {
				if(oldItem.isSplash()) {
					executeRemoveItem(oldItem);
					break;
				}
			}
		}
		tile.getInternal().addItem(item);
	}
	
	public ErrorType queryRemoveItem(Item item) {
		if(!getItems().contains(item)) {
			return ErrorType.NOTPOSSIBLE;
		} else {
			return ErrorType.NONE;
		}
	}
	
	public void executeRemoveItem(Item item) {
		getInternal().removeItem(item);
	}
	
	/*
	 * Other action methods.
	 */
	
	public void executeAddEffect(Effect effect) {
		getInternal().addEffect(effect);
	}
	
	
	/*
	 * Item provider methods.
	 */

	public Item getGround() {
		return internal.getGround();
	}
	
	public Item getTopItem() {
		List<Item> items = getDownItems();
		if(!items.isEmpty()) {
			return items.get(0);
		}
		items = getTopItems();
		if(!items.isEmpty()) {
			return items.get(items.size()-1);
		}
		return getGround();
	}

	public List<Item> getDownItems() {
		return internal.getDownItems();
	}
	public List<Item> getTopItems() {
		return internal.getTopItems();
	}
	
	public Collection<Item> getItems() {
		List<Item> ret = new LinkedList<Item>(getTopItems());
		ret.addAll(getDownItems());
		if(getGround() != null) {
			ret.add(getGround());
		}
		return ret;
	}

	/*
	 * Creature provider methods.
	 */
	
	public List<Creature> getCreatures() {
		return internal.getCreatures();
	}
	
	/*
	 * Creature action methods.
	 */
	
	public Tile getDestination() {
		Tile destination = this;
		boolean down = false;
		if(hasAttribute(ItemAttribute.FLOORCHANGEDOWN)) {
			destination = internal.getTile(Direction.DOWN);
			down = true;
		}
		if(destination != null) {
			Direction dir = destination.getFloorChangeDirection();
			if(dir != Direction.NONE) {
				if(down) {
					dir = dir.invert();
					destination = destination.internal.getTile(dir);
				} else {
					destination = destination.internal.getTile(Direction.UP);
					if(destination != null) {
						destination = destination.internal.getTile(dir);
					}
				}
			}
		}
		return destination == null ? this : destination;
	}

	public Cylinder getParent() {
		return null;
	}

	public Tile getTile() {
		return this;
	}
	
	public Tile getTile(Direction direction) {
		return internal.getTile(direction);
	}

	public boolean isPlaced() {
		return internal.isPlaced() && getGameWorld().getState() == WorldState.RUNNING;
	}

	public LocalGameWorld getGameWorld() {
		return internal.map.getGameWorld();
	}
	
	public boolean hasAttribute(ItemAttribute attribute) {
		Collection<Item> items = getItems();
		for(Item item : items) {
			if(item.hasAttribute(attribute)) return true;
		}
		return false;
	}
	
	public Direction getFloorChangeDirection() {
		Collection<Item> items = getItems();
		for(Item item : items) {
			Direction dir = item.getFloorChangeDirection();
			if(dir != Direction.NONE) {
				return dir;
			}
		}
		return Direction.NONE;
	}
	
	public boolean doesFloorChange() {
		return getFloorChangeDirection() != Direction.NONE;
	}
	
	public <T extends Creature> Spectators<T> getContentsSpectators(Class<T> type) {
		return internal.map.getSpectators(type, getPosition());
	}
	
	public Location getLocationOf(Thing thing) {
		int index = this.getIndexOf(thing);
		if(index != -1) {
			return new TileLocation(this, index);
		} else {
			return null;
		}
	}
	
	public Location getLocationOf(int stackPos) {
		return new TileLocation(this, stackPos);
	}

	public void executeAddText(AnimatedTextColor color, String text) {
		getInternal().addText(color, text);
	}
	
}
