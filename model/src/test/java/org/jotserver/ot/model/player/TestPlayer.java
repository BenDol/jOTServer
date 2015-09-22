package org.jotserver.ot.model.player;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.ot.model.Cylinder;
import org.jotserver.ot.model.Outfit;
import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.map.TestableMap;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.map.Town;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.model.world.GameWorld;
import org.junit.Before;
import org.junit.Test;

public class TestPlayer {

    private static final String NAME = "SomeName";
    private static final int ID = 1337;
    private Player player;
    private Mockery context;
    private GameWorld world;

    @Before
    public void setUp() throws Exception {
        context = new Mockery() {{
            setImposteriser(ClassImposteriser.INSTANCE);
        }};
        world = context.mock(GameWorld.class);
        player = new Player(ID, NAME, world);
    }

    @Test
    public void hasWorld() {
        assertSame(world, player.getWorld());
    }

    @Test
    public void hasInternal() {
        assertNotNull(player.getInternal());
    }

    @Test
    public void hasGlobalIdentifier() {
        assertEquals(ID, player.getGlobalId());
    }

    @Test
    public void newPlayerHasEmptyKnownCreatureCache() {
        assertNotNull(player.getKnownCreaturesCache());
        assertTrue(player.getKnownCreaturesCache().getCreatures().isEmpty());
    }

    @Test
    public void canSetGlobalIdentifier() {
        player.setGlobalId(10);
        assertEquals(10, player.getGlobalId());
    }

    @Test
    public void hasStamina() {
        player.setStamina(1337);
        assertEquals(1337, player.getStamina());
    }

    @Test
    public void hasSkills() {
        assertNotNull(player.getSkills());
        assertEquals(10, player.getSkills().getSkill(Skills.Type.SWORD).getLevel());
        assertEquals(10, player.getSkills().getSkill(Skills.Type.DISTANCE).getLevel());
        assertEquals(10, player.getSkills().getSkill(Skills.Type.FIST).getLevel());
        assertEquals(10, player.getSkills().getSkill(Skills.Type.FISH).getLevel());
        assertEquals(10, player.getSkills().getSkill(Skills.Type.SHIELD).getLevel());
    }

    @Test
    public void hasInventory() {
        assertNotNull(player.getInventory());
    }

    //TODO: Check this. Should there be a dot at the end of the description? o.O
    @Test
    public void descriptionIncludesNameAndLevel() {
        player.getInternal().setLevel(12);
        assertEquals(NAME + " (Level 12).", player.getDescription());
    }

    @Test
    public void hasOutfit() {
        assertNotNull(player.getOutfit());
        Outfit outfit = context.mock(Outfit.class);
        player.setOutfit(outfit);
        assertEquals(outfit, player.getOutfit());
    }

    @Test
    public void hasStance() {
        assertEquals(Stance.BALANCED, player.getStance());
        player.setStance(Stance.OFFENSIVE);
        assertEquals(Stance.OFFENSIVE, player.getStance());
    }

    @Test
    public void hasTown() {
        assertNotNull(player.getTown());
        Town t = new Town();
        player.setTown(t);
        assertSame(t, player.getTown());
    }

    @Test
    public void canAddNewVip() {
        Player vip = new Player(ID+1, NAME+"2", world);
        assertTrue(player.addVip(vip));
        assertTrue(player.isVip(vip));
    }

    @Test
    public void unknownPlayerIsNotVip() {
        Player vip = new Player(ID+1, NAME+"2", world);
        assertFalse(player.isVip(vip));
    }

    @Test
    public void cannotAddVipTwice() {
        Player vip = new Player(ID+1, NAME+"2", world);
        player.addVip(vip);
        assertFalse(player.addVip(vip));
        assertTrue(player.isVip(vip));
    }

    @Test
    public void canRemoveKnownVip() {
        Player vip = new Player(ID+1, NAME+"2", world);
        player.addVip(vip);
        assertTrue(player.removeVip(ID+1));
        assertFalse(player.isVip(vip));
    }

    @Test
    public void cannotRemoveUnknownVip() {
        assertFalse(player.removeVip(ID+1));
    }


    @Test
    public void providesParent() {
        final Cylinder parent = context.mock(Cylinder.class);
        context.checking(new Expectations() {{
            allowing(parent).getTile(); will(returnValue(null));
        }});
        player.setParent(parent);
        assertSame(parent, player.getParent());
    }

    @Test
    public void playerCanNotReportBugs() {
        assertFalse(player.canReportBugs());
    }

    @Test
    public void canLookAtThings() {
        Thing thing = context.mock(Thing.class);
        assertTrue(player.lookAt(thing));
    }

    @Test
    public void providesInternalCapacity() {
        player.getInternal().setCapacity(123);
        assertEquals(123, player.getFreeCapacity());
    }

    @Test
    public void providesInternalExperience() {
        player.getInternal().setExperience(1337);
        assertEquals(1337, player.getExperience());
    }

    @Test
    public void providesInternalManaAndMaxMana() {
        player.getInternal().setMana(123);
        player.getInternal().setMaxMana(456);
        assertEquals(123, player.getMana());
        assertEquals(456, player.getMaxMana());
    }

    @Test
    public void providesInternalMagicLevel() {
        player.getInternal().setMagicLevel(56);
        assertEquals(56, player.getMagicLevel());
    }

    @Test
    public void providesInternalSoul() {
        player.getInternal().setSoul(123);
        assertEquals(123, player.getSoul());
    }

    @Test
    public void temporaryPositionIsAlwaysThePositionOfTheLastTile() {
        TestableMap map = new TestableMap();
        Position pos = new Position(123, 456, 7);
        Tile tile = new Tile(map, pos);
        player.setParent(tile);
        assertEquals(pos, player.getTemporaryPosition());
    }

    @Test
    public void temporaryPositionIsSavedWhenRemovedFromTile() {
        TestableMap map = new TestableMap();
        Position pos = new Position(123, 456, 7);
        Tile tile = new Tile(map, pos);
        player.setParent(tile);
        player.setParent(null);
        assertEquals(pos, player.getTemporaryPosition());
    }

}
