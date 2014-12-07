package org.jotserver.ot.model.item;

import java.util.HashMap;

public class ItemEventEngine {
	
	private HashMap<Integer, ItemEventMappings> mappings;
	
	public ItemEventEngine() {
		mappings = new HashMap<Integer, ItemEventMappings>();
	}
	
	private ItemEventMappings getMappings(int itemid) {
		ItemEventMappings ret = mappings.get(itemid);
		if(ret == null) {
			ret = new ItemEventMappings(itemid);
			mappings.put(itemid, ret);
		}
		return ret;
	}
	
	public void registerItemUseHandler(ItemUseHandler handler, int[] itemids) {
		for(int itemid : itemids) {
			getMappings(itemid).addItemUseHandler(handler);
		}
	}
	
	public void executeItemUse(ItemUseEvent event) {
		int itemid = event.getItem().getId();
		getMappings(itemid).executeItemUse(event);
	}
}
