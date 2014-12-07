package org.jotserver.web.secure;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.jotserver.configuration.ConfigurationAccessor;
import org.jotserver.ot.model.account.Account;
import org.jotserver.ot.model.player.PlayerAccessException;
import org.jotserver.ot.model.player.PlayerList;
import org.jotserver.web.AbstractActionBean;

public class IndexActionBean extends AbstractActionBean {
	
	private PlayerList players;
	
	@DefaultHandler
	public Resolution view() throws PlayerAccessException {
		Account account = getContext().getUserAccount();
		ConfigurationAccessor config = getContext().getConfig();
		players = config.getPlayerAccessor().getPlayerList(account, config.getGameWorldAccessor());
		return new ForwardResolution("/WEB-INF/view/secure/Index.jsp");
	}
	
	public PlayerList getPlayers() {
		return players;
	}
	
}
