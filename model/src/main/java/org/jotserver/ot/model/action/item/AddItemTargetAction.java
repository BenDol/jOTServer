package org.jotserver.ot.model.action.item;

import org.jotserver.ot.model.action.ActionVisitor;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.Stackable;
import org.jotserver.ot.model.map.Tile;

public class AddItemTargetAction extends ActionVisitor {
	
	public enum Result { NONE, FAILURE, SUCCESS, PARTIAL, NEEDEXCHANGE }
	
	protected Item item;
	protected Result result;
	protected int partialCount;
	
	public AddItemTargetAction(Item item) {
		this.item = item;
		result = Result.NONE;
		partialCount = 0;
	}

	public void execute(Stackable stackable) {
		if(item instanceof Stackable) {
			Stackable add = (Stackable)item;
			int delta = Math.min(100-stackable.getCount(), add.getCount());
			stackable.executeAddCount(delta);
			if(add.getCount()-delta > 0) {
				add.executeRemoveCount(delta);
			}
		} else {
			throw new IllegalStateException("Trying to add a non-stackable item to a stackable one.");
		}
	}

	public boolean test(Stackable stackable) {
		if(!(item instanceof Stackable)) {
			fail(ErrorType.NOTENOUGHROOM, Result.FAILURE);
		} else if(stackable.getId() != item.getId() || stackable.getCount() >= 100) {
			fail(ErrorType.NOTENOUGHROOM, Result.FAILURE);
		} else {
			Stackable stackableItem = (Stackable)item;
			if(stackable.getCount()+stackableItem.getCount() > 100) {
				fail(ErrorType.NOTENOUGHROOM, Result.PARTIAL);
				partialCount = Math.min(100-stackable.getCount(), stackableItem.getCount());
				ErrorType error = stackable.queryAdd(stackableItem);
				if(error != ErrorType.NONE) {
					fail(error);
				}
			}
		}
		return !hasFailed();
	}

	public void execute(Container container) {
		container.execute(new AddItemCylinderAction(item, container.getLocationOf(null)));
	}

	public boolean test(Container container) {
		if(container.getParent() instanceof Tile) {
			fail(ErrorType.NOTPOSSIBLE, Result.FAILURE);
		} else {
			ActionVisitor addContainer = new AddItemCylinderAction(item, container.getLocationOf(null));
			if(!container.test(addContainer)) {
				fail(addContainer.getError(), Result.FAILURE);
			}
		}
		return !hasFailed();
	}
	
	protected void fail(ErrorType error, Result result) {
		super.fail(error);
		this.result = result;
	}

	public Result getResult() {
		return result;
	}

	public int getPartialCount() {
		return partialCount;
	}
}
