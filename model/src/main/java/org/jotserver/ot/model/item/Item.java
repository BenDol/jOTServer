package org.jotserver.ot.model.item;

import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.action.Tester;
import org.jotserver.ot.model.action.Visitor;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.player.InventorySlot;
import org.jotserver.ot.model.player.Skills;
import org.jotserver.ot.model.util.Direction;

import static org.jotserver.ot.model.item.ItemAttribute.*;

public class Item extends Thing {
	
	protected final ItemType type;
	
	protected Item(ItemType type) {
		super();
		setInternal(new InternalItem(this, null));
		this.type = type;
	}
	
	public boolean test(Tester action) {
		return action.test(this);
	}
	
	public void execute(Visitor visitor) {
		visitor.execute(this);
	}
	
	protected InternalItem getInternal() {
		return (InternalItem)super.getInternal();
	}

	/*
	 * Action methods
	 */
	
	public ErrorType queryUse(Creature creature) {
		if(creature.getPosition().distanceTo(getPosition()) > 1) {
			return ErrorType.TOOFARAWAY;
		} else {
			return ErrorType.NONE;
		}
	}
	
	public void executeUse(Creature creature) {
	}

	public int getId() {
		return type.getId();
	}
	
	public int getClientId() {
		return type.getClientId();
	}
	
	public String getName() {
		return type.getName();
	}
	
	public boolean isGround() {
		return type.isGroundTile();
	}
	
	public boolean isSplash() {
		return type.isSplash();
	}
	
	public boolean isFluidContainer() {
		return type.isFluidContainer();
	}
	
	public boolean isRune() {
		return type.isRune();
	}
	
	public int getAlwaysOnTopOrder() {
		return type.getAlwaysOnTopOrder();
	}
	
	public Direction getFloorChangeDirection() {
		Direction dir = Direction.NONE;
		if(type.hasAttribute(FLOORCHANGENORTH)) {
			dir = dir.combine(Direction.NORTH);
		} else if(type.hasAttribute(FLOORCHANGESOUTH)) {
			dir = dir.combine(Direction.SOUTH);
		}
		
		if(type.hasAttribute(FLOORCHANGEEAST)) {
			dir = dir.combine(Direction.EAST);
		} else if(type.hasAttribute(FLOORCHANGEWEST)) {
			dir = dir.combine(Direction.WEST);
		}
		
		return dir;
	}
	
	public String getDescription() {
		String ret = "";
		String article = type.getArticle();
		if(article != null) {
			ret += article + " ";
		}
		ret += type.getName();
		if(type.isFluidContainer()) {
			if(getFluidType() == FluidType.NONE) {
				ret += ". It is empty";
			} else {
				ret += " of " + getFluidType().getDescription();
			}
		} else if(type.isSplash()) {
			ret += " of " + getFluidType().getDescription();
		} else if(showWeaponStats()) {
			if(type.getAttackValue() > 0) {
				ret += " (Atk: " + type.getAttackValue() + " Def: " + type.getDefenseValue() + ")";
			} else if(type.getDefenseValue() > 0) {
				ret += " (Def: " + type.getDefenseValue() + ")";
			}
		} else if(type.getArmorValue() > 0) {
			ret += " (Arm: " + type.getArmorValue() + ")";
		}
		return ret;
	}

	private boolean showWeaponStats() {
		return type.getWeaponType() != WeaponType.NONE && type.getWeaponType() != WeaponType.AMMUNITION;
	}
	
	public int getSpeed() {
		return type.getBaseSpeed();
	}
	
	public boolean hasAttribute(ItemAttribute attribute) {
		return type.hasAttribute(attribute);
	}
	
	public boolean fitsOn(InventorySlot slot) {
		return type.getSlots().contains(slot);
	}
	
	public FluidType getFluidType() {
		return getInternal().getFluidType();
	}
	
	public ItemType.Group getGroup() {
		return type.getGroup();
	}
	
	public void setFluidType(FluidType fluidType) {
		getInternal().setFluidType(fluidType);
	}

	public int getAttack() {
		return type.getAttackValue();
	}

	public int getDefense() {
		return type.getDefenseValue();
	}

	public Skills.Type getWeaponSkill() {
		switch(type.getWeaponType()) {
		case CLUB:
			return Skills.Type.CLUB;
		case AXE:
			return Skills.Type.AXE;
		case SWORD:
			return Skills.Type.SWORD;
		case DISTANCE:
			return Skills.Type.DISTANCE;
		default:
			return null;
		}
	}

	public WeaponType getWeaponType() {
		return type.getWeaponType();
	}

	public int getArmor() {
		return type.getArmorValue();
	}

	public ItemType getType() {
		return type;
	}

}
