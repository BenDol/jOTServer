package org.jotserver.ot.model.map;

import java.util.HashMap;

import org.jotserver.ot.model.item.BaseItemTypeAccessor;
import org.jotserver.ot.model.item.ItemType;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.model.world.LocalGameWorld;
import org.junit.Ignore;

@Ignore
public class TestableMap extends Map {
	
	private HashMap<Position, Tile> tiles;
	private LocalGameWorld world2;
	
	public TestableMap() {
		super(null);
		tiles = new HashMap<Position, Tile>();
	}
	
	public TestableMap(Position c, int delta) {
		this(c, delta, c.getZ(), c.getZ());
	}
	
	public TestableMap(Position c, int delta, int fromZ, int toZ) {
		this();
		for(int x = -delta; x <= delta; x++) {
			for(int y = -delta; y <= delta; y++) {
				for(int z = fromZ; z <= toZ; z++) {
					Position p = new Position(c.getX()+x, c.getY()+y, z);
					Tile t = new Tile(this, p);
					setTile(p, t);
				}
			}
		}
	}
	
	public void setTile(Position pos, Tile tile) {
		tiles.put(pos, tile);
	}

	@Override
	public Tile getTile(Position position) {
		return tiles.get(position);
	}

	@Override
	public TownAccessor getTownAccessor() {
		return null;
	}
	
	public void fillWithItemsOfType(ItemType type) {
		for(Tile t : tiles.values()) {
			t.executeAddItem(new BaseItemTypeAccessor(1).createItem(type));
		}
	}
	
	public void setGameWorld(LocalGameWorld world) {
		this.world2 = world;
	}
	
	public LocalGameWorld getGameWorld() {
		return world2;
	}

}
