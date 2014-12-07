package org.jotserver.ot.model.item;

import org.jotserver.ot.model.Light;
import org.jotserver.ot.model.player.InventorySlot;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class ItemType {

	public static enum Group {
		NONE, GROUND, CONTAINER, WEAPON, AMMUNITION, ARMOR, RUNE, TELEPORT, 
		MAGICFIELD, WRITEABLE, KEY, SPLASH, FLUID, DOOR, DEPRECATED, LAST
	}

	protected Group group;
	protected WeaponType weaponType;
	
	protected EnumSet<InventorySlot> slots;
	protected EnumSet<ItemAttribute> attributes;
	
	protected int serverId;
	protected int clientId;
	
	protected String article;
	protected String name;
	protected String pluralName;
	
	protected int lightColor;
	protected int lightLevel;
	
	protected int containerSize;
	
	protected int alwaysOnTopOrder;
	
	protected int attack;
	protected int defense;
	protected int armor;
	protected int baseSpeed;
	
	public ItemType() {
		group = Group.NONE;
		weaponType = WeaponType.NONE;
		slots = EnumSet.of(InventorySlot.LEFT, InventorySlot.RIGHT, InventorySlot.AMMO);
		attributes = EnumSet.noneOf(ItemAttribute.class);
	}

	public ItemType(Group group) {
		this();
		this.group = group;
	}
	
	protected void addSlot(InventorySlot slot) {
		slots.add(slot);
	}

	public Light getLight() {
		return new Light(lightLevel, lightColor);
	}
	
	public Set<InventorySlot> getSlots() {
		return Collections.unmodifiableSet(slots);
	}
	
	public boolean hasAttribute(ItemAttribute attribute) {
		return attributes.contains(attribute);
	}
	
	protected void addAttribute(ItemAttribute attribute) {
		attributes.add(attribute);
	}
	
	protected void removeAttribute(ItemAttribute attribute) {
		attributes.remove(attribute);
	}
	
	protected void setAttribute(ItemAttribute attribute, boolean value) {
		if(value) {
			addAttribute(attribute);
		} else {
			removeAttribute(attribute);
		}
	}

	public int getId() {
		return serverId;
	}
	public int getClientId() {
		return clientId;
	}
	
	public String getName() {
		return name;
	}
	public String getPluralName() {
		return pluralName;
	}
	public String getArticle() {
		return article;
	}

	public Group getGroup() {
		return group;
	}
	protected void setGroup(Group group) {
		this.group = group;
	}

	public boolean isGroundTile() {
		return (group == Group.GROUND);
	}

	public boolean isContainer() {
		return (group == Group.CONTAINER);
	}

	public boolean isDoor() {
		return (group == Group.DOOR);
	}

	public boolean isTeleport() {
		return (group == Group.TELEPORT);
	}

	public boolean isMagicField() {
		return (group == Group.MAGICFIELD);
	}

	public boolean isSplash() {
		return (group == Group.SPLASH);
	}

	public boolean isFluidContainer() {
		return (group == Group.FLUID);
	}

	public boolean isKey() {
		return (group == Group.KEY);
	}

	public boolean isRune() {
		return (group == Group.RUNE);
	}

	public int getContainerSize() {
		return containerSize;
	}

	public int getAlwaysOnTopOrder() {
		return alwaysOnTopOrder;
	}

	public int getBaseSpeed() {
		return baseSpeed;
	}

	public int getAttackValue() {
		return attack;
	}

	public int getDefenseValue() {
		return defense;
	}
	
	public int getArmorValue() {
		return armor;
	}
	
	public WeaponType getWeaponType() {
		return weaponType;
	}
	
	/*
	 * boolean isDepot() {return (type == ITEM_TYPE_DEPOT);} boolean isMailbox()
	 * {return (type == ITEM_TYPE_MAILBOX);} boolean isTrashHolder() {return
	 * (type == ITEM_TYPE_TRASHHOLDER);} boolean hasSubType() {return
	 * (isFluidContainer() || isSplash() || stackable || charges != 0);}
	 */
}
