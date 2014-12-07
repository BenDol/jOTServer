package org.jotserver.ot.model.world;

import org.jotserver.ot.model.event.EventEngine;
import org.jotserver.ot.model.event.JavaScriptEventEngine;

import java.io.File;

public aspect LocalGameWorldInitializer {
	
	public EventEngine LocalGameWorld.eventEngine;
	
	public EventEngine LocalGameWorld.getEventEngine() {
		return eventEngine;
	}
	
	private pointcut initializeLocalGameWorld(LocalGameWorld world) : 
		target(world) &&
		execution(public void LocalGameWorld.init());
	
	after(LocalGameWorld world) : initializeLocalGameWorld(world) {
		String path = new File(world.getConfiguration().getDirectory()).getAbsolutePath() + File.separator + "modules";
		world.eventEngine = new JavaScriptEventEngine(path);
		world.eventEngine.init(world);
	}
}
