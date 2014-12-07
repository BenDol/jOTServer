package org.jotserver.ot.model.player;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.creature.TestableCreature;
import org.jotserver.ot.model.map.TestableMap;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.model.world.LocalGameWorld;
import org.jotserver.ot.model.world.WorldState;
import org.junit.Before;
import org.junit.Test;

public class TestCreatureCache {

	private CreatureCache cache;
	private TestableCreature owner;
	private Mockery context;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		
		
		owner = new TestableCreature(1337, "1337");
		cache = new CreatureCache(owner, 3);
	}
	
	@Test
	public void newCacheIsEmpty() {
		assertTrue(cache.getCreatures().isEmpty());
	}
	
	@Test
	public void cacheHasOwner() {
		assertSame(owner, cache.getOwner());
	}
	
	@Test
	public void canAddCreature() {
		Creature creature = new TestableCreature(1, "1");
		cache.addCreature(creature);
		assertTrue(cache.isCached(creature));
		assertTrue(cache.getCreatures().contains(creature));
	}
	
	@Test
	public void canNotAddMoreThenMaxCreatures() {
		cache.addCreature(new TestableCreature(1, "1"));
		cache.addCreature(new TestableCreature(2, "2"));
		cache.addCreature(new TestableCreature(3, "3"));
		cache.addCreature(new TestableCreature(4, "4"));
		assertEquals(3, cache.getCreatures().size());
	}
	
	@Test
	public void canNotContainOnecreatureTwice() {
		Creature creature = new TestableCreature(1, "1");
		cache.addCreature(creature);
		cache.addCreature(creature);
		assertEquals(1, cache.getCreatures().size());
	}
	
	@Test
	public void returnsAddedCreatureWhenNoneWasRemoved() {
		Creature creature = new TestableCreature(1, "1");
		assertSame(creature, cache.addCreature(creature));
	}
	
	@Test
	public void returnsNullWhenReAddingCreature() {
		Creature creature = new TestableCreature(1, "1");
		cache.addCreature(creature);
		assertNull(cache.addCreature(creature));
	}
	
	@Test
	public void returnsRemovedCreatureWhenRemoved() {
		Creature c;
		cache.addCreature(c = new TestableCreature(1, "1"));
		cache.addCreature(new TestableCreature(2, "2"));
		cache.addCreature(new TestableCreature(3, "3"));
		TestableCreature c2 = new TestableCreature(4, "4");
		assertSame(c, cache.addCreature(c2));
	}
	
	@Test(expected = IllegalStateException.class)
	public void throwsExceptionUponUnhandledOverflow() {
		TestableMap m = new TestableMap(new Position(100, 100, 7), 5);
		
		final LocalGameWorld world = context.mock(LocalGameWorld.class);
		m.setGameWorld(world);
		
		context.checking(new Expectations() {{
			allowing(world).getState(); will(returnValue(WorldState.RUNNING));
		}});
		
		Creature c;
		
		m.getTile(new Position(100, 100, 7)).executeAddCreature(owner);
		
		m.getTile(new Position(101, 100, 7)).executeAddCreature(c = new TestableCreature(1, "1"));
		cache.addCreature(c);
		
		m.getTile(new Position(101, 101, 7)).executeAddCreature(c = new TestableCreature(2, "2"));
		cache.addCreature(c);
		
		m.getTile(new Position(101, 99, 7)).executeAddCreature(c = new TestableCreature(3, "3"));
		cache.addCreature(c);
		
		m.getTile(new Position(100, 101, 7)).executeAddCreature(c = new TestableCreature(4, "4"));
		cache.addCreature(c);
		
	}

}
