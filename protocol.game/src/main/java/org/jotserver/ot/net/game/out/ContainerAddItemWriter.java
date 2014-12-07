package org.jotserver.ot.net.game.out;

import org.jotserver.ot.model.item.Container;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class ContainerAddItemWriter extends AbstractWriter {

	private Container container;
	private Item item;

	public ContainerAddItemWriter(Player receiver, Container container, Item item) {
		super(receiver);
		this.container = container;
		this.item = item;
	}

	public void write(OutputStream out) throws IOException {
		OTDataOutputStream otout = new OTDataOutputStream(out);
		otout.writeByte(0x70);
		otout.writeByte(getReceiver().getContainerId(container));
		otout.writeItem(item);
	}
}
