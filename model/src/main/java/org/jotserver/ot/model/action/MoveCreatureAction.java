package org.jotserver.ot.model.action;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.Direction;

public class MoveCreatureAction extends Action {
    private Creature creature;
    private Tile to;
    private Tile from;

    public MoveCreatureAction(Creature creature, Direction direction) {
        this.creature = creature;
        from = creature.getTile();
        if(from != null) {
            to = from.getTile(direction);
        } else {
            to = null;
        }
    }

    private boolean test() {
        if(from == null || to == null) {
            fail(ErrorType.NOTPOSSIBLE);
            return false;
        }
        ErrorType error = from.queryMoveCreature(creature, to);
        if(error != ErrorType.NONE) {
            fail(error);
        }
        return !hasFailed();
    }

    public boolean execute() {

        if(!test()) {
            return false;
        } else {
            from.executeMoveCreature(creature, to);
            return true;
        }
    }
}
