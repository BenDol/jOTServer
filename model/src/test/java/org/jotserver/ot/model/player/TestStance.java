package org.jotserver.ot.model.player;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestStance {

    @Test
    public void balancedOffensiveMultiplier() {
        assertEquals(0.8f, Stance.BALANCED.getOffensiveMultiplier(), 0.0001f);
    }

    @Test
    public void balancedDefensiveMultiplier() {
        assertEquals(1.2f, Stance.BALANCED.getDefensiveMultiplier(), 0.0001f);
    }


    @Test
    public void offensiveOffensiveMultiplier() {
        assertEquals(1.0f, Stance.OFFENSIVE.getOffensiveMultiplier(), 0.0001f);
    }

    @Test
    public void offensiveDefensiveMultiplier() {
        assertEquals(1.0f, Stance.OFFENSIVE.getDefensiveMultiplier(), 0.0001f);
    }


    @Test
    public void defensiveOffensiveMultiplier() {
        assertEquals(0.5f, Stance.DEFENSIVE.getOffensiveMultiplier(), 0.0001f);
    }

    @Test
    public void defensiveDefensiveMultiplier() {
        assertEquals(2.0f, Stance.DEFENSIVE.getDefensiveMultiplier(), 0.0001f);
    }
}
