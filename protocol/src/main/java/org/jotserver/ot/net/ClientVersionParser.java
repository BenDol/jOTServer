package org.jotserver.ot.net;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.CData;


public class ClientVersionParser implements Parser {

    private int os;
    private int version;

    private long dataVersion;
    private long spriteVersion;
    private long picVersion;
    private boolean extended;

    public ClientVersionParser(boolean extended) {
        os = 0;
        version = 0;
        this.extended = extended;
        dataVersion = 0;
        spriteVersion = 0;
        picVersion = 0;
    }

    public ClientVersionParser(InputStream in, boolean extended) throws IOException {
        this(extended);
        parse(in);
    }

    public void parse(InputStream in) throws IOException {
        os = CData.readU16(in);
        version = CData.readU16(in);
        if(extended) {
            dataVersion = CData.readU32(in);
            spriteVersion = CData.readU32(in);
            picVersion = CData.readU32(in);
        }
    }

    public int getOs() {
        return os;
    }

    public int getVersion() {
        return version;
    }

    public long getDataVersion() {
        if(!extended) throw new IllegalStateException("Invalid property for non-extended parser.");
        return dataVersion;
    }

    public long getSpriteVersion() {
        if(!extended) throw new IllegalStateException("Invalid property for non-extended parser.");
        return spriteVersion;
    }

    public long getPicVersion() {
        if(!extended) throw new IllegalStateException("Invalid property for non-extended parser.");
        return picVersion;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }


}
