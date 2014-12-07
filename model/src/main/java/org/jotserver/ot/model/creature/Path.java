package org.jotserver.ot.model.creature;

import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Position;

public interface Path {
	public static final Path EMPTY = new EmptyPath();
	
	public Direction getNextStep();
	public Position getCurrentPosition();
	public boolean isEmpty();
}

class EmptyPath implements Path {

	
	public Position getCurrentPosition() {
		return null;
	}

	
	public Direction getNextStep() {
		return null;
	}

	
	public boolean isEmpty() {
		return true;
	}
	
}