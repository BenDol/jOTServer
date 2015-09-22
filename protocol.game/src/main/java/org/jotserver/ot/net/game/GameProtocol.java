package org.jotserver.ot.net.game;

import org.apache.log4j.Logger;
import org.jotserver.io.AccessException;
import org.jotserver.net.CData;
import org.jotserver.ot.model.Effect;
import org.jotserver.ot.model.account.Account;
import org.jotserver.ot.model.account.AccountAccessException;
import org.jotserver.ot.model.account.AccountAccessor;
import org.jotserver.ot.model.map.Map;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.player.PlayerAccessException;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.player.PlayerVipAccessor;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.model.world.GameWorldAccessor;
import org.jotserver.ot.model.world.LocalGameWorld;
import org.jotserver.ot.net.*;
import org.jotserver.ot.net.game.out.SelfWriter;
import org.jotserver.ot.net.io.MessageInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.List;

public class GameProtocol extends EncryptableProtocol {
    private Logger logger = Logger.getLogger(GameProtocol.class);

    public static final int PROTOCOLID = 0x0A;
    private Player player;

    private GameWorldAccessor<LocalGameWorld> worlds;
    private AccountAccessor accounts;
    private PlayerAccessor players;
    private ThreadLocal<OutputStream> localStream = new ThreadLocal<OutputStream>();

    public GameProtocol(GameWorldAccessor<LocalGameWorld> worlds, AccountAccessor accounts, PlayerAccessor players) {
        super();
        this.worlds = worlds;
        this.accounts = accounts;
        this.players = players;
    }

    public void parseFirst(InputStream message) throws IOException {

        /*ClientVersionParser clientVersion = */new ClientVersionParser(message,
                false);

        try {
            message = decryptStreamRSA(message);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            client.close();
            return;
        }

        XTEAKeyParser xteaKey = new XTEAKeyParser(message);

        CharacterLoginParser characterLogin = new CharacterLoginParser(message);
        try {
            initXTEAEngine(xteaKey.getKeys());

            Account account = accounts.getAccount(characterLogin.getNumber(),
                            characterLogin.getPlayerName());
            if (account == null || !characterLogin.validate(account)) {
                throw new AccountAccessException(
                        "Please enter a valid account number and password.");
            }

            login(characterLogin.getPlayerName());

            OutputBuffer.flush();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            client.close();
        } catch (AccessException e) {
            OutputStream out = getEncryptedMessageOutputStream();
            new DisconnectClientWriter(0x14, e.getMessage()).write(out);
            logger.error("Failed to login player.", e);
            out.flush();
            client.close();
        }

    }

    public void parsePacket(InputStream message) throws IOException {
        message = new MessageInputStream(this.getDecryptedInputStream(message));
        int opbyte = CData.readByte(message);

        ParserContext context = new ParserContext() {
            public Player getPlayer() { return player; }
            public LocalGameWorld getWorld() { return getPlayerWorld(player); }
            public GameWorldAccessor<LocalGameWorld> getWorlds() { return worlds; }
            public PlayerAccessor getPlayers() { return players; }
            public AccountAccessor getAccounts() { return accounts; }
        };

        PacketType type = PacketType.getType(opbyte);
        if(type != null) {
            if(player.isDead() && type != PacketType.LOGOUT) {
                return;
            }
            MessageParser parser = type.getParser();
            parser.setContext(context);
            parser.parse(type, message);

            OutputBuffer.flush();
        } else {
            logger.debug("Unknown packet type " + opbyte + " from player " + player.getName() + ".");
        }
    }


    private void login(String playerName) throws IOException, PlayerAccessException {
        player = players.getPlayer(playerName, worlds);

        LocalGameWorld world = getPlayerWorld(player);
        if(world == null) {
            OutputStream out = getEncryptedMessageOutputStream();
            new DisconnectClientWriter(0x14, "Character not found.").write(out);
            out.flush();
            client.close();
        } else {
            world.getPlayerDataAccessor().loadPlayerData(player, world.getItemTypes(), world.getMap().getTownAccessor());
            player.setGameProtocol(this);
            world.addPlayer(player);

            send(new SelfWriter(player, world.getLight()));

            Map playerMap = world.getMap();
            Position pos = player.getTemporaryPosition();
            Tile tile = playerMap.getTile(pos);
            if(tile == null) {
                pos = player.getTown().getPosition();
                tile = playerMap.getTile(pos);
            }
            if(tile == null) {
                throw new PlayerAccessException("Invalid spawn position.");
            } else {
                tile.executeAddCreature(player);
                tile.executeAddEffect(Effect.TELEPORT);
                PlayerVipAccessor vipAccessor = players.getPlayerVipAccessor();
                List<Player> vipList = vipAccessor.getPlayerVipList(player, world);
                for(Player vip : vipList) {
                    player.addVip(vip);
                }
            }
        }
    }

    /*
     * Retrieval methods.
     */

    public LocalGameWorld getPlayerWorld(Player player) {
        GameWorld world = player.getWorld();
        if(world == null) return null;
        return worlds.getGameWorld(world.getIdentifier());
    }

    public boolean isOnline() {
        return player != null && !client.isClosed();
    }

    /*
     * Action methods.
     */

    public synchronized void send(Writer writer) {
        try {
            OutputStream out = localStream.get();
            if(out == null) {
                out = getEncryptedMessageOutputStream();
                localStream.set(out);
            }
            writer.write(out);
            OutputBuffer.add(out);
        } catch (IOException e) {
            try {
                client.close();
            } catch (IOException e1) {
                logger.error("Failed to disconnect client on error. ", e1);
            }
        }
    }

    public void disconnect() {
        try {
            LocalGameWorld world = getPlayerWorld(player);
            world.getPlayerDataAccessor().savePlayerData(player);
            world.getPlayerAccessor().getPlayerVipAccessor().savePlayerVipList(player);
            Tile tile = player.getTile();
            if(tile != null) {
                tile.executeRemoveCreature(player);
                tile.executeAddEffect(Effect.TELEPORT);
            }
            world.removePlayer(player);
            client.close();
        } catch (PlayerAccessException e) {
            player.getPrivateChannel().sendCancel(e.getMessage());
            logger.error("Failed to save player!", e);
        } catch(IOException e) {
            logger.error("Failed to disconnect client.");
        }
    }

}
