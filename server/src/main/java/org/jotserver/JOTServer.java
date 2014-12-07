package org.jotserver;

import org.jotserver.configuration.ConfigurationAccessor;
import org.jotserver.configuration.PropertiesConfigurationAccessor;
import org.jotserver.net.BaseServer;
import org.jotserver.ot.model.MOTD;
import org.jotserver.ot.model.MOTDAccessException;
import org.jotserver.ot.model.account.AccountAccessor;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.world.*;
import org.jotserver.ot.net.ConnectionInitializer;
import org.jotserver.ot.net.Protocol;
import org.jotserver.ot.net.ProtocolProvider;
import org.jotserver.ot.net.game.GameProtocol;
import org.jotserver.ot.net.login.LoginProtocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class JOTServer {
	
	public static void main(String[] args) throws IOException, MOTDAccessException {
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
		ConfigurationAccessor config = new PropertiesConfigurationAccessor("config.properties");
		
		final AccountAccessor accounts = config.getAccountAccessor();
		final PlayerAccessor players = config.getPlayerAccessor();
		final MOTD motd = config.getMOTDAccessor().getMOTD();
		
		final GameWorldAccessor<GameWorld> worlds = config.getGameWorldAccessor();
		final ArrayList<LocalGameWorld> localWorlds = new ArrayList<LocalGameWorld>();
		
		for(GameWorld world : worlds.getGameWorlds()) {
			if(world.isLocal()) {
				GameWorldConfiguration gameWorldConfig = config.getGameWorldConfigurationAccessor().getGameWorldConfiguration(world.getIdentifier());
				LocalGameWorld localWorld = new LocalGameWorld(executor, world, config, gameWorldConfig);
				localWorld.init();
				
				localWorlds.add(localWorld);
			}
		}
		final LocalGameWorldAccessor localGameWorlds = new LocalGameWorldAccessor(localWorlds);
		
		
		BaseServer server = new BaseServer(config.getPort());
		List<ProtocolProvider> protocolProviders = new ArrayList<ProtocolProvider>();
		
		protocolProviders.add(new ProtocolProvider() {
			
			public Protocol getProtocol(int type) {
				if(type == LoginProtocol.PROTOCOLID) {
					return new LoginProtocol(accounts, players, worlds, motd);
				} else {
					return null;
				}
			}});
		
		protocolProviders.add(new ProtocolProvider() {
			
			public Protocol getProtocol(int type) {
				if(type == GameProtocol.PROTOCOLID) {
					return new GameProtocol(localGameWorlds, accounts, players);
				} else {
					return null;
				}
			}});
		
		server.addConnectionListener(new ConnectionInitializer(protocolProviders));
		
		System.gc();
		
		server.start();
	}
}
