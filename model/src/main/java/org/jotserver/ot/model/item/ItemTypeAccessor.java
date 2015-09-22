package org.jotserver.ot.model.item;

public interface ItemTypeAccessor {
    ItemType getItemType(int id);
    void setItemType(int id, ItemType type);
    ItemType getItemType(String name);
    Item createItem(ItemType type);
    Item createItem(int itemId);
}
