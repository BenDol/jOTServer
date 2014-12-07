package org.jotserver.ot.model.util;

public class Interval3D {
	
	public final Position end;
	public final Position start;
	
	public Interval3D(Interval2D interval, int startZ, int endZ) {
		this(interval.startX, interval.startY, startZ, interval.endX, interval.endY, endZ);
	}
	
	public Interval3D(Position start, int width, int height, int depth) {
		this(start, new Position(start.getX()+width, start.getY()+height, start.getZ()+depth));
	}
	
	public Interval3D(Position start, Position end) {
		this.start = start;
		this.end = end;
	}

	public Interval3D(int startX, int startY, int startZ, int endX, int endY, int endZ) {
		this(new Position(startX, startY, startZ), new Position(endX, endY, endZ));
	}
	
	public Interval2D get2D() {
		return new Interval2D(start, end);
	}

	public int getStartX() {
		return start.getX();
	}

	public int getStartY() {
		return start.getY();
	}

	public int getStartZ() {
		return start.getZ();
	}

	public int getEndX() {
		return end.getX();
	}

	public int getEndY() {
		return end.getY();
	}

	public int getEndZ() {
		return end.getZ();
	}

	public Position getEnd() {
		return end;
	}

	public Position getStart() {
		return start;
	}
	
}
