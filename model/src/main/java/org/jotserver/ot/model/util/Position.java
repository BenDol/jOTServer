package org.jotserver.ot.model.util;

import org.hirondelle.HashCodeUtil;
import org.jotserver.ot.model.player.InventorySlot;

public class Position {

    private int x;
    private int y;
    private int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position() {
        this(0, 0, 0);
    }

    public Position(Position position, Direction direction) {
        this(position.x, position.y, position.z);
        testMap();
        switch(direction) {
        case NORTH: y--; break;
        case EAST: x++; break;
        case SOUTH: y++; break;
        case WEST: x--; break;
        case NORTHWEST: y--; x--; break;
        case NORTHEAST: y--; x++; break;
        case SOUTHWEST: y++; x--; break;
        case SOUTHEAST: y++; x++; break;
        case UP: z--; break;
        case DOWN: z++; break;
        }
    }

    public int getX() {
        testMap();
        return x;
    }

    public int getY() {
        testMap();
        return y;
    }

    public int getZ() {
        testMap();
        return z;
    }

    public boolean equals(Object obj) {
        if(obj instanceof Position) {
            Position pos = (Position)obj;
            return x == pos.x && y == pos.y && z == pos.z;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = HashCodeUtil.SEED;
        result = HashCodeUtil.hash(result, x);
        result = HashCodeUtil.hash(result, y);
        result = HashCodeUtil.hash(result, z);
        return result;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public int distanceTo(Position position) {
        testMap();
        return Math.max(Math.abs(x-position.x), Math.abs(y-position.y));
    }

    public Position scale(int distance) {
        testMap();
        return new Position(x*distance, y*distance, z*distance);
    }

    public Position add(Position delta) {
        testMap();
        return new Position(x+delta.x, y+delta.y, z+delta.z);
    }

    public Position add(Direction direction) {
        return new Position(this, direction);
    }

    public int getZDistanceTo(Position position) {
        testMap();
        return Math.abs(z-position.z);
    }

    /*public Direction directionTo(Position position) {
        Direction ret = Direction.NONE;
        if(getX() > position.getX()) {
            ret = ret.combine(Direction.WEST);
        } else if(getX() < position.getX()) {
            ret = ret.combine(Direction.EAST);
        }
        if(getY() > position.getY()) {
            ret = ret.combine(Direction.NORTH);
        } else if(getY() < position.getY()) {
            ret = ret.combine(Direction.SOUTH);
        }
        return ret;
    }*/

    public Direction directionTo(Position position) {
        testMap();
        position.testMap();

        if(!equals(position)) {
            Position ref = position.add(invert());
            double angle = Math.atan2(ref.getY(), ref.getX());
            double delta = Math.PI/8.0;
            if(between(angle, -delta, delta)) {
                return Direction.EAST;
            } else if(between(angle, delta, 3*delta)) {
                return Direction.SOUTHEAST;
            } else if(between(angle, 3*delta, 5*delta)) {
                return Direction.SOUTH;
            } else if(between(angle, 5*delta, 7*delta)) {
                return Direction.SOUTHWEST;
            } else if(between(angle, -3*delta, -delta)) {
                return Direction.NORTHEAST;
            } else if(between(angle, -5*delta, -3*delta)) {
                return Direction.NORTH;
            } else if(between(angle, -7*delta, -5*delta)) {
                return Direction.NORTHWEST;
            } else {
                return Direction.WEST;
            }
        } else {
            return Direction.NONE;
        }
    }

    private boolean between(double a, double b, double c) {
        return a >= b && a <= c;
    }

    public boolean isNextTo(Position position) {
        testMap();
        position.testMap();
        return getZDistanceTo(position) == 0 && distanceTo(position) <= 1;
    }

    public Position invert() {
        return new Position(-x, -y, -z);
    }

    /*
     * Methods for special positions.
     */

    public boolean isInventory() {
        return x == 0xFFFF && (y & 0x40) == 0;
    }

    public boolean isContainer() {
        return x == 0xFFFF && (y & 0x40) != 0;
    }

    private void testMap() {
        if(isInventory()) throw new IllegalStateException("Position is in inventory!");
        if(isContainer()) throw new IllegalStateException("Position is in container!");
    }

    public InventorySlot getInventorySlot() {
        if(isInventory()) {
            int slotId = y;
            if(slotId >= InventorySlot.HEAD.ordinal() && slotId <= InventorySlot.AMMO.ordinal()) {
                return InventorySlot.values()[slotId];
            } else {
                throw new IllegalStateException("Not a valid inventory slot!");
            }
        } else {
            throw new IllegalStateException("Position is not in inventory!");
        }
    }

    public int getContainerId() {
        if(isContainer()) {
            return y & 0x0F;
        } else {
            throw new IllegalStateException("Position is not in container!");
        }
    }

    public int getContainerSlot() {
        if(isContainer()) {
            return z;
        } else {
            throw new IllegalStateException("Position is not in container!");
        }
    }

}
