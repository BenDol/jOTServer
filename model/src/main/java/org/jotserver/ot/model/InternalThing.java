/**
 * 
 */
package org.jotserver.ot.model;

import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.Position;

public abstract class InternalThing {

    private final Thing thing;
    private Cylinder parent;

    public InternalThing(Thing thing, Cylinder parent) {
        this.thing = thing;
        this.parent = parent;
    }

    /*
     * Methods for public use
     */

    public boolean isPlaced() {
        return parent != null && parent.isPlaced();
    }

    public Tile getTile() {
        if(parent != null) {
            return parent.getTile();
        } else {
            return null;
        }
    }

    public Position getPosition() {
        if(parent != null && parent.getTile() != null) {
            return parent.getTile().getPosition();
        } else {
            return null;
        }
    }

    public Cylinder getParent() {
        return parent;
    }

    public Thing getThing() {
        return thing;
    }

    /*
     * Methods for private use by Internal* classes.
     */
    protected void setParent(Cylinder parent) {
        this.parent = parent;
        thing.onChangeParent();
    }

}