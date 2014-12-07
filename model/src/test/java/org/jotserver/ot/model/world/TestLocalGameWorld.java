package org.jotserver.ot.model.world;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledExecutorService;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jotserver.configuration.ConfigurationAccessor;
import org.jotserver.ot.model.OutfitAccessor;
import org.jotserver.ot.model.chat.ChatManager;
import org.jotserver.ot.model.item.ItemTypeAccessor;
import org.jotserver.ot.model.map.Map;
import org.jotserver.ot.model.map.MapAccessor;
import org.jotserver.ot.model.map.TestableMap;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.player.PlayerDataAccessor;
import org.junit.Before;
import org.junit.Test;

public class TestLocalGameWorld {

	private static final int PORT = 7171;
	private static final String HOST = "localhost";
	private static final String ID = "someworld";
	private static final String NAME = "SomeWorld";
	
	private LocalGameWorld world;
	private Mockery context;
	private ScheduledExecutorService executor;
	private InetSocketAddress address;
	private BaseGameWorld base;
	private GameWorldConfiguration conf;
	private ConfigurationAccessor gconf;

	@Before
	public void setUp() throws Exception {
		context = new Mockery() {{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		executor = context.mock(ScheduledExecutorService.class);
		
		address = new InetSocketAddress(HOST, PORT);
		base = new BaseGameWorld(ID, NAME, address, true);
		
		gconf = context.mock(ConfigurationAccessor.class);
		conf = context.mock(GameWorldConfiguration.class);
		
		world = new LocalGameWorld(executor, base, gconf, conf);
	}
	
	@Test
	public void defaultStateIsLoading() {
		assertEquals(WorldState.LODAING, world.getState());
		assertFalse(world.isRunning());
	}
	
	@Test
	public void canChangeState() {
		world.setState(WorldState.RUNNING);
		assertEquals(WorldState.RUNNING, world.getState());
		assertTrue(world.isRunning());
	}
	
	@Test
	public void newWorldHasNoPlayers() {
		assertTrue(world.getPlayers().isEmpty());
	}
	
	@Test
	public void canAddPlayer() {
		Player player = new Player(1337, "SomePlayer", world);
		world.addPlayer(player);
		assertTrue(world.getPlayers().contains(player));
	}
	
	@Test
	public void canFindPlayerByGlobalIdentifier() {
		Player player = new Player(1337, "SomePlayer", world);
		world.addPlayer(player);
		assertEquals(player, world.getPlayerByGlobalId(1337));
	}
	
	@Test
	public void canFindPlayerByName() {
		Player player = new Player(1337, "SomePlayer", world);
		world.addPlayer(player);
		assertEquals(player, world.getPlayerByName("SomePlayer"));
	}
	
	@Test
	public void unknownPlayerByGlobalIdentifierReturnsNull() {
		assertNull(world.getPlayerByGlobalId(1337));
	}
	
	@Test
	public void unknownPlayerByNameReturnsNull() {
		assertNull(world.getPlayerByName("SomePlayer"));
	}
	
	@Test
	public void canRemovePlayer() {
		Player player = new Player(1337, "SomePlayer", world);
		world.addPlayer(player);
		world.removePlayer(player);
		assertFalse(world.getPlayers().contains(player));
		assertTrue(world.getPlayers().isEmpty());
	}
	
	@Test
	public void providesBaseAddress() {
		assertEquals(address, world.getAddress());
	}
	
	@Test
	public void providesBaseIdentifier() {
		assertEquals(ID, world.getIdentifier());
	}
	
	@Test
	public void providesBaseName() {
		assertEquals(NAME, world.getName());
	}
	
	
	@Test
	public void mapIsNullWhenNotInitialized() {
		assertNull(world.getMap());
	}
	
	@Test
	public void localGameWorldIsLocal() {
		assertTrue(world.isLocal());
	}
	
	@Test
	public void hasLight() {
		assertNotNull(world.getLight());
	}
	
	@Test
	public void hasEmptyChatManager() {
		ChatManager chat = world.getChatManager();
		assertNotNull(chat);
		assertTrue(chat.getPublicChannels().isEmpty());
	}
	
	@Test
	public void providesDispatcher() {
		assertNotNull(world.getDispatcher());
	}
	
	@Test
	public void providesConfiguration() {
		assertEquals(conf, world.getConfiguration());
	}
	
	@Test
	public void providesGlobalConfiguration() {
		assertEquals(gconf, world.getGlobalConfiguration());
	}
	
	@Test
	public void providesGlobalPlayerAccessor() {
		final PlayerAccessor players = context.mock(PlayerAccessor.class);
		context.checking(new Expectations() {{
			oneOf(gconf).getPlayerAccessor(); will(returnValue(players));
		}});
		assertEquals(players, world.getPlayerAccessor());
		context.assertIsSatisfied();
	}
	
	@Test
	public void providesConfigurationPlayerDataAccessor() {
		final PlayerDataAccessor players = context.mock(PlayerDataAccessor.class);
		context.checking(new Expectations() {{
			oneOf(conf).getPlayerDataAccessor(); will(returnValue(players));
		}});
		assertEquals(players, world.getPlayerDataAccessor());
		context.assertIsSatisfied();
	}
	
	@Test
	public void providesConfigurationOutfitAccessor() {
		final OutfitAccessor outfits = context.mock(OutfitAccessor.class);
		context.checking(new Expectations() {{
			oneOf(conf).getOutfitAccessor(); will(returnValue(outfits));
		}});
		assertEquals(outfits, world.getOutfitAccessor());
		context.assertIsSatisfied();
	}
	
	@Test
	public void providesConfigurationItemTypeAccessor() {
		final ItemTypeAccessor types = context.mock(ItemTypeAccessor.class);
		context.checking(new Expectations() {{
			oneOf(conf).getItemTypeAccessor(); will(returnValue(types));
		}});
		assertEquals(types, world.getItemTypes());
		context.assertIsSatisfied();
	}
	
	@Test
	public void initializingWorldLoadsMapAndPopulatesChatManagerAndChangesState() {
		final Map map = new TestableMap();
		final MapAccessor maps = context.mock(MapAccessor.class);
		
		final String directory = "somedir";
		final String identifier = "somemap";
		
		context.checking(new Expectations() {{
			allowing(conf).getMapAccessor(); will(returnValue(maps));
			allowing(conf).getDirectory(); will(returnValue(directory));
			allowing(conf).getMapIdentifier(); will(returnValue(identifier));
			allowing(maps).loadMap(directory, identifier, world); will(returnValue(map));
		}});
		
		world.init();
		
		assertEquals(map, world.getMap());
		assertEquals(WorldState.RUNNING, world.getState());
		
		ChatManager chat = world.getChatManager();
		assertFalse(chat.getPublicChannels().isEmpty());
		
	}
	
}
