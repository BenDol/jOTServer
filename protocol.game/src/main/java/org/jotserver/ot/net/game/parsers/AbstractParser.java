package org.jotserver.ot.net.game.parsers;

import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Location;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.model.world.LocalGameWorld;
import org.jotserver.ot.net.game.MessageParser;
import org.jotserver.ot.net.game.ParserContext;

public abstract class AbstractParser implements MessageParser {

    private ParserContext context;

    public AbstractParser() {
        context = null;
    }

    public void setContext(ParserContext context) {
        this.context = context;
    }

    protected ParserContext getContext() {
        return context;
    }

    protected Player getPlayer() {
        return context.getPlayer();
    }

    protected LocalGameWorld getWorld() {
        return context.getWorld();
    }

    protected Location findThing(Position position, int stackPos) {
        if(position.isInventory()) {
            return getPlayer().getInventory().getSlotLocation(position.getInventorySlot());
        } else if(position.isContainer()) {
            Container container = getPlayer().getContainer(position.getContainerId());
            if(container != null) {
                return container.getSlotLocation(position.getContainerSlot());
            } else {
                return null;
            }
        } else {
            Tile tile = getWorld().getMap().getTile(position);
            if(tile != null) {
                return tile.getLocationOf(stackPos);
            } else {
                return null;
            }
        }
    }

}
