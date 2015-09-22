package org.jotserver.ot.model.player;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class TestPremium {

    private static final int DELTA = 100;
    private static final int DAY = 24*60*60*1000;
    private Premium prem;

    @Before
    public void setUp() throws Exception {
    }


    @Test
    public void newPremiumHasEnded() {
        prem = new Premium();
        assertTrue(prem.hasEnded());
    }

    @Test
    public void endedPremiumHasNotimeLeft() {
        prem = new Premium();
        assertEquals(0, prem.getDaysLeft());
        assertEquals(0, prem.getMilliSecondsLeft());
    }

    @Test
    public void premiumWithTimeLeftHasNotEnded() {
        long ms = System.currentTimeMillis()+DAY+DELTA;
        prem = new Premium(ms);
        assertFalse(prem.hasEnded());
    }

    @Test
    public void premiumWithTimeLeftHasCorrectDaysLeft() {
        long ms = System.currentTimeMillis()+DAY+DELTA;
        prem = new Premium(ms);
        assertEquals(1, prem.getDaysLeft());
    }

    @Test
    public void premiumWithTimeLeftHasCorrectMilliSecondsLeft() {
        long ms = System.currentTimeMillis()+DAY+DELTA;
        prem = new Premium(ms);
        assertTrue(prem.getMilliSecondsLeft() >= DAY);
        assertTrue(prem.getMilliSecondsLeft() <= DAY+DELTA);
    }

    @Test
    public void providesCorrectEndDate() {
        long ms = System.currentTimeMillis()+DAY+DELTA;
        Date d = new Date(ms);
        prem = new Premium(d);
        assertEquals(d, prem.getEnd());
    }

    @Test
    public void canChangeEndTime() {
        prem = new Premium();
        long ms = System.currentTimeMillis()+DAY+DELTA;
        Date d = new Date(ms);
        prem.setEnd(d);
        assertEquals(d, prem.getEnd());
    }

    @Test
    public void canChangeEndTimeMilliSeconds() {
        prem = new Premium();
        long ms = System.currentTimeMillis()+DAY+DELTA;
        Date d = new Date(ms);
        prem.setEnd(ms);
        assertEquals(d, prem.getEnd());
    }

    @Test
    public void canAddTimeInMilliSecondsToEndedPremium() {
        prem = new Premium();
        prem.addMilliSeconds(10*DAY+DELTA);
        assertEquals(10, prem.getDaysLeft());
    }

    @Test
    public void canAddDaysOnEndedPremium() throws InterruptedException {
        prem = new Premium();
        prem.addDays(10);
        prem.addMilliSeconds(DELTA);
        assertEquals(10, prem.getDaysLeft());
    }

    @Test
    public void canAddDaysOnNotEndedPremium() {
        long ms = System.currentTimeMillis()+DAY+DELTA;
        prem = new Premium(ms);
        prem.addDays(10);
        assertEquals(11, prem.getDaysLeft());
    }

    @Test
    public void canAddTimeInMilliSecondsToNotEndedPremium() {
        long ms = System.currentTimeMillis()+DAY+DELTA;
        prem = new Premium(ms);
        prem.addMilliSeconds(DAY);
        assertEquals(2, prem.getDaysLeft());
    }

}
