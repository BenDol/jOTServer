package org.jotserver.ot.model.world;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.Logger;
import org.jotserver.configuration.ConfigurationAccessor;
import org.jotserver.ot.model.Light;
import org.jotserver.ot.model.OutfitAccessor;
import org.jotserver.ot.model.chat.ChatManager;
import org.jotserver.ot.model.chat.ConsoleChannel;
import org.jotserver.ot.model.chat.PublicChatChannel;
import org.jotserver.ot.model.item.ItemTypeAccessor;
import org.jotserver.ot.model.map.Map;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.player.PlayerDataAccessor;
import org.jotserver.ot.model.util.Dispatcher;

public class LocalGameWorld implements GameWorld {
	private static final Logger logger = Logger.getLogger(LocalGameWorld.class);
	
	private GameWorld base;
	private ChatManager chatManager;
	
	private HashMap<Long, Player> players;
	private GameWorldConfiguration config;
	private Map map;
	private Dispatcher dispatcher;
	private WorldState state;
	private ConfigurationAccessor globalConfig;

	public LocalGameWorld(ScheduledExecutorService executor, GameWorld world, ConfigurationAccessor globalConfig, GameWorldConfiguration config) {
		map = null;
		state = WorldState.LODAING;
		this.base = world;
		this.config = config;
		this.globalConfig = globalConfig;
		
		chatManager = new ChatManager();
		
		players = new HashMap<Long, Player>();
		
		dispatcher = new Dispatcher(executor);
	}
	
	public void init() {
		String mapIdentifier = config.getMapIdentifier();
		loadMap(mapIdentifier);
		
		ChatManager chatManager = getChatManager();
		chatManager.addPublicChannel(new PublicChatChannel(0x04, "Game-Chat"));
		chatManager.addPublicChannel(new PublicChatChannel(0x05, "Trade"));
		chatManager.addPublicChannel(new PublicChatChannel(0x06, "RL-Chat"));
		chatManager.addPublicChannel(new PublicChatChannel(0x08, "Help"));
		chatManager.addPublicChannel(new ConsoleChannel(this, 0xA1, "Admin"));
		setState(WorldState.RUNNING);
	}

	private void loadMap(String mapIdentifier) {
		map = config.getMapAccessor().loadMap(
				config.getDirectory(), 
				mapIdentifier, 
				this);
	}
	
	public WorldState getState() {
		return state;
	}

	public void setState(WorldState state) {
		this.state = state;
	}
	
	public boolean isRunning() {
		return state == WorldState.RUNNING;
	}
	
	public void addPlayer(Player player) {
		players.put(player.getId(), player);
	}
	
	public void removePlayer(Player player) {
		players.remove(player.getId());
	}

	public Player getPlayerByGlobalId(int globalId) {
		for(Player player : players.values()) {
			if(player.getGlobalId() == globalId) {
				return player;
			}
		}
		return null;
	}
	
	public Player getPlayerByName(String name) {
		for(Player player : players.values()) {
			if(player.getName().equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}
	
	public Collection<Player> getPlayers() {
		return Collections.unmodifiableCollection(players.values());
	}
	
	public InetSocketAddress getAddress() {
		return base.getAddress();
	}
	
	
	public String getIdentifier() {
		return base.getIdentifier();
	}
	
	
	public String getName() {
		return base.getName();
	}
	
	public Map getMap() {
		return map;
	}

	public PlayerDataAccessor getPlayerDataAccessor() {
		return config.getPlayerDataAccessor();
	}
	
	public PlayerAccessor getPlayerAccessor() {
		return globalConfig.getPlayerAccessor();
	}

	
	public boolean isLocal() {
		return true;
	}

	public Light getLight() {
		return new Light(250, 215);
	}
	
	public ChatManager getChatManager() {
		return chatManager;
	}

	public OutfitAccessor getOutfitAccessor() {
		return config.getOutfitAccessor();
	}

	public Dispatcher getDispatcher() {
		return dispatcher;
	}
	
	public ItemTypeAccessor getItemTypes() {
		return config.getItemTypeAccessor();
	}
	
	public GameWorldConfiguration getConfiguration() {
		return config;
	}

	public ConfigurationAccessor getGlobalConfiguration() {
		return globalConfig;
	}
	
}
