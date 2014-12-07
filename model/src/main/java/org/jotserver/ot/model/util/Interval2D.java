package org.jotserver.ot.model.util;

public class Interval2D {
	
	public final int startX, startY;
	public final int endX, endY;
	
	public Interval2D(Position start, int width, int height) {
		this(start.getX(), start.getY(), 
				start.getX()+width, start.getY()+height);
	}
	
	public Interval2D(Position start, Position end) {
		this(start.getX(), start.getY(), end.getX(), end.getY());
	}
	
	public Interval2D(int startX, int startY, int endX, int endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}

	public int getStartX() {
		return startX;
	}

	public int getStartY() {
		return startY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}
	
	public int getClosestX(int x) {
		return getClosest(startX, endX, x);
	}
	
	public int getClosestY(int y) {
		return getClosest(startY, endY, y);
	}
	
	private int getClosest(int from, int to, int val) {
		if(val < from) {
			return from;
		} else if(val > to) {
			return to;
		} else {
			return val;
		}
	}
	
	public boolean contains(int x, int y) {
		return x >= startX && x <= endX && y >= startY && y <= endY;
	}
	
	public Interval2D offset(int x, int y) {
		return new Interval2D(startX+x, startY+y, endX+x, endY+y);
	}
	
	public String toString() {
		return "[x: " + startX + "-" + endX + ", y: " + startY + "-" + endY + "]";
	}
}
