package org.jotserver.ot.net.game.creature;

import java.io.IOException;
import java.io.OutputStream;

import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.player.InventorySlot;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

public class InventorySetItemWriter extends AbstractWriter {
	
	private static final int OPBYTE_ITEM = 0x78;
	private static final int OPBYTE_NOITEM = 0x79;
	private Item item;
	private InventorySlot slot;
	
	public InventorySetItemWriter(Player receiver, InventorySlot slot, Item item) {
		super(receiver);
		this.item = item;
		this.slot = slot;
	}
	
	public InventorySetItemWriter(Player receiver, InventorySlot slot) {
		this(receiver, slot, null);
	}

	public void write(OutputStream out) throws IOException {
		OTDataOutputStream otout = new OTDataOutputStream(out);
		if(item == null) {
			otout.writeByte(OPBYTE_NOITEM);
			otout.writeByte(slot.ordinal());
		} else {
			otout.writeByte(OPBYTE_ITEM);
			otout.writeByte(slot.ordinal());
			otout.writeItem(item);
		}
	}

}
