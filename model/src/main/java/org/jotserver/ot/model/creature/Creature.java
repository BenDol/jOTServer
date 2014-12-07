package org.jotserver.ot.model.creature;

import org.jotserver.ot.model.Light;
import org.jotserver.ot.model.Outfit;
import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.action.Tester;
import org.jotserver.ot.model.action.Visitor;
import org.jotserver.ot.model.chat.PrivateMessageChannel;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Position;

public abstract class Creature extends Thing {
	
	private CreatureWalkBrain autoWalkBrain;
	protected PrivateMessageChannel privateChannel;

	public Creature() {
		super();
		autoWalkBrain = null;
		privateChannel = new PrivateMessageChannel(this);
	}

	protected InternalCreature getInternal() {
		return (InternalCreature)super.getInternal();
	}

	public boolean test(Tester action) {
		return action.test(this);
	}

	public void execute(Visitor visitor) {
		visitor.execute(this);
	}
	
	public String getName() {
		return getInternal().getName();
	}
	
	public boolean turn(Direction direction) {
		getInternal().turn(direction);
		return true;
	}

	public boolean canSee(Position target) {
		if(getPosition().getZ() <= 7) {
			//we are on ground level or above (7 -> 0)
			//view is from 7 -> 0
			if(target.getZ() > 7) {
				return false;
			}
		} else if(getPosition().getZ() >= 8) {
			//we are underground (8 -> 15)
			//view is +/- 2 from the floor we stand on
			if(Math.abs(getPosition().getZ() - target.getZ()) > 2) {
				return false;
			}
		}
		
		//negative offset means that the action taken place is on a lower floor than ourself
		int zOffset = getPosition().getZ() - target.getZ();
		
		if ((target.getX() >= getPosition().getX() - 8 + zOffset) && 
				(target.getX() <= getPosition().getX() + 9 + zOffset) &&
				(target.getY() >= getPosition().getY() - 6 + zOffset) && 
				(target.getY() <= getPosition().getY() + 7 + zOffset)) {
			return true;
		}
	
		return false;
	}
	
	public Outfit getOutfit() {
		return getInternal().getOutfit();
	}

	public long getId() {
		return getInternal().getId();
	}
	
	public boolean isDead() {
		return getHealth() <= 0;
	}

	public Direction getDirection() {
		return getInternal().getDirection();
	}

	public int getHealth() {
		return getInternal().getHealth();
	}

	public int getMaxHealth() {
		return getInternal().getMaxHealth();
	}

	public Light getLight() {
		return getInternal().getLight();
	}

	public int getSpeed() {
		return getInternal().getSpeed();
	}
	
	public int getStepDuration() {
		Tile tile = getTile();
		if(tile != null && tile.getGround() != null){
			Item ground = tile.getGround();
			return getStepDuration(ground);
		} else {
			return 0;
		}
	}

	public int getStepDuration(Item item) {
		int speed = item.getSpeed();
		if(getSpeed() != 0){
			return (1000 * speed) / getSpeed();
		} else {
			return 0;
		}
	}
	
	public void walk(CreatureWalkBrain walkBrain) {
		cancelWalk();
		autoWalkBrain = walkBrain;
	}
	
	public boolean cancelWalk() {
		boolean ret = autoWalkBrain != null && !autoWalkBrain.isCancelled();
		if(autoWalkBrain != null) {
			autoWalkBrain.cancel();
			autoWalkBrain = null;
		}
		return ret;
	}
	
	public CreatureWalkBrain getWalkBrain() {
		return autoWalkBrain;
	}

	public int getHealthPercent() {
		int val = getHealth()*100 / Math.max(getMaxHealth(), 1);
		val = Math.max(val, 0);
		val = Math.min(val, 100);
		return val;
	}

	public PrivateMessageChannel getPrivateChannel() {
		return privateChannel;
	}
	
	public boolean canReach(Thing thing) {
		return getPosition().isNextTo(thing.getPosition());
	}
	
}
