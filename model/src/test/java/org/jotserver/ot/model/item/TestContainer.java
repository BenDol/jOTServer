package org.jotserver.ot.model.item;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.action.ErrorType;
import org.jotserver.ot.model.creature.Creature;
import org.jotserver.ot.model.map.Spectators;
import org.jotserver.ot.model.util.ItemLocation;
import org.jotserver.ot.model.util.Location;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestContainer {

    private Mockery context;
    private Container container;
    private ItemType type;
    private Creature creature;

    @Before
    public void setUp() throws Exception {
        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};
        type = new ItemType();
        type.group = ItemType.Group.CONTAINER;
        type.containerSize = 2;
        container = new Container(type);

        creature = context.mock(Creature.class);
    }

    @Test
    public void hasInternalContainer() {
        assertNotNull(container.getInternal());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateNonContainerContainer() {
        type.group = ItemType.Group.NONE;
        new Container(type);
    }

    @Test
    public void providesContentsSpectators() {
        container.getInternal().open(creature);
        Spectators<Creature> s = container.getContentsSpectators(Creature.class);
        Iterator<Creature> it = s.iterator();
        assertSame(creature, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void willOpenAndCloseWhenUsed() {
        assertFalse(container.isContentsSpectator(creature));
        container.executeUse(creature);
        assertTrue(container.isContentsSpectator(creature));
        container.executeUse(creature);
        assertFalse(container.isContentsSpectator(creature));
    }

    @Test
    public void impossibleToAddSelf() {
        assertTrue(container.isImpossibleToAdd(container));
    }

    @Test
    public void impossibleToAddParent() {
        Container c2 = new Container(type);
        c2.executeAddItem(container);
        assertTrue(container.isImpossibleToAdd(c2));
    }

    @Test
    public void notImpossibleToAddOtherItem() {
        Item item = new Item(new ItemType());
        assertFalse(container.isImpossibleToAdd(item));
    }

    @Test
    public void queryAddItemWhenImpossible() {
        Container c2 = new Container(type);
        c2.executeAddItem(container);
        assertEquals(ErrorType.IMPOSSIBLE, container.queryAddItem(container));
    }

    @Test
    public void queryAddItemWhenFull() {
        container.executeAddItem(new Item(new ItemType()));
        container.executeAddItem(new Item(new ItemType()));
        assertEquals(ErrorType.NOTENOUGHROOM, container.queryAddItem(new Item(new ItemType())));
    }

    @Test
    public void isFullAndFreeCapacity() {
        type.containerSize = 2;
        assertFalse(container.isFull());
        assertEquals(2, container.getFreeCapacity());
        container.executeAddItem(new Item(new ItemType()));
        assertFalse(container.isFull());
        assertEquals(1, container.getFreeCapacity());
        container.executeAddItem(new Item(new ItemType()));
        assertTrue(container.isFull());
        assertEquals(0, container.getFreeCapacity());
        assertEquals(ErrorType.NOTENOUGHROOM, container.queryAddItem(new Item(new ItemType())));
    }

    @Test
    public void queryAddItemMustBePickupable() {
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, false);
        assertEquals(ErrorType.CANNOTPICKUP, container.queryAddItem(new Item(type)));
    }

    @Test
    public void queryAddValidItem() {
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, true);
        assertEquals(ErrorType.NONE, container.queryAddItem(new Item(type)));
    }

    @Test
    public void queryAddItemBySlotWithValidItemWithoutMerging() {
        container.executeAddItem(new Item(new ItemType()));
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, true);
        assertEquals(ErrorType.NONE, container.queryAddItem(new Item(type), 1));
    }

    @Test
    public void queryAddItemBySlotWithInvalidItemWithoutMerging() {
        container.executeAddItem(new Item(new ItemType()));
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, false);
        assertEquals(ErrorType.CANNOTPICKUP, container.queryAddItem(new Item(type), 1));
    }

    @Test
    public void executeAddItemBySlotAddsItemFirstWhenNotMerging() {
        container.executeAddItem(new Item(new ItemType()));
        Item item = new Item(new ItemType());
        container.executeAddItem(item, 1);
        assertSame(item, container.getItem(0));
    }

    @Test
    public void queryRemoveItemAcceptsOnlyContainedItems() {
        Item item = new Item(new ItemType());
        assertEquals(ErrorType.NOTPOSSIBLE, container.queryRemoveItem(item));
        container.executeAddItem(item);
        assertEquals(ErrorType.NONE, container.queryRemoveItem(item));
    }

    @Test
    public void executeRemoveItemRemovesItem() {
        Item i1 = new Item(new ItemType());
        Item i2 = new Item(new ItemType());
        container.executeAddItem(i1);
        container.executeAddItem(i2);
        container.executeRemoveItem(i2);
        assertEquals(1, container.getItemCount());
        assertSame(i1, container.getItem(0));
    }

    @Test
    public void viewabilityRequiresCylinderVisibilityAndReachability() {
        final Cylinder cylinder = context.mock(Cylinder.class);
        container.setParent(cylinder);
        context.checking(new Expectations() {{
            allowing(creature).canReach(container); will(returnValue(true));
            allowing(cylinder).isVisibleTo(creature); will(returnValue(true));
        }});
        assertTrue(container.canBeViewedBy(creature));
    }

    @Test
    public void viewabilityRequiresReachability() {
        final Cylinder cylinder = context.mock(Cylinder.class);
        container.setParent(cylinder);
        context.checking(new Expectations() {{
            allowing(creature).canReach(container); will(returnValue(false));
            allowing(cylinder).isVisibleTo(creature); will(returnValue(true));
        }});
        assertFalse(container.canBeViewedBy(creature));
    }

    @Test
    public void viewabilityRequiresCylinderVisibility() {
        final Cylinder cylinder = context.mock(Cylinder.class);
        container.setParent(cylinder);
        context.checking(new Expectations() {{
            allowing(creature).canReach(container); will(returnValue(true));
            allowing(cylinder).isVisibleTo(creature); will(returnValue(false));
        }});
        assertFalse(container.canBeViewedBy(creature));
    }

    @Test
    public void hasItemFindsItem() {
        Item item = new Item(new ItemType());
        assertFalse(container.hasItem(item));
        container.executeAddItem(item);
        assertTrue(container.hasItem(item));
    }

    @Test
    public void getLocationOfReturnsNullForNonItemThings() {
        assertNull(container.getLocationOf(creature));
    }

    @Test
    public void getLocationOfProvidesLocationForItems() {
        Item item = new Item(new ItemType());
        container.executeAddItem(item);
        ItemLocation loc = container.getLocationOf(item);
        assertNotNull(loc);
        assertSame(item, loc.get());
    }

    @Test
    public void getDescriptionDisplaysItemDescriptionAndVolume() {
        Item item = new Item(type);
        type.name = "SomeName";
        type.containerSize = 10;
        assertEquals(
                item.getDescription() + " (Vol: " + type.containerSize + ")",
                container.getDescription());
    }

    @Test
    public void recursivelyCallsOnChangeParentHandlerOnContainedItems() {
        final Item item = context.mock(Item.class);
        context.checking(new Expectations() {{
            oneOf(item).onChangeParent();
            allowing(item).setParent(container);
        }});
        container.executeAddItem(item);
        container.onChangeParent();
        context.assertIsSatisfied();
    }

    @Test
    public void canCloseContainerManually() {
        container.executeUse(creature);
        container.close(creature);
        assertFalse(container.isContentsSpectator(creature));
    }

    @Test
    public void providesItemList() {
        Item item = new Item(new ItemType());
        container.executeAddItem(item);
        List<Item> items = container.getItems();
        assertTrue(items.contains(item));
        assertSame(item, items.get(0));
        assertEquals(1, items.size());
    }

    @Test
    public void providesSlotLocation() {
        Item item = new Item(new ItemType());
        container.executeAddItem(item);
        ItemLocation loc = container.getSlotLocation(0);
        assertNotNull(loc);
        assertSame(item, loc.get());
    }

    @Test
    public void queryAddItemOntoOtherItemNotStackableNotContainer() {
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, true);
        Item item1 = new Item(type);
        Item item2 = new Item(type);
        container.executeAddItem(item1);
        assertEquals(ErrorType.NONE, container.queryAddItem(item2, 0));
    }

    @Test
    public void queryAddItemOntoOtherItemStackable() {
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, true);
        type.serverId = 1337;
        type.addAttribute(ItemAttribute.STACKABLE);
        Stackable s1 = new Stackable(type, 50);
        Stackable s2 = new Stackable(type, 50);
        container.executeAddItem(s1);
        container.executeAddItem(new Item(new ItemType()));
        assertEquals(ErrorType.NONE, container.queryAddItem(s2, 1));
    }

    @Test
    public void queryAddItemOntoOtherItemStackableOverflow() {
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, true);
        type.serverId = 1337;
        type.addAttribute(ItemAttribute.STACKABLE);
        Stackable s1 = new Stackable(type, 50);
        Stackable s2 = new Stackable(type, 51);
        container.executeAddItem(s1);
        container.executeAddItem(new Item(new ItemType()));
        assertEquals(ErrorType.NOTENOUGHROOM, container.queryAddItem(s2, 1));
    }

    @Test
    public void queryAddItemOntoOtherItemContainerNotFull() {
        Container c = new Container(type);
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, true);
        Item item = new Item(type);
        container.executeAddItem(c);
        container.executeAddItem(new Item(new ItemType()));
        assertEquals(ErrorType.NONE, container.queryAddItem(item, 1));
    }

    @Test
    public void queryAddItemOntoOtherItemContainerFull() {
        Container c = new Container(type);
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, true);
        Item item = new Item(type);
        container.executeAddItem(c);
        container.executeAddItem(new Item(new ItemType()));
        c.executeAddItem(new Item(new ItemType()));
        c.executeAddItem(new Item(new ItemType()));
        assertEquals(ErrorType.NOTENOUGHROOM, container.queryAddItem(item, 1));
    }

    @Test
    public void executeAddItemOntoOtherItemStackable() {
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, true);
        type.serverId = 1337;
        type.addAttribute(ItemAttribute.STACKABLE);
        Stackable s1 = new Stackable(type, 50);
        Stackable s2 = new Stackable(type, 50);
        container.executeAddItem(s1);
        container.executeAddItem(new Item(new ItemType()));
        container.executeAddItem(s2, 1);
        assertEquals(100, s1.getCount());
        assertEquals(2, container.getItemCount());
    }

    @Ignore
    @Test
    public void executeAddItemOntoOtherItemStackableOverflow() {
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, true);
        type.serverId = 1337;
        type.addAttribute(ItemAttribute.STACKABLE);
        Stackable s1 = new Stackable(type, 50);
        Stackable s2 = new Stackable(type, 51);
        container.executeAddItem(s1);
        container.executeAddItem(s2, 0);
        assertEquals(100, s1.getCount());
        assertEquals(2, container.getItemCount());
    }

    @Test
    public void executeAddItemOntoOtherItemContainerNotFull() {
        Container c = new Container(type);
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.PICKUPABLE, true);
        Item item = new Item(type);
        container.executeAddItem(c);
        container.executeAddItem(new Item(new ItemType()));
        container.executeAddItem(item, 1);
        assertTrue(c.hasItem(item));
        assertEquals(1, c.getItemCount());
    }

}
