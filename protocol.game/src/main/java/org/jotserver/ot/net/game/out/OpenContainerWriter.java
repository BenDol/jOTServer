package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class OpenContainerWriter extends AbstractWriter {
	
	private Container container;
	
	public OpenContainerWriter(Player receiver, Container container) {
		super(receiver);
		this.container = container;
	}

	public void write(OutputStream out) throws IOException {
		OTDataOutputStream otout = new OTDataOutputStream(out);
		otout.writeByte(0x6E);
		otout.writeByte(getReceiver().getContainerId(container));
		otout.writeItemId(container);
		otout.writeString(container.getName());
		otout.writeByte(container.getCapacity());
		//otout.writeByte(hasParent ? 0x01 : 0x00);
		otout.writeByte(0x00);
		List<Item> items = container.getItems();
		otout.writeByte(items.size());
		for(Item item : items) {
			otout.writeItem(item);
		}
	}
}
