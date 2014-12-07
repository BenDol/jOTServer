package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.map.Map;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.util.Direction;
import org.jotserver.ot.model.util.Interval2D;
import org.jotserver.ot.model.util.Position;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.ClientView;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class MoveSelfWriter extends AbstractWriter {
	
	private Map map;
	private Position from;
	private Position to;
	private int fromStackPos;
	
	public MoveSelfWriter(Player receiver, Map map, Position from, int fromStackPos, Position to) {
		super(receiver);
		this.map = map;
		this.from = from;
		this.to = to;
		this.fromStackPos = fromStackPos;
	}

	public void write(OutputStream out) throws IOException {
		OTDataOutputStream otout = new OTDataOutputStream(out);
		
		if(from.getZ() == 7 && to.getZ() >= 8) {
			new TileRemoveThingWriter(getReceiver(), from, fromStackPos).write(out);
		} else {
			otout.writeByte(0x6D);
			otout.writePosition(from);
			otout.writeByte(fromStackPos);
			otout.writePosition(to);
		}
		
		if(to.getZ() > from.getZ()) { //floor change down
			new SelfMoveDownWriter(getReceiver()).write(otout);
		} else if(to.getZ() < from.getZ()) { //floor change up
			new SelfMoveUpWriter(getReceiver()).write(otout);
		}
		
		
		Position pos = new Position(from.getX(), to.getY(), to.getZ()); 
		// Here we use the old X to ease up diagonal movement handling
		if(from.getY() > to.getY()) { // north, for old x
			otout.writeByte(0x65);
			new MapDescriptionWriter(getReceiver(), map, 
					ClientView.get3DBorderView(pos, Direction.NORTH)).write(out);
		} else if(from.getY() < to.getY()) { // south, for old x
			otout.writeByte(0x67);
			new MapDescriptionWriter(getReceiver(), map, 
					ClientView.get3DBorderView(pos, Direction.SOUTH)).write(out);
		}
		
		// Here we use the new position.
		if(from.getX() < to.getX()) { // east, [with new y]
			otout.writeByte(0x66);
			new MapDescriptionWriter(getReceiver(), map, 
					ClientView.get3DBorderView(to, Direction.EAST)).write(out);
		} else if(from.getX() > to.getX()) { // west, [with new y]
			otout.writeByte(0x68);
			new MapDescriptionWriter(getReceiver(), map, 
					ClientView.get3DBorderView(to, Direction.WEST)).write(out);
		}
	}

	class SelfMoveUpWriter extends AbstractWriter {
		public SelfMoveUpWriter(Player receiver) {
			super(receiver);
		}

		public void write(OutputStream out) throws IOException {
			OTDataOutputStream otout = new OTDataOutputStream(out);
			
			//floor change up
			otout.writeByte(0xBE);
			
			if(to.getZ() == 7) {
				FloorDescriptionWriter writer;
				Interval2D interval = ClientView.get2DView(from);
				//(floor 7 and 6 already set)
				writer = new FloorDescriptionWriter(getReceiver(), map, 5, interval.offset(3, 3)); 
				writer.write(out);
				
				writer = new FloorDescriptionWriter(getReceiver(), map, 4, interval.offset(4, 4), 
						writer.getSkipped(), false); 
				writer.write(out);
				
				writer = new FloorDescriptionWriter(getReceiver(), map, 3, interval.offset(5, 5), 
						writer.getSkipped(), false); 
				writer.write(out);
				
				writer = new FloorDescriptionWriter(getReceiver(), map, 2, interval.offset(6, 6), 
						writer.getSkipped(), false); 
				writer.write(out);
				
				writer = new FloorDescriptionWriter(getReceiver(), map, 1, interval.offset(7, 7), 
						writer.getSkipped(), false); 
				writer.write(out);
				
				writer = new FloorDescriptionWriter(getReceiver(), map, 0, interval.offset(8, 8), 
						writer.getSkipped(), true); 
				writer.write(out);
				
			} else if(to.getZ() > 7) {
				FloorDescriptionWriter writer;
				Interval2D interval = ClientView.get2DView(from);
				writer = new FloorDescriptionWriter(getReceiver(), map, from.getZ()-3, interval.offset(3, 3), true); 
				writer.write(out);
			}

			//moving up a floor up makes us out of sync
			//west
			otout.writeByte(0x68);
			Position pos = new Position(from.getX(), from.getY()+1, to.getZ());
			new MapDescriptionWriter(getReceiver(), map, ClientView.get3DBorderView(pos, Direction.WEST)).write(out);
			
			//north
			otout.writeByte(0x65);
			pos = new Position(from.getX(), from.getY(), to.getZ());
			new MapDescriptionWriter(getReceiver(), map, ClientView.get3DBorderView(pos, Direction.NORTH)).write(out);
		}
	}
	
	class SelfMoveDownWriter extends AbstractWriter {
		public SelfMoveDownWriter(Player receiver) {
			super(receiver);
		}
		
		public void write(OutputStream out) throws IOException {
			OTDataOutputStream otout = new OTDataOutputStream(out);
			
			otout.writeByte(0xBF);

			//going from surface to underground
			if(to.getZ() == 8) {
				FloorDescriptionWriter writer;
				Interval2D interval = ClientView.get2DView(new Position(from.getX(), from.getY(), to.getZ()));
				writer = new FloorDescriptionWriter(getReceiver(), map, to.getZ()+0, interval.offset(-1, -1)); 
				writer.write(out);
				
				writer = new FloorDescriptionWriter(getReceiver(), map, to.getZ()+1, interval.offset(-2, -2), 
						writer.getSkipped(), false); 
				writer.write(out);
				
				writer = new FloorDescriptionWriter(getReceiver(), map, to.getZ()+2, interval.offset(-3, -3), 
						writer.getSkipped(), true); 
				writer.write(out);
				
			}
			//going further down
			else if(to.getZ() > from.getZ() && to.getZ() > 8 && to.getZ() < 14) {
				FloorDescriptionWriter writer;
				Interval2D interval = ClientView.get2DView(new Position(from.getX(), from.getY(), to.getZ()));
				writer = new FloorDescriptionWriter(getReceiver(), map, to.getZ()+2, interval.offset(-3, -3), true); 
				writer.write(out);
			}
			
			//moving down a floor makes us out of sync
			//east
			otout.writeByte(0x66);
			Position pos = new Position(from.getX(), from.getY()-1, to.getZ());
			new MapDescriptionWriter(getReceiver(), map, ClientView.get3DBorderView(pos, Direction.EAST)).write(out);
			
			//south
			otout.writeByte(0x67);
			pos = new Position(from.getX(), from.getY(), to.getZ());
			new MapDescriptionWriter(getReceiver(), map, ClientView.get3DBorderView(pos, Direction.SOUTH)).write(out);
		}
	}
}
