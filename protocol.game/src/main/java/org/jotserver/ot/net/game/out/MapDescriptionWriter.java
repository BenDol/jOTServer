package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.map.Map;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Interval3D;
import org.jotserver.ot.net.game.AbstractWriter;

import java.io.IOException;
import java.io.OutputStream;


public class MapDescriptionWriter extends AbstractWriter {

    private Map map;
    private Interval3D interval;

    public MapDescriptionWriter(Player receiver, Map map, Interval3D interval) {
        super(receiver);
        this.map = map;
        this.interval = interval;
    }

    public void write(OutputStream out) throws IOException {
        int skip = -1;

        int zStep = interval.getStartZ() <= interval.getEndZ() ? 1 : -1;

        FloorDescriptionWriter writer;
        for(int z = interval.getStartZ(); z != interval.getEndZ()+zStep; z += zStep) {
            int off = getReceiver().getPosition().getZ() - z;
            boolean isLast = z == interval.getEndZ();
            writer = new FloorDescriptionWriter(getReceiver(), map, z, interval.get2D().offset(off, off), skip, isLast);
            writer.write(out);
            skip = writer.getSkipped();
        }
    }
}