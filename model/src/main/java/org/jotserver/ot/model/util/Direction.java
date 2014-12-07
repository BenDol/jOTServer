package org.jotserver.ot.model.util;

import java.util.Arrays;

public enum Direction {
	NORTH, EAST, SOUTH, WEST, SOUTHWEST, SOUTHEAST, NORTHWEST, NORTHEAST, UP, DOWN, NONE;
	
	public Direction combine(Direction direction) {
		if(this == NONE) return direction;
		else if(direction == NONE) return this;
		else if(this == NORTH && direction == EAST) return Direction.NORTHEAST;
		else if(this == NORTH && direction == WEST) return Direction.NORTHWEST;
		else if(this == SOUTH && direction == EAST) return Direction.SOUTHEAST;
		else if(this == SOUTH && direction == WEST) return Direction.SOUTHWEST;
		
		else if(direction == NORTH && this == EAST) return Direction.NORTHEAST;
		else if(direction == NORTH && this == WEST) return Direction.NORTHWEST;
		else if(direction == SOUTH && this == EAST) return Direction.SOUTHEAST;
		else if(direction == SOUTH && this == WEST) return Direction.SOUTHWEST;
		
		else throw new IllegalArgumentException("Cannot combine directions " + this + " and " + direction + ".");
	}
	
	public Direction invert() {
		switch(this) {
		case NORTH:
			return SOUTH;
		case SOUTH: 
			return NORTH;
		case EAST:
			return WEST;
		case WEST: 
			return EAST;
		case UP:
			return DOWN;
		case DOWN: 
			return UP;
		case SOUTHWEST:
			return NORTHEAST;
		case SOUTHEAST:
			return NORTHWEST;
		case NORTHEAST:
			return SOUTHWEST;
		case NORTHWEST:
			return SOUTHEAST;
		default:
			return NONE;
		}
	}
	
	public Direction normalize() {
		switch(this) {
		case NORTHEAST:
		case SOUTHEAST:
			return Direction.EAST;
		case NORTHWEST:
		case SOUTHWEST:
			return Direction.WEST;
		case NORTH:
		case EAST:
		case SOUTH:
		case WEST:
			return this;
		default:
			return Direction.NONE;
		}
	}

	public boolean isDiagonal() {
		switch(this) {
		case NORTHWEST:
		case NORTHEAST:
		case SOUTHWEST:
		case SOUTHEAST:
			return true;
		default:
			return false;
		}
	}
	
	public boolean contains(Direction dir) {
		if(dir.isDiagonal() || dir.isZ() || dir == NONE) {
			throw new IllegalArgumentException("A Direction can not contain a diagonal, none or Z direction.");
		}
		if(this == dir) {
			return true;
		} else {
			for(Direction d : Arrays.asList(NORTH, EAST, SOUTH, WEST)) {
				if(d != dir && d.invert() != dir && d.combine(dir) == this) {
					return true;
				}
			}
			return false;
		}
	}

	public boolean isZ() {
		switch(this) {
		case UP:
		case DOWN:
			return true;
		default:
			return false;
		}
	}
	
}
