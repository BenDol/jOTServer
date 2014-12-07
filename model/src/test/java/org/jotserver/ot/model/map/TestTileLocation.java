package org.jotserver.ot.model.map;

import static org.jotserver.ot.model.item.TestItemProvider.*;
import static org.junit.Assert.*;

import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.util.Location;
import org.jotserver.ot.model.util.Position;
import org.junit.Before;
import org.junit.Test;

public class TestTileLocation {

	private TestableMap map;
	private Position pos;
	private Tile tile;

	@Before
	public void setUp() throws Exception {
		map = new TestableMap();
		pos = new Position(100, 100, 7);
		tile = new Tile(map, pos);
	}
	
	@Test
	public void locationFindsItem() {
		Item i = getRegularItem();
		tile.executeAddItem(getRegularItem());
		tile.executeAddItem(getRegularItem());
		tile.executeAddItem(getRegularItem());
		tile.executeAddItem(i);
		tile.executeAddItem(getRegularItem());
		tile.executeAddItem(getRegularItem());
		
		Location loc = new TileLocation(tile, 2);
		
		assertSame(i, loc.get());
	}
	
	@Test
	public void locationHasCorrectIndex() {
		Location loc = new TileLocation(tile, 2);
		assertEquals(2, loc.getIndex());
	}
	
	@Test
	public void locationHasCorrectCylinder() {
		Location loc = new TileLocation(tile, 2);
		assertSame(tile, loc.getCylinder());
	}
	
	@Test
	public void locationCanGetTopItem() {
		Item i = getRegularItem();
		tile.executeAddItem(getRegularItem());
		tile.executeAddItem(getRegularItem());
		tile.executeAddItem(getRegularItem());
		tile.executeAddItem(i);
		
		Location loc = new TileLocation(tile, -1);
		assertSame(i, loc.get());
	}
	
}
