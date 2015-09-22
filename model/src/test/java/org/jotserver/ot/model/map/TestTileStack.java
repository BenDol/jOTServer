package org.jotserver.ot.model.map;

import static org.junit.Assert.assertEquals;

import org.jotserver.ot.model.item.BaseItemTypeAccessor;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.ItemType;
import org.jotserver.ot.model.item.TestItemProvider;
import org.junit.Before;
import org.junit.Test;

public class TestTileStack {

    private TileStack stack;
    private BaseItemTypeAccessor items;

    @Before
    public void setUp() throws Exception {
        stack = new TileStack();
        items = new BaseItemTypeAccessor(10);
    }

    @Test
    public void stackIsEmpty() {
        assertEquals(0, stack.size());
        assertEquals(0, stack.getTopItemCount());
        assertEquals(0, stack.getDownItemCount());
        assertEquals(0, stack.getCreatureCount());
    }

    @Test
    public void addGround() {
        Item ground = items.createItem(TestItemProvider.getGrassType());
        stack.setGround(ground);
        assertEquals(1, stack.size());
        assertEquals(0, stack.getTopItemCount());
        assertEquals(0, stack.getDownItemCount());
        assertEquals(0, stack.getCreatureCount());

        stack.setGround(ground);
        assertEquals(1, stack.size());
        assertEquals(0, stack.getTopItemCount());
        assertEquals(0, stack.getDownItemCount());
        assertEquals(0, stack.getCreatureCount());
    }

}
