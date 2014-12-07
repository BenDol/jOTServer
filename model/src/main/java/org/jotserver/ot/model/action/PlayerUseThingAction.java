package org.jotserver.ot.model.action;

import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Dispatcher;

public class PlayerUseThingAction extends PlayerAction {

	private Thing thing;

	public PlayerUseThingAction(Dispatcher dispatcher, Player player, Thing thing) {
		super(dispatcher, player);
		this.thing = thing;
	}

	
	public boolean execute() {
		
		if(player.getPosition().distanceTo(thing.getPosition()) > 1) {
			if(!walkToExecute(thing.getPosition(), 1)) {
				fail(ErrorType.THEREISNOWAY);
				return false;
			} else {
				return true;
			}
		}
		
		ActionVisitor use = new UseThingAction(player);
		if(thing.test(use)) {
			thing.execute(use);
			return true;
		} else {
			fail(use.getError());
			return false;
		}
	}

}
