package org.jotserver.ot.model.creature;

import java.util.Collection;
import java.util.LinkedList;

import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Position;

public class DirectionPath implements Path {
	
	private LinkedList<Direction> path;
	private Position position;
	
	private DirectionPath() {
		path = new LinkedList<Direction>();
		position = null;
	}
	
	public DirectionPath(Position start) {
		this();
		position = start;
	}
	
	public DirectionPath(Position start, Collection<Direction> path) {
		this();
		position = start;
		this.path.addAll(path);
	}
	
	public void addStep(Direction direction) {
		path.addLast(direction);
	}
	
	
	public Position getCurrentPosition() {
		return position;
	}

	
	public Direction getNextStep() {
		if(path.isEmpty()) {
			return null;
		} else {
			Direction ret = path.removeFirst();
			if(ret != null) {
				position = new Position(position, ret);
			}
			return ret;
		}
	}

	
	public boolean isEmpty() {
		return path.isEmpty();
	}

	public void addStepFirst(Direction direction) {
		path.addFirst(direction);
	}

}
