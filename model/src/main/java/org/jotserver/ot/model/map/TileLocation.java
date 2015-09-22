/**
 * 
 */
package org.jotserver.ot.model.map;

import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.util.Location;

final class TileLocation implements Location {

    private final Tile tile;
    private final int index;

    TileLocation(Tile tile, int index) {
        this.tile = tile;
        this.index = index;
    }

    public Thing get() {
        if(index == -1) {
            return this.tile.getTopItem();
        } else {
            return this.tile.getThing(index);
        }
    }

    public Cylinder getCylinder() {
        return this.tile;
    }

    public int getIndex() {
        return index;
    }
}