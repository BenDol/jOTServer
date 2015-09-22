package org.jotserver.ot.net.game;

import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.Writer;

public abstract class AbstractWriter implements Writer {

    private final Player receiver;

    public AbstractWriter(Player receiver) {
        this.receiver = receiver;
    }

    protected Player getReceiver() {
        return receiver;
    }
}
