package org.jotserver.ot.model.action.item;

import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.ItemAttribute;
import org.jotserver.ot.model.item.Stackable;
import org.jotserver.ot.model.player.Player;

public class PlayerAddItemTargetAction extends AddItemTargetAction {
	
	private Player player;
	
	public PlayerAddItemTargetAction(Player player, Item item) {
		super(item);
		this.player = player;
	}

	public boolean test(Container container) {
		if(!super.test(container)) {
			//
		} else if(player.getPosition().distanceTo(container.getPosition()) > 1) {
			fail(ErrorType.TOOFARAWAY);
		} else if(!item.hasAttribute(ItemAttribute.MOVEABLE)) {
			fail(ErrorType.NOTMOVEABLE);
		}
		return !hasFailed();
	}

	public boolean test(Stackable stackable) {
		if(!super.test(stackable)) {
			//
		} else if(stackable.getPosition().distanceTo(player.getPosition()) > 5) {
			fail(ErrorType.UNREACHABLE);
		} else if(!item.hasAttribute(ItemAttribute.MOVEABLE)) {
			fail(ErrorType.NOTMOVEABLE);
		}
		return !hasFailed();
	}
}
