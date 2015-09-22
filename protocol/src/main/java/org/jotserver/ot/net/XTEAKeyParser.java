package org.jotserver.ot.net;

import java.io.IOException;
import java.io.InputStream;

import org.jotserver.net.CData;


public class XTEAKeyParser implements Parser {

    private long[] key;

    public XTEAKeyParser() {
        key = new long[4];
    }

    public XTEAKeyParser(InputStream in) throws IOException {
        this();
        parse(in);
    }


    public void parse(InputStream in) throws IOException {
        key[0] = CData.readU32(in);
        key[1] = CData.readU32(in);
        key[2] = CData.readU32(in);
        key[3] = CData.readU32(in);
    }

    public long[] getKeys() {
        return key.clone();
    }

}
