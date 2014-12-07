package org.jotserver.ot.model.item;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.SimpleSpectators;
import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.action.Tester;
import org.jotserver.ot.model.action.Visitor;
import org.jotserver.ot.model.action.item.AddItemTargetAction;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Spectators;
import org.jotserver.ot.model.util.ItemLocation;

import java.util.List;

public class Container extends Item implements Cylinder {

	protected Container(ItemType type) {
		super(type);
		setInternal(new InternalContainer(this, null));
		if(!type.isContainer()) {
			throw new IllegalArgumentException("Attempt to make a non-container container.");
		}
	}

	public <T extends Creature> Spectators<T> getContentsSpectators(
			Class<T> type) {
		return new SimpleSpectators<T>(type, getInternal().getSpectators());
	}

	public void execute(Visitor visitor) {
		visitor.execute(this);
	}

	public boolean test(Tester action) {
		return action.test(this);
	}

	protected InternalContainer getInternal() {
		return (InternalContainer)super.getInternal();
	}
	
	/*
	 * Action methods
	 */
	
	public void executeUse(Creature creature) {
		if(getInternal().isSpectator(creature)) {
			getInternal().close(creature);
		} else {
			getInternal().open(creature);
		}
	}
	
	public ErrorType queryAddItem(Item item) {
		if(isImpossibleToAdd(item)) {
			return ErrorType.IMPOSSIBLE;
		} else if(isFull()) {
			return ErrorType.NOTENOUGHROOM;
		} else if(!item.hasAttribute(ItemAttribute.PICKUPABLE)) {
			return ErrorType.CANNOTPICKUP;
		} else {
			return ErrorType.NONE;
		}
	}
	
	public int getFreeCapacity() {
		return getCapacity() - getItemCount();
	}

	public boolean isFull() {
		return getCapacity() <= getItemCount();
	}
	
	public boolean isImpossibleToAdd(Item item) {
		return item instanceof Cylinder && (equals(item) || isParent((Cylinder)item));
	}
	
	public void executeAddItem(Item item) {
		getInternal().addItem(item);
	}
	
	public ErrorType queryAddItem(Item item, int slot) {
		Item toItem = getItem(slot);
		if(toItem != null) {
			if(toItem.test(new AddItemTargetAction(item))) {
				return ErrorType.NONE;
			}
		}
		return queryAddItem(item);
	}
	
	public void executeAddItem(Item item, int slot) {
		Item toItem = getItem(slot);
		AddItemTargetAction action = new AddItemTargetAction(item);
		if(toItem != null && toItem.test(action)) {
			toItem.execute(action);
		} else {
			executeAddItem(item);
		}
	}
	
	public ErrorType queryRemoveItem(Item item) {
		if(hasItem(item)) {
			return ErrorType.NONE;
		} else {
			return ErrorType.NOTPOSSIBLE;
		}
	}
	
	public void executeRemoveItem(Item item) {
		getInternal().removeItem(getInternal().getSlot(item));
	}
	
	public boolean canBeViewedBy(Creature creature) {
		return isVisibleTo(creature) && creature.canReach(this);
	}
	
	public int getCapacity() {
		return type.getContainerSize();
	}

	public Item getItem(int slot) {
		return getInternal().getItem(slot);
	}
	
	public int getSlot(Item item) {
		return getInternal().getSlot(item);
	}

	public int getItemCount() {
		return getInternal().getItemCount();
	}

	public boolean hasItem(Item item) {
		return getSlot(item) != -1;
	}

	public ItemLocation getLocationOf(Thing thing) {
		if(thing instanceof Item) {
			return getInternal().getLocationOf((Item)thing);
		} else {
			return null;
		}
	}

	public void onChangeParent() {
		super.onChangeParent();
		for(Item item : getInternal().getItems()) {
			item.onChangeParent();
		}
	}

	public void close(Creature creature) {
		getInternal().close(creature);
	}

	public ItemLocation getSlotLocation(int slot) {
		return getInternal().getSlotLocation(slot);
	}

	public List<Item> getItems() {
		return getInternal().getItems();
	}

	public boolean isContentsSpectator(Creature creature) {
		return getInternal().isSpectator(creature);
	}

	public String getDescription() {
		return super.getDescription() + " (Vol: " + getCapacity() + ")";
	}
	
}

