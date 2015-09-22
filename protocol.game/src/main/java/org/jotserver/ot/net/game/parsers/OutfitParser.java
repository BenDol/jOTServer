package org.jotserver.ot.net.game.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.jotserver.net.CData;
import org.jotserver.ot.model.Outfit;
import org.jotserver.ot.model.OutfitAccessor;
import org.jotserver.ot.model.OutfitType;
import org.jotserver.ot.net.game.GameProtocol;
import org.jotserver.ot.net.game.PacketType;
import org.jotserver.ot.net.game.out.OutfitDialogWriter;

public class OutfitParser extends AbstractParser {
    public void parse(PacketType type, InputStream message) throws IOException {
        switch(type) {
        case REQUESTOUTFITS:
            OutfitAccessor outfits = getWorld().getOutfitAccessor();
            getPlayer().getGameProtocol().send(new OutfitDialogWriter(getPlayer(), outfits.getOutfits()));
            break;
        case CHANGEOUTFIT:
            parseChangeOutfit(message);
            break;
        }
    }

    private void parseChangeOutfit(InputStream in) throws IOException {
        int type = CData.readU16(in);
        OutfitType outfitType = getWorld().getOutfitAccessor().getOutfit(type);
        if(outfitType != null) {
            int head = CData.readByte(in);
            int body = CData.readByte(in);
            int legs = CData.readByte(in);
            int feet = CData.readByte(in);
            int addons = CData.readByte(in);
            Outfit outfit = new Outfit(outfitType, head, body, legs, feet, addons);
            getPlayer().setOutfit(outfit);
        }
    }
}
