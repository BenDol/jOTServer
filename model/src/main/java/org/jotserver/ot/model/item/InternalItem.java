package org.jotserver.ot.model.item;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.InternalThing;

public class InternalItem extends InternalThing {
	
	private final Item item;
	private FluidType fluidType;
	
	public InternalItem(Item item, Cylinder parent) {
		super(item, parent);
		this.item = item;
		fluidType = FluidType.NONE;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setFluidType(FluidType type) {
		fluidType = type;
	}
	
	public FluidType getFluidType() {
		return fluidType;
	}
	
}
