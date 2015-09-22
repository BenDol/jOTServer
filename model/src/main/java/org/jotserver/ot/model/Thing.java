package org.jotserver.ot.model;

import org.jotserver.ot.model.action.ActionVisitable;
import org.jotserver.ot.model.action.Tester;
import org.jotserver.ot.model.action.Visitor;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Spectators;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.Location;
import org.jotserver.ot.model.util.Position;

public abstract class Thing implements ActionVisitable {

    private InternalThing internal;
    private Position temporaryPosition;

    public Thing() {
         this.internal = null;
         temporaryPosition = null;
    }

    public boolean test(Tester action) {
        return action.test(this);
    }

    public void execute(Visitor visitor) {
        visitor.execute(this);
    }

    protected void setInternal(InternalThing internal) {
        if(internal == null || internal.getThing() != this) {
            throw new IllegalArgumentException("Invalid internal thing.");
        }
        this.internal = internal;
    }

    protected InternalThing getInternal() {
        if(internal == null) throw new IllegalStateException("Missing internal state!");
        return internal;
    }

    public void setParent(Cylinder parent) {
        getInternal().setParent(parent);
    }

    public Position getPosition() {
        return getInternal().getPosition();
    }

    public Cylinder getParent() {
        return getInternal().getParent();
    }

    public Tile getTile() {
        return getInternal().getTile();
    }

    public Position getTemporaryPosition() {
        return temporaryPosition;
    }

    public void setTemporaryPosition(Position temporaryPosition) {
        this.temporaryPosition = temporaryPosition;
    }

    public boolean isPlaced() {
        return getInternal().isPlaced();
    }

    public abstract String getDescription();

    public <T extends Creature> Spectators<T> getSpectators(Class<T> type) {
        if(getInternal().getParent() != null) {
            return getInternal().getParent().getContentsSpectators(type);
        } else {
            return SimpleSpectators.getEmpty(type);
        }
    }

    public Location getLocation() {
        if(getParent() != null) {
            return getParent().getLocationOf(this);
        } else {
            return null;
        }
    }

    public boolean isParent(Cylinder cylinder) {
        Cylinder parent = getParent();
        while(parent != null) {
            if(parent.equals(cylinder)) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    public void onChangeParent() {
        //
    }

    public boolean isVisibleTo(Creature creature) {
        return getParent() != null && getParent().isVisibleTo(creature);
    }

}
