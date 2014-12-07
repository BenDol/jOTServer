package org.jotserver.ot.model;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.Position;
import org.junit.Before;
import org.junit.Test;

public class TestInternalThing {

	private InternalThing unPlaced;
	private Mockery context;
	private Cylinder mockedParent;
	private Thing mockedThing;
	private InternalThing mocked;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		mockedParent = context.mock(Cylinder.class);
		mockedThing = context.mock(Thing.class);
		unPlaced = new InternalThing(mockedThing, null) {};
		mocked = new InternalThing(mockedThing, mockedParent) {};
	}
	
	@Test
	public void thingWithoutParentIsNotPlaced() {
		assertFalse(unPlaced.isPlaced());
	}
	
	@Test
	public void assignParent() {
		context.checking(new Expectations() {{
			oneOf(mockedThing).onChangeParent();
		}});
		assertNull(unPlaced.getParent());
		unPlaced.setParent(mockedParent);
		assertSame(mockedParent, unPlaced.getParent());
		context.assertIsSatisfied();
	}
	
	@Test
	public void placedConsultsParent() {
		context.checking(new Expectations() {{
			oneOf(mockedParent).isPlaced(); will(returnValue(true));
		}});
		assertTrue(mocked.isPlaced());
		context.assertIsSatisfied();
	}
	
	@Test
	public void unPlacedHasNoTile() {
		assertNull(unPlaced.getTile());
	}
	
	@Test
	public void unPlacedHasNoPosition() {
		assertNull(unPlaced.getPosition());
	}
	
	@Test
	public void consultsParentTile() {
		final Tile tile = context.mock(Tile.class);
		context.checking(new Expectations() {{
			oneOf(mockedParent).getTile(); will(returnValue(tile));
		}});
		assertSame(tile, mocked.getTile());
		context.assertIsSatisfied();
	}
	
	@Test
	public void consultsParentTilePosition() {
		final Position position = context.mock(Position.class);
		final Tile tile = context.mock(Tile.class);
		context.checking(new Expectations() {{
			atLeast(1).of(mockedParent).getTile(); will(returnValue(tile));
			oneOf(tile).getPosition(); will(returnValue(position));
		}});
		assertSame(position, mocked.getPosition());
		context.assertIsSatisfied();
	}
	
}
