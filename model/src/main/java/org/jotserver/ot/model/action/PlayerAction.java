package org.jotserver.ot.model.action;

import org.jotserver.ot.model.creature.CreatureWalkBrain;
import org.jotserver.ot.model.creature.Path;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Dispatcher;
import org.jotserver.ot.model.util.Position;

public abstract class PlayerAction extends Action {
	
	protected final Player player;
	private Dispatcher dispatcher;
	
	public PlayerAction(Dispatcher dispatcher, Player player) {
		super();
		this.dispatcher = dispatcher;
		this.player = player;
	}
	
	public boolean walkToExecute(Position destination, int radius) {
		Tile tile = player.getTile();
		if(tile != null) {
			Path path = tile.getMap().getPath(player.getPosition(), destination, radius);
			if(path != null) {
				setState(State.DELAYED);
				CreatureWalkBrain walkBrain = new CreatureWalkBrain(player, path);
				walkBrain.addAction(this);
				player.walk(walkBrain);
				dispatcher.run(walkBrain, walkBrain.getDelay());
				return true;
			}
		}
		return false;
	}

}
