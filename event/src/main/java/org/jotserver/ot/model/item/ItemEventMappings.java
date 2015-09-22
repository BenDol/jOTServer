package org.jotserver.ot.model.item;

import java.util.LinkedList;

public class ItemEventMappings {

    private int itemid;
    private LinkedList<ItemUseHandler> itemUseHandlers;

    public ItemEventMappings(int itemid) {
        this.itemid = itemid;
        itemUseHandlers = new LinkedList<ItemUseHandler>();
    }

    public void addItemUseHandler(ItemUseHandler handler) {
        itemUseHandlers.add(handler);
    }

    public void executeItemUse(ItemUseEvent event) {
        for(ItemUseHandler handler : itemUseHandlers) {
            handler.onUse(event);
        }
    }
}
