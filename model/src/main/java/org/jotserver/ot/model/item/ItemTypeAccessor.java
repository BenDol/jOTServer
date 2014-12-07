package org.jotserver.ot.model.item;

public interface ItemTypeAccessor {
	public ItemType getItemType(int id);
	public void setItemType(int id, ItemType type);
	public ItemType getItemType(String name);
	public Item createItem(ItemType type);
	public Item createItem(int itemId);
}
