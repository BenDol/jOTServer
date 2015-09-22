package org.jotserver.ot.model.player;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestInventorySlot {

    @Test
    public void canLookupByIntegerIdentifier() {
        assertEquals(InventorySlot.HEAD, InventorySlot.getSlot(1));
        assertEquals(InventorySlot.NECKLACE, InventorySlot.getSlot(2));
        assertEquals(InventorySlot.BACKPACK, InventorySlot.getSlot(3));
        assertEquals(InventorySlot.ARMOR, InventorySlot.getSlot(4));
        assertEquals(InventorySlot.RIGHT, InventorySlot.getSlot(5));
        assertEquals(InventorySlot.LEFT, InventorySlot.getSlot(6));
        assertEquals(InventorySlot.LEGS, InventorySlot.getSlot(7));
        assertEquals(InventorySlot.FEET, InventorySlot.getSlot(8));
        assertEquals(InventorySlot.RING, InventorySlot.getSlot(9));
        assertEquals(InventorySlot.AMMO, InventorySlot.getSlot(10));
    }

    @Test
    public void lookingUpUnknownIdReturnsNull() {
        assertNull(InventorySlot.getSlot(1337));
    }
}
