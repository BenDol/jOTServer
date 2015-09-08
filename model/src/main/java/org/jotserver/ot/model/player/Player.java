package org.jotserver.ot.model.player;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.Outfit;
import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.action.Tester;
import org.jotserver.ot.model.action.Visitor;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.map.Town;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.model.world.GameWorld;

public class Player extends Creature {
	
	private static long nextId = 0x10000000+1;
	
	private GameWorld world;
	private int stamina;
	private CreatureCache knownCreatures;
	
	private int globalId;
	private Town town;
	
	private final Inventory inventory;
	
	private Stance stance;

	public Player(int globalId, String name, GameWorld world) {
		super();
		setInternal(new InternalPlayer(this, nextId++, name, null));
		
		this.globalId = globalId;
		this.town = new Town();
		
		this.world = world;
		stamina = 0;
		
		knownCreatures = new CreatureCache(this, 150);
		
		inventory = new Inventory(this);
		
		stance = Stance.BALANCED;
	}
	
	public boolean test(Tester action) {
		return action.test(this);
	}
	
	public void execute(Visitor visitor) {
		visitor.execute(this);
	}
	
	protected InternalPlayer getInternal() {
		return (InternalPlayer)super.getInternal();
	}
	
	public int getGlobalId() {
		return globalId;
	}
	
	public void setGlobalId(int globalId) {
		this.globalId = globalId;
	}
	
	public CreatureCache getKnownCreaturesCache() {
		return knownCreatures;
	}
	
	public GameWorld getWorld() {
		return world;
	}

	public boolean canReportBugs() {
		return false;
	}

	/*public int getSpeed() {
		return 220 + (2* (level - 1));
	}*/

	public void onChangeParent() {
		Tile tile = getTile();
		if(tile != null) {
			Position newPos = tile.getPosition();
			setTemporaryPosition(newPos);
		}
		super.onChangeParent();
	}
	
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public Skills getSkills() {
		return getInternal().getSkills();
	}

	public int getFreeCapacity() {
		return getInternal().getCapacity();
	}

	public long getExperience() {
		return getInternal().getExperience();
	}

	public int getLevel() {
		return getInternal().getLevel();
	}

	public int getMana() {
		return getInternal().getMana();
	}

	public int getMaxMana() {
		return getInternal().getMaxMana();
	}

	public int getMagicLevel() {
		return getInternal().getMagicLevel();
	}

	public int getSoul() {
		return getInternal().getSoul();
	}

	public int getStamina() {
		return stamina;
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public boolean lookAt(Thing thing) {
		return true;
	}
	
	public String getDescription() {
		return getName() + " (Level " + getLevel() + ").";
	}

	public void setOutfit(Outfit outfit) {
		getInternal().setOutfit(outfit);
	}
	
	public Cylinder getParent() {
		return getInternal().getParent();
	}

	public Stance getStance() {
		return stance;
	}

	public void setStance(Stance stance) {
		this.stance = stance;
	}

	public Town getTown() {
		return town;
	}

	public void setTown(Town town) {
		this.town = town;
	}

	public boolean addVip(Player vip) {
		InternalPlayer internal = getInternal();
		if(internal.isVip(vip.getGlobalId())) {
			return false;
		} else {
			internal.addVip(vip);
			return true;
		}
	}

	public boolean removeVip(long globalId) {
		InternalPlayer internal = getInternal();
		for(Player vip : internal.getVipList()) {
			if(vip.getGlobalId() == globalId) {
				internal.removeVip(vip);
				return true;
			}
		}
		return false;
	}
	
	public boolean isVip(Player player) {
		return getInternal().isVip(player.getGlobalId());
	}
	
}
