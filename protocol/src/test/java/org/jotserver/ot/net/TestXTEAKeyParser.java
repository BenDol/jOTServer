package org.jotserver.ot.net;


import org.jotserver.net.CData;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class TestXTEAKeyParser {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void parseKey() throws IOException {
        int a = 1234;
        int b = 2345;
        int c = 3456;
        int d = 4567;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CData.writeU32(out, a);
        CData.writeU32(out, b);
        CData.writeU32(out, c);
        CData.writeU32(out, d);

        XTEAKeyParser p = new XTEAKeyParser(new ByteArrayInputStream(out.toByteArray()));
        assertArrayEquals(new long[] {a, b, c, d}, p.getKeys());
    }

}
