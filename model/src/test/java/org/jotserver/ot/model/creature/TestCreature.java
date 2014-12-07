package org.jotserver.ot.model.creature;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.Light;
import org.jotserver.ot.model.Outfit;
import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Position;
import org.junit.Before;
import org.junit.Test;

public class TestCreature {

	private static final long ID = 1337;
	private static final String NAME = "SomeName";
	private Mockery context;
	private Cylinder cylinder;
	private InternalCreature internal;
	private InternalCreature mockedInternal;
	private TestableCreature creature;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		
		cylinder = context.mock(Cylinder.class);
		creature = new TestableCreature();
		
		internal = new InternalCreature(creature, ID, NAME, cylinder);
		mockedInternal = context.mock(InternalCreature.class);
	}
	
	@Test
	public void hasInternal() {
		creature.setInternal(internal);
		assertSame(internal, creature.getInternal());
	}
	
	@Test
	public void reflectsInternalNameAndId() {
		creature.setInternal(internal);
		assertEquals(ID, creature.getId());
		assertEquals(NAME, creature.getName());
	}
	
	@Test
	public void turnTurnsInternal() {
		creature.setInternal(internal);
		creature.turn(Direction.EAST);
		assertEquals(creature.getDirection(), internal.getDirection());
		assertEquals(Direction.EAST, creature.getDirection());
	}
	
	@Test
	public void hasPrivateMessageChannel() {
		assertNotNull(creature.getPrivateChannel());
		assertSame(creature, creature.getPrivateChannel().getOwner());
	}
	
	@Test
	public void isDead() {
		creature.setInternal(internal);
		internal.setHealth(150);
		assertFalse(creature.isDead());
		internal.setHealth(0);
		assertTrue(creature.isDead());
	}
	
	@Test
	public void healthPercentValid() {
		creature.setInternal(internal);
		internal.setHealth(150);
		internal.setMaxHealth(300);
		assertEquals(50, creature.getHealthPercent());
	}
	
	@Test
	public void healthPercentMax() {
		creature.setInternal(internal);
		internal.setHealth(300);
		internal.setMaxHealth(300);
		assertEquals(100, creature.getHealthPercent());
	}
	
	@Test
	public void healthPercentMin() {
		creature.setInternal(internal);
		internal.setHealth(0);
		internal.setMaxHealth(300);
		assertEquals(0, creature.getHealthPercent());
	}
	
	@Test
	public void healthPercentNegative() {
		creature.setInternal(internal);
		internal.setHealth(-150);
		internal.setMaxHealth(300);
		assertEquals(0, creature.getHealthPercent());
	}
	
	@Test
	public void healthPercentTooHigh() {
		creature.setInternal(internal);
		internal.setHealth(450);
		internal.setMaxHealth(300);
		assertEquals(100, creature.getHealthPercent());
	}
	
	@Test
	public void calculatesStepDuration() {
		creature.setInternal(internal);
		internal.setSpeed(100);
		
		final Item item = context.mock(Item.class);
		context.checking(new Expectations() {{
			allowing(item).getSpeed(); will(returnValue(123));
		}});
		
		int estimated = 1000*123/100;
		assertEquals(estimated, creature.getStepDuration(item));
	}
	
	@Test
	public void noExceptionWhenZeroSpeedStepDuration() {
		creature.setInternal(internal);
		internal.setSpeed(0);
		
		final Item item = context.mock(Item.class);
		context.checking(new Expectations() {{
			allowing(item).getSpeed(); will(returnValue(123));
		}});
		
		creature.getStepDuration(item);
	}
	
	@Test
	public void calculatesStepDurationFromTileGroundItem() {
		final Tile tile = context.mock(Tile.class);
		final Item item = context.mock(Item.class);
		context.checking(new Expectations() {{
			allowing(tile).getGround(); will(returnValue(item));
			allowing(tile).getTile(); will(returnValue(tile));
			allowing(item).getSpeed(); will(returnValue(123));
		}});
		InternalCreature c = new InternalCreature(creature, ID, NAME, tile);
		c.setSpeed(100);
		creature.setInternal(c);
		
		assertEquals(1000*123/100, creature.getStepDuration());
	}
	
	@Test
	public void noExceptionWhenNotPlacedStepDuration() {
		context.checking(new Expectations() {{
			allowing(cylinder).getTile(); will(returnValue(null));
		}});
		creature.setInternal(internal);
		internal.setSpeed(100);
		
		creature.getStepDuration();
	}
	
	@Test
	public void reflectsInternalLightObject() {
		creature.setInternal(internal);
		Light light = new Light(13, 37);
		internal.setLight(light);
		assertSame(light, creature.getLight());
	}
	
	@Test
	public void reflectsInternalOutfitObject() {
		creature.setInternal(internal);
		Outfit o = context.mock(Outfit.class);
		internal.setOutfit(o);
		assertSame(o, creature.getOutfit());
	}
	
	@Test
	public void newCreatureHasNoWalkBrain() {
		assertNull(creature.getWalkBrain());
	}
	
	@Test
	public void canAssignWalkBrain() {
		CreatureWalkBrain b = context.mock(CreatureWalkBrain.class);
		creature.walk(b);
		assertSame(b, creature.getWalkBrain());
	}
	
	@Test
	public void canCancelWalkBrain() {
		final CreatureWalkBrain b = context.mock(CreatureWalkBrain.class);
		creature.walk(b);
		
		context.checking(new Expectations() {{
			allowing(b).isCancelled(); will(returnValue(false));
			oneOf(b).cancel();
		}});
		
		assertTrue(creature.cancelWalk());
		assertNull(creature.getWalkBrain());
		context.assertIsSatisfied();
	}
	
	@Test
	public void canReplaceWalkBrain() {
		final CreatureWalkBrain b = context.mock(CreatureWalkBrain.class);
		creature.walk(b);
		
		context.checking(new Expectations() {{
			allowing(b).isCancelled(); will(returnValue(false));
			oneOf(b).cancel();
		}});
		
		CreatureWalkBrain b2 = new CreatureWalkBrain(creature, new DirectionPath(new Position()));
		creature.walk(b2);
		
		assertTrue(creature.cancelWalk());
		assertNull(creature.getWalkBrain());
		context.assertIsSatisfied();
	}
	
	@Test
	public void cancelNonexistingWalkBrain() {
		assertFalse(creature.cancelWalk());
		assertNull(creature.getWalkBrain());
	}
	
	@Test
	public void canSeeSamePosition() {
		final Position pos = new Position();
		context.checking(new Expectations() {{
			allowing(mockedInternal).getPosition(); will(returnValue(pos));
			allowing(mockedInternal).getThing(); will(returnValue(creature));
		}});
		creature.setInternal(mockedInternal);
		
		assertTrue(creature.canSee(pos));
	}
	
	@Test
	public void canSeePositionsSameLevel() {
		final Position pos1 = new Position(100, 100, 7);
		context.checking(new Expectations() {{
			allowing(mockedInternal).getPosition(); will(returnValue(pos1));
			allowing(mockedInternal).getThing(); will(returnValue(creature));
		}});
		creature.setInternal(mockedInternal);
		
		assertTrue(creature.canSee(new Position(105, 105, 7)));
		assertFalse(creature.canSee(new Position(115, 105, 7)));
		assertFalse(creature.canSee(new Position(105, 115, 7)));
		
		assertTrue(creature.canSee(new Position(92, 94, 7)));
		assertFalse(creature.canSee(new Position(91, 94, 7)));
		assertFalse(creature.canSee(new Position(92, 93, 7)));
		
		assertTrue(creature.canSee(new Position(109, 107, 7)));
		assertFalse(creature.canSee(new Position(110, 107, 7)));
		assertFalse(creature.canSee(new Position(109, 108, 7)));
	}
	
	@Test
	public void canSeePositionsOtherLevelsWhenAboveGround() {
		final Position pos1 = new Position(100, 100, 4);
		context.checking(new Expectations() {{
			allowing(mockedInternal).getPosition(); will(returnValue(pos1));
			allowing(mockedInternal).getThing(); will(returnValue(creature));
		}});
		creature.setInternal(mockedInternal);
		
		
		assertTrue(creature.canSee(new Position(92+1, 94+1, 3)));
		assertFalse(creature.canSee(new Position(91+1, 94+1, 3)));
		assertFalse(creature.canSee(new Position(92+1, 93+1, 3)));
		assertTrue(creature.canSee(new Position(109+1, 107+1, 3)));
		assertFalse(creature.canSee(new Position(110+1, 107+1, 3)));
		assertFalse(creature.canSee(new Position(109+1, 108+1, 3)));
		
		assertTrue(creature.canSee(new Position(92+4, 94+4, 0)));
		assertFalse(creature.canSee(new Position(91+4, 94+4, 0)));
		assertFalse(creature.canSee(new Position(92+4, 93+4, 0)));
		assertTrue(creature.canSee(new Position(109+4, 107+4, 0)));
		assertFalse(creature.canSee(new Position(110+4, 107+4, 0)));
		assertFalse(creature.canSee(new Position(109+4, 108+4, 0)));
		
		assertTrue(creature.canSee(new Position(92-3, 94-3, 7)));
		assertFalse(creature.canSee(new Position(91-3, 94-3, 7)));
		assertFalse(creature.canSee(new Position(92-3, 93-3, 7)));
		assertTrue(creature.canSee(new Position(109-3, 107-3, 7)));
		assertFalse(creature.canSee(new Position(110-3, 107-3, 7)));
		assertFalse(creature.canSee(new Position(109-3, 108-3, 7)));
		
		assertFalse(creature.canSee(new Position(100-4, 100-4, 8)));
		assertFalse(creature.canSee(new Position(100-5, 100-5, 9)));
		assertFalse(creature.canSee(new Position(100-6, 100-6, 10)));
		assertFalse(creature.canSee(new Position(100-7, 100-7, 11)));
	}
	
	@Test
	public void canSeePositionsOtherLevelsWhenBelowGround() {
		final Position pos1 = new Position(100, 100, 9);
		context.checking(new Expectations() {{
			allowing(mockedInternal).getPosition(); will(returnValue(pos1));
			allowing(mockedInternal).getThing(); will(returnValue(creature));
		}});
		creature.setInternal(mockedInternal);
		
		
		assertTrue(creature.canSee(new Position(92+1, 94+1, 8)));
		assertFalse(creature.canSee(new Position(91+1, 94+1, 8)));
		assertFalse(creature.canSee(new Position(92+1, 93+1, 8)));
		assertTrue(creature.canSee(new Position(109+1, 107+1, 8)));
		assertFalse(creature.canSee(new Position(110+1, 107+1, 8)));
		assertFalse(creature.canSee(new Position(109+1, 108+1, 8)));
		
		assertTrue(creature.canSee(new Position(92+2, 94+2, 7)));
		assertFalse(creature.canSee(new Position(91+2, 94+2, 7)));
		assertFalse(creature.canSee(new Position(92+2, 93+2, 7)));
		assertTrue(creature.canSee(new Position(109+2, 107+2, 7)));
		assertFalse(creature.canSee(new Position(110+2, 107+2, 7)));
		assertFalse(creature.canSee(new Position(109+2, 108+2, 7)));
		
		assertFalse(creature.canSee(new Position(92+3, 94+3, 6)));
		assertFalse(creature.canSee(new Position(91+3, 94+3, 6)));
		assertFalse(creature.canSee(new Position(92+3, 93+3, 6)));
		assertFalse(creature.canSee(new Position(109+3, 107+3, 6)));
		assertFalse(creature.canSee(new Position(110+3, 107+3, 6)));
		assertFalse(creature.canSee(new Position(109+3, 108+3, 6)));
		
		assertTrue(creature.canSee(new Position(92-2, 94-2, 11)));
		assertFalse(creature.canSee(new Position(91-2, 94-2, 11)));
		assertFalse(creature.canSee(new Position(92-2, 93-2, 11)));
		assertTrue(creature.canSee(new Position(109-2, 107-2, 11)));
		assertFalse(creature.canSee(new Position(110-2, 107-2, 11)));
		assertFalse(creature.canSee(new Position(109-2, 108-2, 11)));
		
		assertFalse(creature.canSee(new Position(92-3, 94-3, 12)));
		assertFalse(creature.canSee(new Position(91-3, 94-3, 12)));
		assertFalse(creature.canSee(new Position(92-3, 93-3, 12)));
		assertFalse(creature.canSee(new Position(109-3, 107-3, 12)));
		assertFalse(creature.canSee(new Position(110-3, 107-3, 12)));
		assertFalse(creature.canSee(new Position(109-3, 108-3, 12)));
	}
	
	@Test
	public void canReachWithinOneSqm() {
		final Thing thing = context.mock(Thing.class);
		final Position creaturePos = new Position(100, 100, 7);
		final Position thingPos = new Position(101, 101, 7);
		context.checking(new Expectations() {{
			allowing(thing).getPosition(); will(returnValue(thingPos));
			allowing(mockedInternal).getPosition(); will(returnValue(creaturePos));
			allowing(mockedInternal).getThing(); will(returnValue(creature));
		}});
		creature.setInternal(mockedInternal);
		assertTrue(creature.canReach(thing));
	}
	
	@Test
	public void canReachOutsideOneSqm() {
		final Thing thing = context.mock(Thing.class);
		final Position creaturePos = new Position(100, 100, 7);
		final Position thingPos = new Position(101, 102, 7);
		context.checking(new Expectations() {{
			allowing(thing).getPosition(); will(returnValue(thingPos));
			allowing(mockedInternal).getPosition(); will(returnValue(creaturePos));
			allowing(mockedInternal).getThing(); will(returnValue(creature));
		}});
		creature.setInternal(mockedInternal);
		assertFalse(creature.canReach(thing));
	}
	
	@Test
	public void canReachOtherFloor() {
		final Thing thing = context.mock(Thing.class);
		final Position creaturePos = new Position(100, 100, 7);
		final Position thingPos = new Position(101, 101, 8);
		context.checking(new Expectations() {{
			allowing(thing).getPosition(); will(returnValue(thingPos));
			allowing(mockedInternal).getPosition(); will(returnValue(creaturePos));
			allowing(mockedInternal).getThing(); will(returnValue(creature));
		}});
		creature.setInternal(mockedInternal);
		assertFalse(creature.canReach(thing));
	}
	
}
