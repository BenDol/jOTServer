package org.jotserver.ot.model.world;

import java.net.InetSocketAddress;

import org.jotserver.configuration.TestableConfigurationAccessor;
import org.jotserver.ot.model.OutfitAccessor;
import org.jotserver.ot.model.chat.ChatManager;
import org.jotserver.ot.model.item.ItemTypeAccessor;
import org.jotserver.ot.model.map.Map;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.player.PlayerDataAccessor;
import org.jotserver.ot.model.util.Dispatcher;
import org.junit.Ignore;

@Ignore
public class TestableGameWorld extends LocalGameWorld {

    @Override
    public ChatManager getChatManager() {
        return super.getChatManager();
    }

    @Override
    public Dispatcher getDispatcher() {
        return super.getDispatcher();
    }

    @Override
    public ItemTypeAccessor getItemTypes() {
        return super.getItemTypes();
    }

    @Override
    public Map getMap() {
        return super.getMap();
    }

    @Override
    public OutfitAccessor getOutfitAccessor() {
        return super.getOutfitAccessor();
    }

    @Override
    public PlayerAccessor getPlayerAccessor() {
        return super.getPlayerAccessor();
    }

    @Override
    public PlayerDataAccessor getPlayerDataAccessor() {
        return super.getPlayerDataAccessor();
    }

    public TestableGameWorld() {
        super(null, new BaseWorld(), new TestableConfigurationAccessor(), new TestableGameWorldConfiguration());
    }

    public static class BaseWorld implements GameWorld {
        public InetSocketAddress getAddress() {
            return null;
        }
        public String getIdentifier() {
            return null;
        }
        public String getName() {
            return null;
        }
        public boolean isLocal() {
            return false;
        }
    }

}
