package org.jotserver.web.secure;

import java.util.StringTokenizer;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.jotserver.configuration.ConfigurationAccessor;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.player.PlayerAccessException;
import org.jotserver.ot.model.player.PlayerAccessor;
import org.jotserver.ot.model.world.GameWorld;
import org.jotserver.ot.model.world.GameWorldAccessor;
import org.jotserver.ot.model.world.GameWorldConfiguration;
import org.jotserver.web.AbstractActionBean;

public class CreateCharacterActionBean extends AbstractActionBean {
	
	private String name;
	private String world;

	@DefaultHandler
	@DontValidate
	public Resolution view() {
		return new ForwardResolution("/WEB-INF/view/secure/CreateCharacter.jsp");
	}
	
	public Resolution create() throws PlayerAccessException {
		ConfigurationAccessor config = getContext().getConfig();
		GameWorldAccessor<GameWorld> gameWorldAccessor = config.getGameWorldAccessor();
		PlayerAccessor playerAccessor = config.getPlayerAccessor();
		Player player = playerAccessor.getPlayer(name, gameWorldAccessor);
		if(gameWorldAccessor.getGameWorld(world) == null) {
			addFieldError("world", "/secure/CreateCharacter.action.world.invalid", world);
			return new ForwardResolution("/WEB-INF/view/secure/CreateCharacter.jsp");
		} else if(player == null) {
			name = formatName(name);
			player = new Player(0, name, gameWorldAccessor.getGameWorld(world));
			playerAccessor.createPlayer(getContext().getUserAccount(), player);
			
			GameWorldConfiguration worldConfig = config.getGameWorldConfigurationAccessor().getGameWorldConfiguration(world);
			worldConfig.getPlayerDataAccessor().savePlayerData(player);
			
			return new RedirectResolution("/secure/Index.action");
		} else {
			addFieldError("name", "/secure/CreateCharacter.action.exists", name);
			return new ForwardResolution("/WEB-INF/view/secure/CreateCharacter.jsp");
		}
	}
	
	@Validate(label="character name", required=true, minlength=4, maxlength=25, mask="^[a-zA-Z](([\\'\\.\\- ]|[\\'\\.\\-] )?[a-zA-Z]+)*$")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Validate(label="world", required=true)
	public String getWorld() {
		return world;
	}
	public void setWorld(String worldId) {
		this.world = worldId;
	}
	
	private String formatName(String str) {
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(str, " .-'", true);
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			sb.append(Character.toUpperCase(token.charAt(0)));
			sb.append(token.substring(1).toLowerCase());
		}
		return sb.toString();
	}
	
}
