package org.jotserver.ot.model.creature;

import org.jotserver.ot.model.action.Action;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.Brain;
import org.jotserver.ot.model.util.Direction;

import java.util.LinkedList;

public class CreatureWalkBrain extends Brain {
	
	Creature creature;
	private LinkedList<Action> actions;
	private Path path;
	
	public CreatureWalkBrain(Creature creature, Path path) {
		actions = new LinkedList<Action>();
		this.creature = creature;
		this.path = path;
	}

	public void think() {
		if(shouldCancel()) {
			cancel();
		} else {
			if(path.isEmpty() && !actions.isEmpty()) {
				performActions();
			} else if(!path.isEmpty()) {
				nextStep();
				if(path.isEmpty()) {
					think();
				}
			}
		}
	}

	protected void performActions() {
        while (!actions.isEmpty()) {
            Action action = actions.poll();
            if (!action.execute()) {
                reportError(action.getError());
                break;
            }
        }
    }

	protected void reportError(ErrorType error) {
		creature.getPrivateChannel().sendCancel(error);
		cancel();
	}

	protected void nextStep() {
		Direction direction = path.getNextStep();
		Tile tile = creature.getTile();
		ErrorType error = tile.queryMoveCreature(creature, direction);
		if(error == ErrorType.NONE) {
			tile.executeMoveCreature(creature, direction);
		}
	}

	protected boolean shouldCancel() {
		return path == null || !creature.isPlaced()
            || (path.isEmpty() && actions.isEmpty())
            || !creature.getPosition().equals(path.getCurrentPosition());
	}

	public long getDelay() {
		return creature.getStepDuration();
	}

	public void addAction(Action action) {
		actions.add(action);
	}
}
