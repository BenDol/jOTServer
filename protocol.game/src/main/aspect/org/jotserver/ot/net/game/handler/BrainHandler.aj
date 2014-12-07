package org.jotserver.ot.net.game.handler;

import org.jotserver.ot.net.game.OutputBuffer;

public aspect BrainHandler {
	
	public pointcut brainThink() :
		execution(public void Brain.think());
	
	after() : brainThink() {
		OutputBuffer.flush();
	}
}