package org.jotserver.ot.model.item;

import static org.jotserver.ot.model.item.ItemAttribute.STACKABLE;


public class BaseItemTypeAccessor implements ItemTypeAccessor {

    protected ItemType[] types;

    public BaseItemTypeAccessor(int size) {
        types = new ItemType[size];
    }

    public ItemType getItemType(int id) {
        if(isValid(id)) {
            return null;
        } else {
            return types[id];
        }
    }

    public ItemType getItemType(String name) {
        for(ItemType type : types) {
            if(type != null && type.getName() != null && type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public void setItemType(int id, ItemType type) {
        if(isValid(id)) {
            throw new IllegalArgumentException("Id outside valid range for this accessor.");
        } else {
            types[id] = type;
        }
    }

    private boolean isValid(int id) {
        return id >= types.length || id < 0;
    }

    public Item createItem(ItemType type) {
        if(type.isContainer()) {
            return new Container(type);
        } else if(type.hasAttribute(STACKABLE)) {
            return new Stackable(type, 1);
        } else {
            return new Item(type);
        }
    }

    public Item createItem(int itemId) {
        ItemType itemType = getItemType(itemId);
        if(itemType == null) {
            return null;
        } else {
            return createItem(itemType);
        }
    }

}