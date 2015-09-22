package org.jotserver.ot.net.game.out;

import org.jotserver.net.CData;
import org.jotserver.ot.model.Light;
import org.jotserver.ot.model.item.Item;
import org.jotserver.ot.model.player.Inventory;
import org.jotserver.ot.model.player.InventorySlot;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.net.game.AbstractWriter;
import org.jotserver.ot.net.game.OTDataOutputStream;
import org.jotserver.ot.net.game.creature.InventorySetItemWriter;
import org.jotserver.ot.net.game.creature.PlayerSkillsWriter;
import org.jotserver.ot.net.game.creature.PlayerStatsWriter;

import java.io.IOException;
import java.io.OutputStream;

public class SelfWriter extends AbstractWriter {

    private static final int OPBYTE_CANREPORT = 0x32;
    private static final int OPBYTE = 0x0A;

    private Light light;

    public SelfWriter(Player receiver, Light light) {
        super(receiver);
        this.light = light;
    }

    public void write(OutputStream out) throws IOException {
        OTDataOutputStream otout = new OTDataOutputStream(out);

        Player self = getReceiver();

        CData.writeByte(out, OPBYTE);
        CData.writeU32(out, self.getId());

        CData.writeByte(out, OPBYTE_CANREPORT);
        CData.writeByte(out, 0x00);
        CData.writeByte(out, self.canReportBugs() ? 1 : 0); // Can report 1/0

        writeInventory(otout);

        new PlayerStatsWriter(self).write(out);
        new PlayerSkillsWriter(self).write(out);

        new LightWriter(getReceiver(), light).write(out);
        new LightWriter(self, self.getLight()).write(out);
    }

    private void writeInventory(OTDataOutputStream out) throws IOException {
        Inventory inventory = getReceiver().getInventory();
        for(InventorySlot slot : InventorySlot.values()) {
            Item item = inventory.getItem(slot);
            if(item != null) {
                new InventorySetItemWriter(getReceiver(), slot, item).write(out);
            }
        }
    }

}
