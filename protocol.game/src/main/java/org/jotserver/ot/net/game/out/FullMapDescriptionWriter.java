package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.map.Map;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.ClientView;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class FullMapDescriptionWriter extends AbstractWriter {

    private static final int OPBYTE_MAPDESCRIPTION = 0x64;
    private Map map;
    private Position pos;

    public FullMapDescriptionWriter(Player receiver, Position pos, Map map) {
        super(receiver);
        this.map = map;
        this.pos = pos;
    }

    public void write(OutputStream out) throws IOException {
        OTDataOutputStream otout = new OTDataOutputStream(out);

        otout.writeByte(OPBYTE_MAPDESCRIPTION);
        otout.writePosition(pos);
        new MapDescriptionWriter(getReceiver(), map, ClientView.get3DView(pos)).write(out);
    }
}
