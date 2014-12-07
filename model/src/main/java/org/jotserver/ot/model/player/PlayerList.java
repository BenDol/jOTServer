package org.jotserver.ot.model.player;

import java.util.ArrayList;
import java.util.Collection;

public class PlayerList extends ArrayList<Player> {

	private static final long serialVersionUID = 1L;

	public PlayerList() {
		super();
	}

	public PlayerList(Collection<? extends Player> arg0) {
		super(arg0);
	}

	public PlayerList(int arg0) {
		super(arg0);
	}
	
}
