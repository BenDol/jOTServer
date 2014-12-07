package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.map.Map;
import org.jotserver.ot.model.map.Tile;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Interval2D;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class FloorDescriptionWriter extends AbstractWriter {
	
	private Map map;
	private int z;
	private Interval2D interval;
	private int skipped;
	private boolean flushSkipped;
	
	public FloorDescriptionWriter(Player receiver, Map map, int z, Interval2D interval, int preSkipped, boolean flushSkipped) {
		super(receiver);
		this.map = map;
		this.z = z;
		this.interval = interval;
		skipped = preSkipped;
		this.flushSkipped = flushSkipped;
	}
	
	public FloorDescriptionWriter(Player receiver, Map map, int z, Interval2D interval) {
		super(receiver);
		this.map = map;
		this.z = z;
		this.interval = interval;
		skipped = -1;
		this.flushSkipped = false;
	}
	
	public FloorDescriptionWriter(Player receiver, Map map, int z, Interval2D interval, boolean flushSkipped) {
		super(receiver);
		this.map = map;
		this.z = z;
		this.interval = interval;
		skipped = -1;
		this.flushSkipped = flushSkipped;
	}
	
	public int getSkipped() {
		return skipped;
	}
	
	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}

	public void write(OutputStream out) throws IOException {
		OTDataOutputStream msg = new OTDataOutputStream(out);
		
		for (int x = interval.getStartX(); x <= interval.getEndX(); x++) {
			for (int y = interval.getStartY(); y <= interval.getEndY(); y++) {
				
				Position tilePos = new Position(x, y, z);
				Tile tile = map.getTile(tilePos);
				if (tile != null) {
					if (skipped >= 0) {
						skip(msg, skipped);
					}
					skipped = 0;
					new TileWriter(getReceiver(), tile).write(msg);
				} else {
					skipped++;
					if (skipped == 0xFF) {
						skip(msg, skipped);
						skipped = -1;
					}
				}
			}
		}
		if(flushSkipped && skipped >= 0) {
			skip(msg, skipped);
		}
	}
	
	private void skip(OTDataOutputStream msg, int skip) throws IOException {
		msg.writeByte(skip);
		msg.writeByte(0xFF);
	}
}
