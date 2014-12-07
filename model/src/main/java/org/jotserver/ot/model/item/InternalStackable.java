package org.jotserver.ot.model.item;

import org.jotserver.ot.model.Cylinder;

public class InternalStackable extends InternalItem {
	
	private Stackable stackable;
	private int count;
	
	public InternalStackable(Stackable stackable, int count, Cylinder parent) {
		super(stackable, parent);
		this.stackable = stackable;
		this.count = count;
	}
	
	public Stackable getStackable() {
		return stackable;
	}
	
	public void setCount(int count) {
		if(count <= 0 || count > 100) throw new IllegalArgumentException("Illegal count!");
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}

}
