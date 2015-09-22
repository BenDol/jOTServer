package org.jotserver.ot.model.player;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.SimpleSpectators;
import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.action.Tester;
import org.jotserver.ot.model.action.Visitor;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.ItemAttribute;
import org.jotserver.ot.model.map.Spectators;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.ItemLocation;
import org.jotserver.ot.model.util.Location;

public class Inventory implements Cylinder {

    private final Player player;
    private final InternalInventory internal;

    public Inventory(Player player) {
        this.player = player;
        internal = new InternalInventory(this);
    }

    protected InternalInventory getInternal() {
        return internal;
    }

    /*
     * Action methods
     */

    public ErrorType queryAddItem(InventorySlot slot, Item item) {
        if(!item.hasAttribute(ItemAttribute.PICKUPABLE)) {
            return ErrorType.CANNOTPICKUP;
        } else if(!item.fitsOn(slot)) {
            return ErrorType.DOESNOTFIT;
        } else if(!isEmpty(slot)) {
            return ErrorType.NOTENOUGHROOM;
        } else {
            return ErrorType.NONE;
        }
    }

    public void executeAddItem(InventorySlot slot, Item item) {
        getInternal().setItem(slot, item);
    }

    public ErrorType queryRemoveItem(Item item) {
        InventorySlot slot = this.getSlot(item);
        if(slot == null) {
            return ErrorType.NOTPOSSIBLE;
        } else {
            return queryRemoveItem(slot);
        }
    }

    public void executeRemoveItem(Item item) {
        InventorySlot slot = this.getSlot(item);
        executeRemoveItem(slot);
    }

    public ErrorType queryRemoveItem(InventorySlot slot) {
        if(isEmpty(slot)) {
            return ErrorType.NOTPOSSIBLE;
        } else {
            return ErrorType.NONE;
        }
    }

    public void executeRemoveItem(InventorySlot slot) {
        getInternal().removeItem(slot);
    }




    public <T extends Creature> Spectators<T> getContentsSpectators(Class<T> type) {
        if(type.isInstance(player)) {
            return new SimpleSpectators<T>(type.cast(player));
        } else {
            return SimpleSpectators.getEmpty(type);
        }
    }

    public ItemLocation getLocationOf(Thing thing) {
        if(thing instanceof Item) {
            Item item = (Item)thing;
            InventorySlot slot = internal.getSlot(item);
            if(slot == null) {
                return null;
            } else {
                return internal.getSlotLocation(slot);
            }
        } else {
            return null;
        }
    }


    public Cylinder getParent() {
        return player.getParent();
    }


    public Tile getTile() {
        return player.getTile();
    }


    public boolean isPlaced() {
        return player.isPlaced();
    }


    public void execute(Visitor visitor) {
        visitor.execute(this);
    }

    public boolean test(Tester tester) {
        return tester.test(this);
    }


    public boolean isEmpty(InventorySlot slot) {
        return getInternal().isEmpty(slot);
    }

    public boolean hasItem(Item item) {
        return getInternal().hasItem(item);
    }

    public ItemLocation getSlotLocation(InventorySlot slot) {
        return getInternal().getSlotLocation(slot);
    }

    public Player getPlayer() {
        return player;
    }

    public InventorySlot getSlot(Item item) {
        return getInternal().getSlot(item);
    }

    public boolean isVisibleTo(Creature creature) {
        return player.equals(creature);
    }

    public Item getItem(InventorySlot slot) {
        return getInternal().getItem(slot);
    }

}
