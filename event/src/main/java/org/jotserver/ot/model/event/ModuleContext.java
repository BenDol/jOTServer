package org.jotserver.ot.model.event;

import org.jotserver.ot.model.world.LocalGameWorld;

public interface ModuleContext {
	public LocalGameWorld getWorld();

	public EventEngine getEventEngine();
}
