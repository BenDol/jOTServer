package org.jotserver.ot.model.creature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.action.Action;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.chat.PrivateMessageChannel;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Position;
import org.junit.Before;
import org.junit.Test;

public class TestCreatureWalkBrain {

	private Mockery context;
	private CreatureWalkBrain b;
	private Creature creature;
	private Position origin;
	private DirectionPath small;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		creature = context.mock(Creature.class);
		origin = new Position(12, 34, 7);
		small = new DirectionPath(origin, Arrays.asList(Direction.NORTH, Direction.SOUTH));
	}
	
	@Test
	public void canPerformActions() {
		final Action action = context.mock(Action.class);
		b = new CreatureWalkBrain(null, null);
		b.addAction(action);
		context.checking(new Expectations() {{
			oneOf(action).execute(); will(returnValue(true));
		}});
		b.performActions();
		context.assertIsSatisfied();
	}
	
	@Test
	public void delayEqualsStepDuration() {
		context.checking(new Expectations() {{
			allowing(creature).getStepDuration(); will(returnValue(123));
		}});
		b = new CreatureWalkBrain(creature, null);
		assertEquals(123, b.getDelay());
	}
	
	@Test
	public void shouldCancelWhenNoPathIsSupplied() {
		b = new CreatureWalkBrain(creature, null);
		assertTrue(b.shouldCancel());
	}
	
	@Test
	public void shouldCancelIfCreatureIsNotPlaced() {
		b = new CreatureWalkBrain(creature, small);
		context.checking(new Expectations() {{
			allowing(creature).isPlaced(); will(returnValue(false));
		}});
		assertTrue(b.shouldCancel());
	}
	
	@Test
	public void shouldCancelIfPathIsEmpty() {
		b = new CreatureWalkBrain(creature, Path.EMPTY);
		context.checking(new Expectations() {{
			allowing(creature).isPlaced(); will(returnValue(true));
		}});
		assertTrue(b.shouldCancel());
	}
	
	@Test
	public void shouldCancelIfPositionIsIncorrect() {
		b = new CreatureWalkBrain(creature, small);
		context.checking(new Expectations() {{
			allowing(creature).isPlaced(); will(returnValue(true));
			allowing(creature).getPosition(); will(returnValue(origin.add(Direction.NORTH)));
		}});
		assertTrue(b.shouldCancel());
	}
	
	@Test
	public void shouldNotCancelWhenValidAndNotFinished() {
		b = new CreatureWalkBrain(creature, small);
		context.checking(new Expectations() {{
			allowing(creature).isPlaced(); will(returnValue(true));
			allowing(creature).getPosition(); will(returnValue(origin));
		}});
		assertFalse(b.shouldCancel());
	}
	
	@Test
	public void shouldNotCancelWhenValidAndActionsRemain() {
		b = new CreatureWalkBrain(creature, new DirectionPath(origin));
		final Action action = context.mock(Action.class);
		b.addAction(action);
		context.checking(new Expectations() {{
			allowing(creature).isPlaced(); will(returnValue(true));
			allowing(creature).getPosition(); will(returnValue(origin));
		}});
		assertFalse(b.shouldCancel());
	}
	
	@Test
	public void reportsErrorsToPrivateChatAndCancelsBrain() {
		final PrivateMessageChannel c = context.mock(PrivateMessageChannel.class);
		b = new CreatureWalkBrain(creature, small);
		context.checking(new Expectations() {{
			allowing(creature).getPrivateChannel(); will(returnValue(c));
			oneOf(c).sendCancel(ErrorType.NOTPOSSIBLE);
		}});
		b.reportError(ErrorType.NOTPOSSIBLE);
		assertTrue(b.isCancelled());
		context.assertIsSatisfied();
	}
	
	@Test
	public void canTakeStep() {
		b = new CreatureWalkBrain(creature, small);
		final Tile tile = context.mock(Tile.class);
		context.checking(new Expectations() {{
			allowing(creature).getTile(); will(returnValue(tile));
			oneOf(tile).queryMoveCreature(creature, Direction.NORTH); will(returnValue(ErrorType.NONE));
			oneOf(tile).executeMoveCreature(creature, Direction.NORTH);
		}});
		b.nextStep();
		context.assertIsSatisfied();
	}
	
}
