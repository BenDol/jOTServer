package org.jotserver.ot.model.item;


import static org.junit.Assert.*;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.action.ErrorType;
import org.junit.Before;
import org.junit.Test;

public class TestStackable {

	private static final int COUNT = 56;
	private Mockery context;
	private ItemType type;
	private Stackable stackable;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		
		type = new ItemType();
		stackable = new Stackable(type, COUNT);
		
	}
	
	@Test
	public void hasInternalStackable() {
		assertNotNull(stackable.getInternal());
	}
	
	@Test
	public void allowsQueryAddValidCount() {
		assertEquals(ErrorType.NONE, stackable.queryAddCount(1));
	}
	
	@Test
	public void doesNotAllowQueryAddOverflow() {
		assertEquals(ErrorType.NOTENOUGHROOM, stackable.queryAddCount(100-COUNT+1));
	}
	
	@Test
	public void allowsQueryAddToFull() {
		assertEquals(ErrorType.NONE, stackable.queryAddCount(100-COUNT));
	}
	
	@Test
	public void negativeCountIsNotAllowed() {
		assertEquals(ErrorType.NOTPOSSIBLE, stackable.queryAddCount(-1));
	}
	
	@Test
	public void addNothingIsAccepted() {
		assertEquals(ErrorType.NONE, stackable.queryAddCount(0));
	}
	
	@Test
	public void executeAddCountUpdatesCountCorrectly() {
		stackable.executeAddCount(1);
		assertEquals(COUNT+1, stackable.getCount());
		stackable.executeAddCount(9);
		assertEquals(COUNT+10, stackable.getCount());
	}
	
	@Test
	public void queryAddStackableAcceptsItemsOfSameType() {
		ItemType other = new ItemType();
		other.serverId = 123;
		type.serverId = 123;
		Stackable stackable2 = new Stackable(other, 10);
		assertEquals(ErrorType.NONE, stackable.queryAdd(stackable2));
	}
	
	@Test
	public void queryAddStackableAcceptsOnlyItemsOfSameType() {
		ItemType other = new ItemType();
		other.serverId = 123;
		type.serverId = 124;
		Stackable stackable2 = new Stackable(other, 10);
		assertEquals(ErrorType.NOTPOSSIBLE, stackable.queryAdd(stackable2));
	}
	
	@Test
	public void queryAddStackableOverflow() {
		ItemType other = new ItemType();
		other.serverId = 123;
		type.serverId = 123;
		Stackable stackable2 = new Stackable(other, 50);
		assertEquals(ErrorType.NOTENOUGHROOM, stackable.queryAdd(stackable2));
	}
	
	@Test
	public void executeAddStackableUpdatesCount() {
		ItemType other = new ItemType();
		other.serverId = 123;
		type.serverId = 123;
		Stackable stackable2 = new Stackable(other, 10);
		stackable.executeAdd(stackable2);
		assertEquals(COUNT+10, stackable.getCount());
	}
	
	@Test
	public void queryRemoveValidCount() {
		assertEquals(ErrorType.NONE, stackable.queryRemoveCount(1));
	}
	
	@Test
	public void queryRemoveAllCountIsNotAccepted() {
		assertEquals(ErrorType.NOTPOSSIBLE, stackable.queryRemoveCount(stackable.getCount()));
	}
	
	@Test
	public void queryRemoveOverflowIsNotAccepted() {
		assertEquals(ErrorType.NOTPOSSIBLE, stackable.queryRemoveCount(100));
	}
	
	@Test
	public void queryRemoveMaxCount() {
		assertEquals(ErrorType.NONE, stackable.queryRemoveCount(stackable.getCount()-1));
	}
	
	@Test
	public void queryRemoveNegativeCountIsNotAccepted() {
		assertEquals(ErrorType.NOTPOSSIBLE, stackable.queryRemoveCount(-1));
	}
	
	@Test
	public void queryRemoveZeroCountIsAccepted() {
		assertEquals(ErrorType.NONE, stackable.queryRemoveCount(0));
	}
	
	@Test
	public void executeRemoveCountUpdatesCount() {
		stackable.executeRemoveCount(10);
		assertEquals(COUNT-10, stackable.getCount());
	}
	
	@Test
	public void clonedStackableHasCorrectCount() {
		Stackable s = stackable.clone(10);
		assertEquals(10, s.getCount());
	}
	
	@Test
	public void clonedStackableHasSameType() {
		Stackable s = stackable.clone(10);
		assertSame(stackable.getType(), s.getType());
	}
	
	@Test
	public void singleStackableHasSameDescriptionAsNonStackable() {
		type.article = "SomeArticle";
		type.name = "SomeName";
		type.pluralName = "SomeNames";
		Item item = new Item(type);
		Stackable s = new Stackable(type, 1);
		assertEquals(item.getDescription(), s.getDescription());
	}
	
	@Test
	public void multicountStackableDescription() {
		type.article = "SomeArticle";
		type.name = "SomeName";
		type.pluralName = "SomeNames";
		Stackable s = new Stackable(type, 2);
		assertEquals(s.getCount() + " " + type.pluralName, s.getDescription());
	}
	
}
