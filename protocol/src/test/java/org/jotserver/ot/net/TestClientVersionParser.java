package org.jotserver.ot.net;


import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jotserver.net.CData;
import org.junit.Before;
import org.junit.Test;

public class TestClientVersionParser {

    private ClientVersionParser unextended;

    @Before
    public void setUp() throws Exception {
        unextended = new ClientVersionParser(false);
    }

    @Test
    public void extendable() {
        ClientVersionParser p = new ClientVersionParser(false);
        assertFalse(p.isExtended());
        p.setExtended(true);
        assertTrue(p.isExtended());
    }

    @Test
    public void parseUnextended() throws IOException {
        int os = 1;
        int version = 2;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CData.writeU16(out, os);
        CData.writeU16(out, version);
        unextended.parse(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(os, unextended.getOs());
        assertEquals(version, unextended.getVersion());
    }

    @Test
    public void parseExtended() throws IOException {
        int os = 1;
        int version = 2;
        long dat = 1337L;
        long spr = 13371337L;
        long pic = 13370000L;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CData.writeU16(out, os);
        CData.writeU16(out, version);
        CData.writeU32(out, dat);
        CData.writeU32(out, spr);
        CData.writeU32(out, pic);

        ClientVersionParser p = new ClientVersionParser(new ByteArrayInputStream(out.toByteArray()), true);
        assertEquals(os, p.getOs());
        assertEquals(version, p.getVersion());
        assertEquals(dat, p.getDataVersion());
        assertEquals(spr, p.getSpriteVersion());
        assertEquals(pic, p.getPicVersion());
    }

    @Test(expected = IllegalStateException.class)
    public void attemptReadDataVersion() {
        unextended.getDataVersion();
    }

    @Test(expected = IllegalStateException.class)
    public void attemptReadSpriteVersion() {
        unextended.getSpriteVersion();
    }

    @Test(expected = IllegalStateException.class)
    public void attemptReadPicVersion() {
        unextended.getPicVersion();
    }

}
