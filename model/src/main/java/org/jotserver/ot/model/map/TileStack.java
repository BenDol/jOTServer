package org.jotserver.ot.model.map;

import java.util.ArrayList;
import java.util.ListIterator;

import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.item.Item;

public class TileStack {
	private ArrayList<Thing> stack;
	private int topStart;
	private int creatureStart;
	private int downStart;
	
	public TileStack() {
		stack = new ArrayList<Thing>();
		topStart = 0;
		creatureStart = 0;
		downStart = 0;
	}
	
	public void setGround(Item item) {
		if(topStart != 0) {
			stack.set(0, item);
		} else {
			stack.add(0, item);
			topStart++;
			creatureStart++;
			downStart++;
		}
	}
	
	public void addTopItem(Item item) {
		boolean inserted = false;
		ListIterator<Thing> it = stack.listIterator(topStart);
		
		while(it.nextIndex() < creatureStart) {
			Item i = (Item)it.next();
			if(i.getAlwaysOnTopOrder() > item.getAlwaysOnTopOrder()) {
				it.set(item);
				it.add(i);
				inserted = true;
				break;
			}
		}

		if(!inserted) {
			stack.add(creatureStart, item);
		}
		creatureStart++;
		downStart++;
	}
	
	public void addCreature(Creature creature) {
		stack.add(creatureStart, creature);
		downStart++;
	}
	
	public void addDownItem(Item item) {
		stack.add(downStart, item);
	}
	
	public int size() {
		return stack.size();
	}
	
	public int getItemCount() {
		return size() - getCreatureCount();
	}

	public int getCreatureCount() {
		return downStart - creatureStart;
	}
	
	public int getTopItemCount() {
		return creatureStart - topStart;
	}
	
	public int getDownItemCount() {
		return size() - downStart;
	}
	
}
