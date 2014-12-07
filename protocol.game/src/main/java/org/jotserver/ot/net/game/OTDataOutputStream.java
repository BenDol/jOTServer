package org.jotserver.ot.net.game;

import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.net.CDataOutputStream;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.item.Stackable;
import org.jotserver.ot.model.util.Position;

public class OTDataOutputStream extends CDataOutputStream {

	public OTDataOutputStream(OutputStream stream) {
		super(stream);
	}
	
	public void writePosition(Position pos) throws IOException {
		writeU16(pos.getX());
		writeU16(pos.getY());
		writeByte(pos.getZ());
	}
	
	public void writeItem(Item item) throws IOException {
		writeItemId(item);
		
		if(item instanceof Stackable) {
			Stackable stackable = (Stackable)item;
			writeByte(stackable.getCount());
		} else if(item.isRune()) {
			writeByte(1);
			// TORO: Implement runes.
		} else if(item.isSplash() || item.isFluidContainer()) {
			writeByte(item.getFluidType().getColor().getClientId());
		}
	}
	
	public void writeItemId(Item item) throws IOException {
		writeU16(item.getClientId());
	}

}
