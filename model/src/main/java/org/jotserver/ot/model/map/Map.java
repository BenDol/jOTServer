package org.jotserver.ot.model.map;

import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.creature.Path;
import org.jotserver.ot.model.map.pathfind.AStarPathFinder;
import org.jotserver.ot.model.player.Camera;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Interval2D;
import org.jotserver.ot.model.util.Interval3D;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.model.world.LocalGameWorld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Map {
	
	public static final int MAX_Z = 16;
	private LocalGameWorld world;
	
	public Map(LocalGameWorld world) {
		this.world = world;
	}
	
	public abstract Tile getTile(Position position);
	
	public abstract TownAccessor getTownAccessor();
	
	@SuppressWarnings("unchecked")
	public <T extends Creature> Spectators<T> getSpectators(Class<T> type, Position start, Position end) {
		final List<T> spectators = new ArrayList<T>();
		for(int z = start.getZ(); z <= end.getZ(); z++) {
			for(int y = start.getY(); y <= end.getY(); y++) {
				for(int x = start.getX(); x <= end.getX(); x++) {
					Tile tile = getTile(new Position(x, y, z));
					if(tile != null) {
						for(Creature creature : tile.getCreatures()) {
							if(type.isInstance(creature)) {
								spectators.add((T)creature);
							}
						}
					}
				}
			}
		}
		return new Spectators<T>() {
            public Iterator<T> iterator() {
                return spectators.iterator();
            }
        };
	}
	
	public <T extends Creature> Spectators<T> getSpectators(Class<T> type, Position center) {
		Interval3D interval = getSpectatorRange(center);
		return getSpectators(type, interval.getStart(), interval.getEnd());
	}
	
	public Interval3D getSpectatorRange(Position center) {
		Position viewport = new Position(Camera.viewportX, Camera.viewportY, 0);
		Interval2D interval = new Interval2D(center.add(viewport.invert()), center.add(viewport));
		
		int z = center.getZ();
		int startZ;
		int endZ;
		
		if(z > 7) {
			startZ = z - 2;
			endZ = Math.min(z + 2, MAX_Z - 1);
		} else {
			startZ = 0;
			endZ = Math.max(z + 2, 7);
		}
		
		return new Interval3D(interval, startZ, endZ);
	}
	
	public Spectators<Player> getSpectators(Position position) {
		return getSpectators(Player.class, position);
	}
	
	public Path getPath(Position from, Position to) {
		if(from.getZ() != to.getZ()) {
			return null;
		} else {
			return getPath(from, new Interval2D(to, 0, 0));
		}
	}
	
	public Path getPath(Position from, Interval2D to) {
		return new AStarPathFinder(this).findPath(null, from, to);
	}
	
	public Path getPath(Position from, Position to, int radius) {
		if(from.getZ() != to.getZ()) {
			return null;
		} else {
			Interval2D interval = new Interval2D(to.getX()-radius, to.getY()-radius, 
					to.getX()+radius, to.getY()+radius);
			return getPath(from, interval);
		}
	}

	public LocalGameWorld getGameWorld() {
		return world;
	}
}
