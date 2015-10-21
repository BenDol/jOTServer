package org.jotserver.ot.model.action.item;

import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.item.Stackable;
import org.jotserver.ot.model.player.Player;

public class PlayerRemoveItemTargetAction extends RemoveItemTargetAction {

	private Player player;

	public PlayerRemoveItemTargetAction(Player player, int count) {
		super(count);
		this.player = player;
	}

	public boolean test(Stackable stackable) {
		if(super.test(stackable)) {
			//
		} else if(player.getPosition().distanceTo(stackable.getPosition()) > 1) {
			fail(ErrorType.TOOFARAWAY);
		}
		return !hasFailed();
	}
}
