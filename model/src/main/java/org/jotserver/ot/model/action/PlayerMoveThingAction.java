package org.jotserver.ot.model.action;

import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.action.item.PlayerMoveItemAction;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Dispatcher;
import org.jotserver.ot.model.util.Location;

public class PlayerMoveThingAction extends Action {

    private Location to;
    private Location from;
    private Player player;
    private int count;
    private Dispatcher dispatcher;

    public PlayerMoveThingAction(Dispatcher dispatcher, Player player, Location from, Location to, int count) {
        this.dispatcher = dispatcher;
        this.player = player;
        this.from = from;
        this.to = to;
        this.count = count;
    }

    public boolean execute() {
        Thing thing = from.get();
        if(thing != null) {
            if(thing instanceof Item) {
                Item item = (Item)thing;
                PlayerMoveItemAction moveAction = new PlayerMoveItemAction(dispatcher, player, item, count, to);

                if(!moveAction.execute()) {
                    fail(moveAction.getError());
                    return false;
                } else {
                    return true;
                }

            } else {
                fail(ErrorType.NOTPOSSIBLE);
                return false;
            }
        } else {
            fail(ErrorType.NOTPOSSIBLE);
            return false;
        }
    }
}
