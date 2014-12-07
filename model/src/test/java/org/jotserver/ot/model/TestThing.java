package org.jotserver.ot.model;


import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Spectators;
import org.jotserver.ot.model.util.Position;
import org.junit.Before;
import org.junit.Test;

public class TestThing {

	private Mockery context;
	private Cylinder cylinder;
	private Thing thing;
	private InternalThing internal;
	private InternalThing mockedInternal;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		cylinder = context.mock(Cylinder.class);
		
		thing = new Thing() {
			@Override
			public String getDescription() {
				return null;
			}
		};
		
		internal = new InternalThing(thing, cylinder) {};
		
		mockedInternal = context.mock(InternalThing.class);
		context.checking(new Expectations() {{
			allowing(mockedInternal).getThing(); will(returnValue(thing));
		}});
	}
	
	@Test
	public void setValidInternalThing() {
		thing.setInternal(internal);
		assertSame(internal, thing.getInternal());
	}
	
	@Test(expected = IllegalStateException.class)
	public void retrievalOfMissingInternalThrowsException() {
		thing.getInternal();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void cannotAssignNonexisitingInternal() {
		thing.setInternal(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void cannotAssignInvalidInternal() {
		final Thing other = new Thing() { public String getDescription() { return null; } };
		
		final InternalThing i = context.mock(InternalThing.class);
		context.checking(new Expectations() {{
			oneOf(i).getThing(); will(returnValue(other));
		}});
		
		thing.setInternal(i);
		
		context.assertIsSatisfied();
	}
	
	@Test
	public void reflectsInternalParent() {
		thing.setInternal(internal);
		assertSame(cylinder, thing.getParent());
	}
	
	@Test
	public void changedParentUpdatesInternalParent() {
		context.checking(new Expectations() {{
			oneOf(mockedInternal).setParent(cylinder);
		}});
		thing.setInternal(mockedInternal);
		thing.setParent(cylinder);
		context.assertIsSatisfied();
	}
	
	@Test
	public void temporaryPosition() {
		assertNull(thing.getTemporaryPosition());
		Position pos = new Position();
		thing.setTemporaryPosition(pos);
		assertSame(pos, thing.getTemporaryPosition());
	}
	
	@Test
	public void getTileAndIsPlacedAndGetPositionConsultsInternal() {
		context.checking(new Expectations() {{
			oneOf(mockedInternal).getTile();
			oneOf(mockedInternal).isPlaced();
			oneOf(mockedInternal).getPosition();
		}});
		
		thing.setInternal(mockedInternal);
		thing.getTile();
		thing.isPlaced();
		thing.getPosition();
		context.assertIsSatisfied();
	}
	
	@Test
	public void unplacedThingHasNoSpectators() {
		internal.setParent(null);
		thing.setInternal(internal);
		assertFalse(thing.getSpectators(Creature.class).iterator().hasNext());
	}
	
	@Test
	public void placedThingConsultsParentContentsSpectators() {
		final Spectators<Creature> spect = SimpleSpectators.getEmpty(Creature.class);
		context.checking(new Expectations() {{
			oneOf(cylinder).getContentsSpectators(Creature.class); will(returnValue(spect));
		}});
		
		thing.setInternal(internal);
		
		assertSame(spect, thing.getSpectators(Creature.class));
		
		context.assertIsSatisfied();
	}
	
	@Test
	public void isParentIdentifiesImmideateParent() {
		thing.setInternal(internal);
		assertTrue(thing.isParent(cylinder));
	}
	
	@Test
	public void isParentWorksForUnplacedThings() {
		internal.setParent(null);
		thing.setInternal(internal);
		assertFalse(thing.isParent(cylinder));
	}
	
	@Test
	public void unplacedThingHasNoLocation() {
		internal.setParent(null);
		thing.setInternal(internal);
		assertNull(thing.getLocation());
	}
	
	@Test
	public void providesLocationFromParent() {
		thing.setInternal(internal);
		context.checking(new Expectations() {{
			oneOf(cylinder).getLocationOf(thing);
		}});
		
		thing.getLocation();
		
		context.assertIsSatisfied();
	}
	
	@Test
	public void unplacedThingIsNotVisible() {
		Creature creature = context.mock(Creature.class);
		internal.setParent(null);
		thing.setInternal(internal);
		assertFalse(thing.isVisibleTo(creature));
	}
	
	@Test
	public void placedThingConsultsParentForVisibility() {
		final Creature creature = context.mock(Creature.class);
		thing.setInternal(internal);
		context.checking(new Expectations() {{
			oneOf(cylinder).isVisibleTo(creature); will(returnValue(true));
		}});
		assertTrue(thing.isVisibleTo(creature));
		context.assertIsSatisfied();
	}
	
}
