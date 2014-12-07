package org.jotserver.ot.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Spectators;
import org.jotserver.ot.model.player.Player;
import org.junit.Before;
import org.junit.Test;


public class TestSimpleSpectators {
	
	private Mockery context;

	@Before
	public void setUp() {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
	}
	
	@Test
	public void providesEmptyInstance() {
		Spectators<Player> s = SimpleSpectators.getEmpty(Player.class);
		assertFalse(s.iterator().hasNext());
		assertNull(s.iterator().next());
	}
	
	@Test
	public void isEmptyByDefault() {
		Spectators<Player> s = new SimpleSpectators<Player>();
		assertFalse(s.iterator().hasNext());
	}
	
	@Test
	public void singleObject() {
		Player player = context.mock(Player.class);
		Spectators<Player> s = new SimpleSpectators<Player>(player);
		Iterator<Player> it = s.iterator();
		assertTrue(it.hasNext());
		assertSame(player, it.next());
		assertFalse(it.hasNext());
	}
	
	@Test
	public void filtersInvalidSpectators() {
		Player player = context.mock(Player.class);
		Creature c1 = context.mock(Creature.class);
		Spectators<Player> s = new SimpleSpectators<Player>(Player.class, Arrays.asList(c1, player));
		Iterator<Player> it = s.iterator();
		assertTrue(it.hasNext());
		assertSame(player, it.next());
		assertFalse(it.hasNext());
	}
	
	@Test
	public void copiesInputCollection() {
		Player player = context.mock(Player.class);
		Creature c1 = context.mock(Creature.class);
		Spectators<Creature> s = new SimpleSpectators<Creature>(Arrays.asList(c1, player));
		Iterator<Creature> it = s.iterator();
		assertTrue(it.hasNext());
		assertSame(c1, it.next());
		assertTrue(it.hasNext());
		assertSame(player, it.next());
		assertFalse(it.hasNext());
	}
	
}
