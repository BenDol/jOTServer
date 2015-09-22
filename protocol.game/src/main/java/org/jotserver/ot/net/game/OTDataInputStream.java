package org.jotserver.ot.net.game;

import org.jotserver.net.CDataInputStream;
import org.jotserver.ot.model.util.Position;

import java.io.IOException;
import java.io.InputStream;

public class OTDataInputStream extends CDataInputStream {

    public OTDataInputStream(InputStream stream) {
        super(stream);
    }

    public Position readPosition() throws IOException {
        int x = readU16();
        int y = readU16();
        int z = readByte();
        return new Position(x, y, z);
    }
}
