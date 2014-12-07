package org.jotserver.ot.model.player;

import java.util.EnumMap;
import java.util.Map.Entry;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.util.ItemLocation;

public class InternalInventory {
	
	private Inventory inventory;
	private EnumMap<InventorySlot, Item> items;
	
	public InternalInventory(Inventory inventory) {
		this.inventory = inventory;
		items = new EnumMap<InventorySlot, Item>(InventorySlot.class);
	}
	
	public void setItem(InventorySlot slot, Item item) {
		if(getItem(slot) != null) {
			getItem(slot).setParent(null);
		}
		items.put(slot, item);
		if(item != null) {
			item.setParent(inventory);
		}
	}
	
	public void removeItem(InventorySlot slot) {
		setItem(slot, null);
	}
	
	public Item getItem(InventorySlot slot) {
		return items.get(slot);
	}
	
	public boolean isEmpty(InventorySlot slot) {
		return getItem(slot) == null;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void removeItem(Item item) {
		InventorySlot slot = getSlot(item);
		if(slot != null) {
			removeItem(slot);
		}
	}

	public boolean hasItem(Item item) {
		return items.containsValue(item);
	}
	
	public InventorySlot getSlot(Item item) {
		for(Entry<InventorySlot, Item> entry : items.entrySet()) {
			if(entry.getValue() != null && entry.getValue().equals(item)) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public ItemLocation getSlotLocation(final InventorySlot slot) {
		return new ItemLocation() {
				public Item get() {
					return getItem(slot);
				}
				public Cylinder getCylinder() {
					return getInventory();
				}
				public int getIndex() {
					return slot.ordinal();
				}
			};
	}
	
}
