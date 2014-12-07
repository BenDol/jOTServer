package org.jotserver.ot.net.game;

import org.jotserver.ot.model.map.Map;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Interval;
import org.jotserver.ot.model.util.Interval2D;
import org.jotserver.ot.model.util.Interval3D;
import org.jotserver.ot.model.util.Position;

public class ClientView {
	
	public static Interval3D get3DView(Position position) {
		Interval zSpan = getZSpan(position.getZ());
		return new Interval3D(get2DView(position), zSpan.getStart(), zSpan.getEnd());
	}
	
	public static Interval3D get3DBorderView(Position position, Direction direction) {
		Interval zSpan = getZSpan(position.getZ());
		return new Interval3D(get2DView(position, direction), zSpan.getStart(), zSpan.getEnd());
	}

	public static Interval getZSpan(int z) {
		int startz;
		int endz;
		if (z > 7) {
			startz = z - 2;
			endz = Math.min(Map.MAX_Z - 1, z + 2);
		} else {
			startz = 7;
			endz = 0;
		}
		return new Interval(startz, endz);
	}
	
	public static Interval2D get2DView(Position center) {
		return get2DView(center.getX(), center.getY());
	}
	
	public static Interval2D get2DView(Position center, Direction direction) {
		return get2DView(center.getX(), center.getY(), direction);
	}

	public static Interval2D get2DView(int x, int y) {
		int startX = x-8;
		int startY = y-6;
		return new Interval2D(startX, startY, startX+17, startY+13);
	}
	
	private static Interval2D get2DView(int x, int y, Direction direction) {
		int startX = x-8;
		int startY = y-6;
		int width = 0;
		int height = 0;
		switch(direction) {
		case NORTH:
			//Leave startX and startY!
			width = 17;
			height = 0;
			break;
		case SOUTH:
			startY = startY+13;
			width = 17;
			height = 0;
			break;
		case WEST:
			
			width = 0;
			height = 13;
			break;
		case EAST:
			startX = startX+17;
			width = 0;
			height = 13;
			break;
		}
		return new Interval2D(startX, startY, startX+width, startY+height);
	}
}
