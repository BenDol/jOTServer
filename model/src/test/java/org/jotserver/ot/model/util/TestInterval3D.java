package org.jotserver.ot.model.util;


import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestInterval3D {

    private static final int X1 = 10;
    private static final int Y1 = 20;
    private static final int Z1 = 30;

    private static final int X2 = 100;
    private static final int Y2 = 200;
    private static final int Z2 = 300;

    private Interval3D interval;

    @Before
    public void setUp() throws Exception {
        interval = new Interval3D(X1, Y1, Z1, X2, Y2, Z2);
    }

    @Test
    public void hasBounds() {
        assertEquals(X1, interval.getStartX());
        assertEquals(Y1, interval.getStartY());
        assertEquals(Z1, interval.getStartZ());
        assertEquals(X2, interval.getEndX());
        assertEquals(Y2, interval.getEndY());
        assertEquals(Z2, interval.getEndZ());
    }

    @Test
    public void providesPositionBounds() {
        Position start = new Position(X1, Y1, Z1);
        Position end = new Position(X2, Y2, Z2);

        assertEquals(start, interval.getStart());
        assertEquals(end, interval.getEnd());
    }

    @Test
    public void provides2DSlice() {
        Interval2D slice = interval.get2D();
        assertEquals(X1, slice.getStartX());
        assertEquals(Y1, slice.getStartY());
        assertEquals(X2, slice.getEndX());
        assertEquals(Y2, slice.getEndY());
    }

}
