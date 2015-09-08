package org.jotserver.ot.net.login;

import org.apache.log4j.Logger;
import org.jotserver.io.AccessException;
import org.jotserver.ot.model.MOTD;
import org.jotserver.ot.model.account.Account;
import org.jotserver.ot.model.account.AccountAccessException;
import org.jotserver.ot.model.account.AccountAccessor;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.player.PlayerList;
import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.model.world.GameWorldAccessor;
import org.jotserver.ot.net.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

public class LoginProtocol extends EncryptableProtocol {
	private static final Logger logger = Logger.getLogger(LoginProtocol.class);

	public static final int PROTOCOLID = 0x01;
	
	private AccountAccessor accounts;
	private PlayerAccessor players;
	private GameWorldAccessor<GameWorld> worlds;
	private MOTD motd;

	public LoginProtocol(AccountAccessor accounts, PlayerAccessor players, GameWorldAccessor<GameWorld> worlds, MOTD motd) {
		super();

		this.accounts = accounts;
		this.players = players;
		this.motd = motd;
		this.worlds = worlds;
	}
	
	public void parseFirst(InputStream message) throws IOException {

		ClientVersionParser clientVersion = new ClientVersionParser(message, true);

		logger.trace("Client with os " + clientVersion.getOs()
            + " and version " + clientVersion.getVersion()
            + " connected. (Dat: " + clientVersion.getDataVersion()
            + ", spr: " + clientVersion.getSpriteVersion() + ", pic: "
            + clientVersion.getPicVersion() + ")");

		try {
			message = decryptStreamRSA(message);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			client.close();
			return;
		}
		
		XTEAKeyParser xteaKey = new XTEAKeyParser(message);
		
		AccountLoginParser accountLogin = new AccountLoginParser(message);
		try {
			initXTEAEngine(xteaKey.getKeys());
			
			Account account = accounts.getAccount(accountLogin.getNumber());
			if(account == null || !accountLogin.validate(account)) {
				throw new AccountAccessException("Please enter a valid account number and password.");
			}
			
			OutputStream out = getEncryptedMessageOutputStream();
			
			new MOTDWriter(motd).write(out);
			
			PlayerList characters = players.getPlayerList(account, worlds);

			new CharacterListWriter(account.getPremium(), characters).write(out);
			
			out.flush();
			
		} catch (AccessException e) {
			OutputStream out = getEncryptedMessageOutputStream();
			new DisconnectClientWriter(e.getMessage()).write(out);
			out.flush();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}

	public void parsePacket(InputStream message) throws IOException {
		//
	}
}
