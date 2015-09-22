package org.jotserver.ot.model.event;

import org.jotserver.ot.model.world.LocalGameWorld;

public interface ModuleContext {
    LocalGameWorld getWorld();

    EventEngine getEventEngine();
}
