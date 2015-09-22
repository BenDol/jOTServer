package org.jotserver.ot.model.player;


import static org.junit.Assert.*;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

public class TestInternalPlayer {

    private Player player;
    private InternalPlayer internal;
    private Mockery context;

    @Before
    public void setUp() throws Exception {
        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};
        player = context.mock(Player.class);
        internal = new InternalPlayer(player, 1337, "SomeName", null);
    }

    @Test
    public void hasPlayer() {
        assertSame(player, internal.getPlayer());
    }

    @Test
    public void hasSkills() {
        assertNotNull(internal.getSkills());
        Skills skills = new Skills();
        internal.setSkills(skills);
        assertSame(skills, internal.getSkills());
    }

    @Test
    public void hasLevel() {
        internal.setLevel(1337);
        assertEquals(1337, internal.getLevel());
    }

    @Test
    public void hasExperience() {
        internal.setExperience(1337);
        assertEquals(1337, internal.getExperience());
    }

    @Test
    public void hasCapacity() {
        internal.setCapacity(1337);
        assertEquals(1337, internal.getCapacity());
    }

    @Test
    public void hasManaAndMaxMana() {
        internal.setMana(1337);
        assertEquals(1337, internal.getMana());

        internal.setMaxMana(1338);
        assertEquals(1338, internal.getMaxMana());
    }

    @Test
    public void hasMagicLevel() {
        internal.setMagicLevel(1337);
        assertEquals(1337, internal.getMagicLevel());
    }

    @Test
    public void hasSoul() {
        internal.setSoul(1337);
        assertEquals(1337, internal.getSoul());
    }

    @Test
    public void canAddVip() {
        Player p1 = new Player(1, "SomeVip", null);
        internal.addVip(p1);
        assertTrue(internal.getVipList().contains(p1));
    }

    @Test
    public void removeKnownVip() {
        Player p1 = new Player(1, "SomeVip", null);
        internal.addVip(p1);
        internal.removeVip(p1);
        assertFalse(internal.getVipList().contains(p1));
    }

    @Test
    public void checkIfVip() {
        Player p1 = new Player(1, "SomeVip", null);

        assertFalse(internal.isVip(1));
        assertFalse(internal.isVip("SomeVip"));

        internal.addVip(p1);

        assertTrue(internal.isVip(1));
        assertTrue(internal.isVip("SomeVip"));
    }

}
